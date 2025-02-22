package com.yangdai.opennote.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.SortByAlpha
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yangdai.opennote.MainActivity
import com.yangdai.opennote.R
import com.yangdai.opennote.data.local.entity.FolderEntity
import com.yangdai.opennote.data.local.entity.NoteEntity
import com.yangdai.opennote.presentation.component.main.AdaptiveNavigationScreen
import com.yangdai.opennote.presentation.component.main.AdaptiveNoteCard
import com.yangdai.opennote.presentation.component.main.AdaptiveTopSearchbar
import com.yangdai.opennote.presentation.component.main.DrawerContent
import com.yangdai.opennote.presentation.component.main.Timeline
import com.yangdai.opennote.presentation.component.dialog.ExportDialog
import com.yangdai.opennote.presentation.component.dialog.FolderListDialog
import com.yangdai.opennote.presentation.component.dialog.OrderSectionDialog
import com.yangdai.opennote.presentation.component.dialog.ProgressDialog
import com.yangdai.opennote.presentation.event.DatabaseEvent
import com.yangdai.opennote.presentation.event.ListEvent
import com.yangdai.opennote.presentation.navigation.Screen
import com.yangdai.opennote.presentation.state.ListNoteContentOverflowStyle
import com.yangdai.opennote.presentation.state.ListNoteContentSize
import com.yangdai.opennote.presentation.util.rememberDateTimeFormatter
import com.yangdai.opennote.presentation.viewmodel.SharedViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainScreen(
    viewModel: SharedViewModel = hiltViewModel(LocalActivity.current as MainActivity),
    isLargeScreen: Boolean,
    navigateToNote: (Long) -> Unit,
    navigateToScreen: (Screen) -> Unit
) {

    val mainScreenData by viewModel.mainScreenDataStateFlow.collectAsStateWithLifecycle()
    val settings by viewModel.settingsStateFlow.collectAsStateWithLifecycle()
    val folderNoteCountsList by viewModel.folderWithNoteCountsFlow.collectAsStateWithLifecycle()
    val dataAction by viewModel.dataActionStateFlow.collectAsStateWithLifecycle()

    val staggeredGridState = rememberLazyStaggeredGridState()
    val navigationDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // Search bar state, reset when configuration changes
    var isSearchActive by remember { mutableStateOf(false) }
    // Selected drawer item and folder, 0 for all, 1 for trash, others for folder index
    var selectedNavDrawerIndex by rememberSaveable { mutableIntStateOf(0) }
    var currentFolder by rememberSaveable(stateSaver = object :
        Saver<FolderEntity, Triple<Long?, String, Int?>> {
        override fun restore(value: Triple<Long?, String, Int?>): FolderEntity {
            return FolderEntity(value.first, value.second, value.third)
        }

        override fun SaverScope.save(value: FolderEntity): Triple<Long?, String, Int?> {
            return Triple(value.id, value.name, value.color)
        }
    }) { mutableStateOf(FolderEntity()) }

    // Record whether multi-select mode has been enabled, selected items and whether all items have been selected
    var isMultiSelectEnabled by remember { mutableStateOf(false) }
    var selectedNotesSet by remember { mutableStateOf<Set<NoteEntity>>(emptySet()) }
    var allNotesSelected by remember { mutableStateOf(false) }

    // Whether to show the floating button, determined by the scroll state of the grid, the selected drawer, the search bar, and whether multi-select mode is enabled
    val isAddNoteFabVisible by remember {
        derivedStateOf {
            selectedNavDrawerIndex != 1 && !isSearchActive && !isMultiSelectEnabled
                    && !staggeredGridState.isScrollInProgress
        }
    }

    // Reset multi-select mode
    fun initializeNoteSelection() {
        isMultiSelectEnabled = false
        selectedNotesSet = emptySet()
        allNotesSelected = false
    }

    LaunchedEffect(selectedNavDrawerIndex, currentFolder) {
        initializeNoteSelection()
        when (selectedNavDrawerIndex) {
            0 -> viewModel.onListEvent(ListEvent.Sort(trash = false))

            1 -> viewModel.onListEvent(ListEvent.Sort(trash = true))

            else -> viewModel.onListEvent(
                ListEvent.Sort(
                    filterFolder = true,
                    folderId = currentFolder.id
                )
            )
        }
    }

    // select all and deselect all, triggered by the checkbox in the bottom bar
    LaunchedEffect(allNotesSelected) {
        selectedNotesSet = if (allNotesSelected) mainScreenData.notes.toSet()
        else emptySet()
    }

    // Back logic for better user experience
    BackHandler(isMultiSelectEnabled) {
        initializeNoteSelection()
    }

    var isMoveToFolderDialogVisible by remember { mutableStateOf(false) }
    var isExportNotesDialogVisible by remember { mutableStateOf(false) }

    AdaptiveNavigationScreen(
        isLargeScreen = isLargeScreen,
        drawerState = navigationDrawerState,
        gesturesEnabled = !isMultiSelectEnabled && !isSearchActive,
        drawerContent = {
            DrawerContent(
                folderNoteCounts = folderNoteCountsList,
                selectedDrawerIndex = selectedNavDrawerIndex,
                showLock = settings.password.isNotEmpty(),
                onLockClick = {
                    scope.launch {
                        navigationDrawerState.apply {
                            close()
                        }
                    }
                    viewModel.authenticated.value = false
                },
                navigateTo = { navigateToScreen(it) }
            ) { index, folderEntity ->
                scope.launch {
                    navigationDrawerState.apply {
                        close()
                    }
                }
                selectedNavDrawerIndex = index
                currentFolder = folderEntity
            }
        },
    ) {
        Scaffold(
            topBar = {
                if (selectedNavDrawerIndex != 0) {
                    TopAppBar(
                        title = {
                            Text(
                                text = if (selectedNavDrawerIndex == 1) stringResource(id = R.string.trash)
                                else currentFolder.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            if (!isLargeScreen) {
                                IconButton(
                                    enabled = !isMultiSelectEnabled,
                                    onClick = {
                                        scope.launch {
                                            navigationDrawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Menu,
                                        contentDescription = "Open Menu"
                                    )
                                }
                            }
                        },
                        actions = {

                            var showMenu by remember {
                                mutableStateOf(false)
                            }

                            IconButton(onClick = { viewModel.onListEvent(ListEvent.ChangeViewMode) }) {
                                Icon(
                                    imageVector = if (!settings.isListView) Icons.Outlined.ViewAgenda else Icons.Outlined.GridView,
                                    contentDescription = "View Mode"
                                )
                            }
                            IconButton(onClick = { viewModel.onListEvent(ListEvent.ToggleOrderSection) }) {
                                Icon(
                                    imageVector = Icons.Outlined.SortByAlpha,
                                    contentDescription = "Sort"
                                )
                            }
                            if (selectedNavDrawerIndex == 1) {
                                IconButton(onClick = { showMenu = !showMenu }) {
                                    Icon(
                                        imageVector = Icons.Outlined.MoreVert,
                                        contentDescription = "More"
                                    )
                                }

                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }) {
                                    DropdownMenuItem(
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Outlined.RestartAlt,
                                                contentDescription = "Restore"
                                            )
                                        },
                                        text = { Text(text = stringResource(id = R.string.restore_all)) },
                                        onClick = {
                                            viewModel.onListEvent(
                                                ListEvent.RestoreNotes(mainScreenData.notes)
                                            )
                                        })

                                    DropdownMenuItem(
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Outlined.Delete,
                                                contentDescription = "Delete"
                                            )
                                        },
                                        text = { Text(text = stringResource(id = R.string.delete_all)) },
                                        onClick = {
                                            viewModel.onListEvent(
                                                ListEvent.DeleteNotes(mainScreenData.notes, false)
                                            )
                                        })
                                }
                            }
                        }
                    )
                }
//                else {
//                    AdaptiveSearchbar2 (
//                        modifier = Modifier
//                            .semantics { traversalIndex = 0f },
//                        enabled = !isMultiSelectionModeEnabled,
//                        isLargeScreen = isLargeScreen,
//                        searchBarState = rememberSearchBarState(),
//                        onDrawerStateChange = {
//                            coroutineScope.launch {
//                                drawerState.apply {
//                                    if (isClosed) open() else close()
//                                }
//                            }
//                        }
//                    )
//                }
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = isMultiSelectEnabled,
                    enter = slideInVertically { fullHeight -> fullHeight },
                    exit = slideOutVertically { fullHeight -> fullHeight }
                ) {
                    BottomAppBar {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Row(
                                modifier = Modifier.fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Checkbox(
                                    checked = allNotesSelected,
                                    onCheckedChange = { allNotesSelected = it }
                                )

                                Text(text = stringResource(R.string.checked))

                                Text(text = selectedNotesSet.size.toString())
                            }

                            Row(
                                modifier = Modifier.fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                if (selectedNavDrawerIndex == 1) {
                                    TextButton(onClick = {
                                        viewModel.onListEvent(
                                            ListEvent.RestoreNotes(selectedNotesSet)
                                        )
                                        initializeNoteSelection()
                                    }) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = Icons.Outlined.RestartAlt,
                                                contentDescription = "Restore"
                                            )
                                            Text(
                                                text = stringResource(id = R.string.restore),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                } else {
                                    TextButton(onClick = {
                                        isExportNotesDialogVisible = true
                                    }) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = Icons.Outlined.Upload,
                                                contentDescription = "Export"
                                            )
                                            Text(
                                                text = stringResource(id = R.string.export),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    TextButton(onClick = { isMoveToFolderDialogVisible = true }) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Outlined.DriveFileMove,
                                                contentDescription = "Move"
                                            )
                                            Text(
                                                text = stringResource(id = R.string.move),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }

                                TextButton(onClick = {
                                    viewModel.onListEvent(
                                        ListEvent.DeleteNotes(
                                            selectedNotesSet,
                                            selectedNavDrawerIndex != 1
                                        )
                                    )
                                    initializeNoteSelection()
                                }) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = "Delete"
                                        )
                                        Text(
                                            text = stringResource(id = R.string.delete),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            floatingActionButton = {

                val hapticFeedback = LocalHapticFeedback.current
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val scale by animateFloatAsState(
                    targetValue = if (isPressed) 0.9f else 1f,
                    label = "scale"
                )

                FloatingActionButton(
                    modifier = Modifier
                        .scale(scale)
                        .animateFloatingActionButton(
                            visible = isAddNoteFabVisible,
                            alignment = Alignment.BottomEnd
                        ),
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                        viewModel.onListEvent(
                            ListEvent.OpenOrCreateNote(
                                null,
                                currentFolder.id
                            )
                        )
                        navigateToNote(-1)
                    },
                    interactionSource = interactionSource
                ) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
                }

            }) { innerPadding ->

            // 确保不被遮挡
            val direction = LocalLayoutDirection.current
            val cutOutInsets = WindowInsets.displayCutout.asPaddingValues()
            val paddingValues = remember(direction, cutOutInsets) {
                PaddingValues(
                    start = cutOutInsets.calculateStartPadding(direction),
                    end = cutOutInsets.calculateEndPadding(direction)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .semantics { isTraversalGroup = true }
            ) {

                if (selectedNavDrawerIndex == 0) {
                    AdaptiveTopSearchbar(
                        modifier = Modifier
                            .zIndex(1f)
                            .align(Alignment.TopCenter)
                            .semantics { traversalIndex = 0f },
                        enabled = !isMultiSelectEnabled,
                        isLargeScreen = isLargeScreen,
                        onSearchBarActivationChange = { isActive ->
                            isSearchActive = isActive
                        },
                        onDrawerStateChange = {
                            scope.launch {
                                navigationDrawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    )
                }

                // 如果没有笔记，不显示，性能优化
                if (mainScreenData.notes.isEmpty()) {
                    return@Box
                }

                AnimatedVisibility(
                    visible = settings.isListView,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Timeline(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxHeight()
                            .padding(start = 8.dp),
                        thickness = 2.dp
                    )
                }

                val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
                val contentPadding =
                    remember(statusBarPadding, innerPadding, settings.isListView) {

                        if (!settings.isListView) PaddingValues(
                            top = statusBarPadding.calculateTopPadding() + 78.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = innerPadding.calculateBottomPadding()
                        ) else PaddingValues(
                            top = statusBarPadding.calculateTopPadding() + 74.dp,
                            start = 5.dp,
                            end = 16.dp,
                            bottom = innerPadding.calculateBottomPadding()
                        )
                    }
                val textOverflow = remember(settings.enumOverflowStyle) {
                    when (settings.enumOverflowStyle) {
                        ListNoteContentOverflowStyle.CLIP -> TextOverflow.Clip
                        else -> TextOverflow.Ellipsis
                    }
                }
                val maxLines = remember(settings.enumContentSize) {
                    when (settings.enumContentSize) {
                        ListNoteContentSize.DEFAULT -> 12
                        ListNoteContentSize.COMPACT -> 6
                        else -> Int.MAX_VALUE
                    }
                }
                val dateTimeFormatter = rememberDateTimeFormatter()
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { traversalIndex = 1f },
                    state = staggeredGridState,
                    // The staggered grid layout is adaptive, with a minimum column width of 160dp(mdpi)
                    columns = if (settings.isListView) StaggeredGridCells.Fixed(1)
                    else StaggeredGridCells.Adaptive(160.dp),
                    verticalItemSpacing = if (settings.isListView) 12.dp else 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    // for better edgeToEdge experience
                    contentPadding = contentPadding,
                    content = {
                        items(
                            items = mainScreenData.notes,
                            key = { note: NoteEntity -> note.id!! },
                            contentType = { "NoteItem" }
                        ) { note ->
                            AdaptiveNoteCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem(),
                                isListView = settings.isListView,
                                displayedNote = note,
                                dateFormatter = dateTimeFormatter,
                                contentMaxLines = maxLines,
                                contentTextOverflow = textOverflow,
                                isEditMode = isMultiSelectEnabled,
                                isNoteSelected = selectedNotesSet.contains(note),
                                onEditModeChange = { isMultiSelectEnabled = it },
                                onSelectNote = {
                                    if (isMultiSelectEnabled) {
                                        selectedNotesSet =
                                            if (selectedNotesSet.contains(it))
                                                selectedNotesSet.minus(it)
                                            else selectedNotesSet.plus(it)
                                    } else {
                                        if (selectedNavDrawerIndex != 1) {
                                            viewModel.onListEvent(
                                                ListEvent.OpenOrCreateNote(
                                                    it,
                                                    null
                                                )
                                            )
                                            navigateToNote(it.id!!)
                                        } else {
                                            Unit
                                        }
                                    }
                                }
                            )
                        }
                    }
                )
            }

            if (mainScreenData.isOrderSectionVisible) {
                OrderSectionDialog(
                    noteOrder = mainScreenData.noteOrder,
                    onOrderChange = {
                        viewModel.onListEvent(
                            ListEvent.Sort(
                                noteOrder = it,
                                trash = selectedNavDrawerIndex == 1,
                                filterFolder = selectedNavDrawerIndex != 0 && selectedNavDrawerIndex != 1,
                                folderId = currentFolder.id
                            )
                        )
                    },
                    onDismiss = { viewModel.onListEvent(ListEvent.ToggleOrderSection) }
                )
            }

            if (isExportNotesDialogVisible) {
                val context = LocalContext.current
                ExportDialog(onDismissRequest = { isExportNotesDialogVisible = false }) {
                    viewModel.onDatabaseEvent(
                        DatabaseEvent.ExportFiles(
                            context.applicationContext,
                            selectedNotesSet.toList(),
                            it
                        )
                    )
                    isExportNotesDialogVisible = false
                }
            }

            if (isMoveToFolderDialogVisible) {
                FolderListDialog(
                    hint = stringResource(R.string.destination_folder),
                    oFolderId = currentFolder.id,
                    folders = folderNoteCountsList.map { it.first },
                    onDismissRequest = { isMoveToFolderDialogVisible = false }
                ) {
                    viewModel.onListEvent(ListEvent.MoveNotes(selectedNotesSet, it))
                    initializeNoteSelection()
                }
            }

            ProgressDialog(
                isLoading = dataAction.loading,
                progress = dataAction.progress,
                message = dataAction.message,
                onDismissRequest = viewModel::cancelDataAction
            )
        }
    }
}
