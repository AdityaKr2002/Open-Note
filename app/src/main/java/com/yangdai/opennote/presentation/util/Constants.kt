package com.yangdai.opennote.presentation.util

object Constants {
    const val DEFAULT_MAX_LINES = 2
    const val NAV_ANIMATION_TIME = 300
    const val MIME_TYPE_TEXT = "text/"
    const val LINK = "https://www.yangdai-opennote.com"
    object File {
        const val OPENNOTE = "OpenNote"
        const val OPENNOTE_BACKUP = "OpenNote/Backup"
        const val OPENNOTE_IMAGES = "OpenNote/Images"
    }

    object Preferences {
        const val SEARCH_HISTORY = "SEARCH_HISTORY"
        const val APP_THEME = "APP_THEME"
        const val APP_COLOR = "APP_COLOR"
        const val NEED_PASSWORD = "NEED_PASSWORD"
        const val IS_APP_IN_AMOLED_MODE = "IS_APP_IN_AMOLED_MODE"
        const val IS_APP_IN_DARK_MODE = "IS_APP_IN_DARK_MODE"
        const val SHOULD_FOLLOW_SYSTEM = "SHOULD_FOLLOW_SYSTEM"
        const val IS_SWITCH_ACTIVE = "IS_DARK_SWITCH_ACTIVE"
        const val MASK_CLICK_X = 0f
        const val MASK_CLICK_Y = 0f
        const val IS_LIST_VIEW = "IS_List_VIEW"
        const val IS_DEFAULT_VIEW_FOR_READING = "IS_DEFAULT_VIEW_FOR_READING"
        const val IS_DEFAULT_LITE_MODE = "IS_DEFAULT_LITE_MODE"
        const val IS_LINT_ACTIVE = "IS_LINT_ACTIVE"
    }

    object Editor {
        const val UNDO = "undo"
        const val REDO = "redo"

        const val H1 = "h1"
        const val H2 = "h2"
        const val H3 = "h3"
        const val H4 = "h4"
        const val H5 = "h5"
        const val H6 = "h6"

        const val BOLD = "bold"
        const val ITALIC = "italic"
        const val UNDERLINE = "underline"
        const val STRIKETHROUGH = "strikethrough"
        const val MARK = "mark"

        const val INLINE_CODE = "inlineCode"
        const val INLINE_BRACKETS = "inlineBrackets"
        const val INLINE_BRACES = "inlineBraces"
        const val INLINE_MATH = "inlineMath"

        const val TABLE = "table"
        const val LIST = "list"
        const val QUOTE = "quote"
        const val TAB = "tab"
        const val UN_TAB = "unTab"
        const val RULE = "rule"
        const val DIAGRAM = "diagram"

        const val TEXT = "text"
        const val TITLE = "title"
        const val NEW_TEXT = "newText"
    }

}
