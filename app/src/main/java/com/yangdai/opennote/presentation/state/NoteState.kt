package com.yangdai.opennote.presentation.state

import androidx.compose.runtime.Stable

@Stable
data class NoteState(
    val id: Long? = null,
    val folderId: Long? = null,
    val isStandard: Boolean = true,
    val timestamp: Long? = null
)