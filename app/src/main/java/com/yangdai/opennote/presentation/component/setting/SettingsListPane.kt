package com.yangdai.opennote.presentation.component.setting

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.TextSnippet
import androidx.compose.material.icons.outlined.CloudCircle
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PermDeviceInformation
import androidx.compose.material.icons.outlined.SdStorage
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import com.yangdai.opennote.R
import com.yangdai.opennote.presentation.component.TopBarTitle
import com.yangdai.opennote.presentation.screen.SettingsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsListPane(
    navigateUp: () -> Unit,
    navigateToDetail: (SettingsItem) -> Unit
) {

    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back)
                        )
                    }
                },
                title = {
                    TopBarTitle(title = stringResource(id = R.string.settings))
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->

        val layoutDirection = LocalLayoutDirection.current
        Column(
            Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(layoutDirection)
                )
                .verticalScroll(rememberScrollState())
        ) {

            SettingsSection {
                SettingItem(
                    modifier = Modifier.clickable {
                        navigateToDetail(SettingsItem(0, R.string.style))
                    },
                    headlineText = stringResource(R.string.style),
                    supportingText = stringResource(R.string.dark_mode) + "  •  " + stringResource(R.string.color_platte),
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = "Style"
                        )
                    }
                )

                SettingsSectionDivider()

                SettingItem(
                    modifier = Modifier.clickable {
                        navigateToDetail(SettingsItem(7, R.string.list))
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.GridView,
                            contentDescription = "Grid"
                        )
                    },
                    headlineText = stringResource(R.string.list),
                    supportingText = stringResource(R.string.text_overflow) + "  •  "
                            + stringResource(R.string.card_size)
                )

                SettingsSectionDivider()

                SettingItem(
                    modifier = Modifier.clickable {
                        navigateToDetail(SettingsItem(4, R.string.editor))
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.ModeEdit,
                            contentDescription = "Editor"
                        )
                    },
                    headlineText = stringResource(R.string.editor),
                    supportingText = stringResource(R.string.editor_description)
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    SettingsSectionDivider()

                    SettingItem(
                        modifier = Modifier.clickable {

                            try {
                                val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS)
                                intent.setData(
                                    Uri.fromParts(
                                        "package",
                                        context.packageName,
                                        null
                                    )
                                )
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                try {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    intent.setData(
                                        Uri.fromParts(
                                            "package", context.packageName, null
                                        )
                                    )
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                }
                            }

                        },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Outlined.Language,
                                contentDescription = "Language"
                            )
                        },
                        headlineText = stringResource(R.string.language),
                        supportingText = stringResource(R.string.language_description)
                    )
                }
            }

            SettingsSection {
                SettingItem(
                    modifier = Modifier.clickable {
                        navigateToDetail(SettingsItem(5, R.string.templates))
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.TextSnippet,
                            contentDescription = "Templates"
                        )
                    },
                    headlineText = stringResource(R.string.templates),
                    supportingText = stringResource(R.string.date_format) + "  •  " + stringResource(
                        R.string.time_format
                    )
                )

                SettingsSectionDivider()

                SettingItem(
                    modifier = Modifier.clickable {
                        navigateToDetail(SettingsItem(6, R.string.security))
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Security,
                            contentDescription = "Security"
                        )
                    },
                    headlineText = stringResource(R.string.security),
                    supportingText = stringResource(R.string.screen_protection) + "  •  " + stringResource(
                        R.string.password
                    )
                )

                SettingsSectionDivider()

                SettingItem(
                    modifier = Modifier.clickable {
                        navigateToDetail(SettingsItem(1, R.string.data))
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.SdStorage,
                            contentDescription = "Storage"
                        )
                    },
                    headlineText = stringResource(R.string.data),
                    supportingText = stringResource(R.string.backup) + "  •  " + stringResource(R.string.recovery)
                )

                SettingsSectionDivider()

                SettingItem(
                    modifier = Modifier.clickable {
                        navigateToDetail(SettingsItem(2, R.string.cloud))
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.CloudCircle,
                            contentDescription = "Cloud"
                        )
                    },
                    headlineText = stringResource(R.string.cloud),
                    supportingText = "WebDAV" + "  •  " + stringResource(R.string.sync)
                )
            }

            SettingsSection {
                SettingItem(
                    modifier = Modifier.clickable {
                        navigateToDetail(SettingsItem(8, R.string.widget))
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Widgets,
                            contentDescription = "Widget"
                        )
                    },
                    headlineText = stringResource(R.string.widget),
                    supportingText = stringResource(R.string.font_size) + "  •  " + stringResource(R.string.color)
                )
            }

            SettingsSection {
                SettingItem(
                    modifier = Modifier.clickable {
                        navigateToDetail(SettingsItem(3, R.string.app_info))
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.PermDeviceInformation,
                            contentDescription = "App Info"
                        )
                    },
                    headlineText = stringResource(R.string.app_info),
                    supportingText = stringResource(R.string.version) + "  •  " + stringResource(R.string.guide) + "  •  " + stringResource(
                        R.string.privacy_policy
                    )
                )
            }

            Spacer(Modifier.navigationBarsPadding())
        }
    }
}
