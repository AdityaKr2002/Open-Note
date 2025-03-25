package com.yangdai.opennote.presentation.util

object Constants {
    const val DEFAULT_MAX_LINES = 3
    const val NAV_ANIMATION_TIME = 300
    const val MIME_TYPE_TEXT = "text/"
    const val LINK = "https://www.yangdai-opennote.com"
    const val KEY_DESTINATION = "KEY_DESTINATION"

    object File {
        const val OPENNOTE = "OpenNote"
        const val OPENNOTE_BACKUP = "Backup"
        const val OPENNOTE_IMAGES = "Images"
        const val OPENNOTE_TEMPLATES = "Templates"
        const val OPENNOTE_AUDIO = "Audio"
        const val OPENNOTE_VIDEOS = "Videos"
    }

    object Preferences {
        const val SEARCH_HISTORY = "SEARCH_HISTORY"
        const val APP_THEME = "APP_THEME"
        const val APP_COLOR = "APP_COLOR"
        const val PASSWORD = "PASSWORD"
        const val BIOMETRIC_AUTH_ENABLED = "BIOMETRIC_AUTH_ENABLED"
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
        const val STORAGE_PATH = "STORAGE_PATH"
        const val DATE_FORMATTER = "DATE_FORMATTER"
        const val TIME_FORMATTER = "TIME_FORMATTER"
        const val IS_SCREEN_PROTECTED = "IS_SCREEN_PROTECTED"
        const val FONT_SCALE = "FONT_SCALE"
        const val ENUM_OVERFLOW_STYLE = "ENUM_OVERFLOW_STYLE"
        const val ENUM_CONTENT_SIZE = "ENUM_WIDGET_SIZE"
        const val ENUM_DISPLAY_MODE = "ENUM_DISPLAY_MODE"
        const val IS_AUTO_SAVE_ENABLED = "IS_AUTO_SAVE_ENABLED"
        const val TITLE_ALIGN = "TITLE_ALIGN"
        const val SHOW_LINE_NUMBERS = "LINE_NUMBERS"
    }

    object Widget {
        const val WIDGET_TEXT_SIZE = "WIDGET_TEXT_SIZE"
        const val WIDGET_TEXT_LINES = "WIDGET_TEXT_LINES"
        const val WIDGET_BACKGROUND_COLOR = "WIDGET_BACKGROUND_COLOR"
        const val WIDGET_DISPLAY_MODE = "WIDGET_DISPLAY_MODE"
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
        const val TASK = "task"
        const val LIST = "list"
        const val QUOTE = "quote"
        const val TAB = "tab"
        const val UN_TAB = "unTab"
        const val RULE = "rule"
        const val DIAGRAM = "diagram"

        const val TEXT = "text"
    }
}

val SampleNote = """
    # Markdown Syntax Guide

    ## Headings

    ```
    # Heading 1
    ## Heading 2
    ### Heading 3
    #### Heading 4
    ##### Heading 5
    ###### Heading 6
    ```

    ## Text Formatting

    **Bold text** or __also bold__

    *Italic text* or _also italic_

    ***Bold and italic*** or ___also bold and italic___

    <del>Strikethrough</del> or ~~also Strikethrough~~

    <ins>Underline</ins> or ++also underline++

    <mark>Highlighted text</mark> or ==also highlighted==

    ## Lists

    ### Unordered Lists

    * Item 1
    * Item 2
        + Nested item 2.1
        + Nested item 2.2
            - Deeply nested item
    * Item 3

    ### Ordered Lists

    1. First item
    2. Second item
        1. Nested numbered item
        2. Another nested item
    3. Third item

    ### Task Lists

    - [x] Completed task
    - [ ] Incomplete task
    - [ ] Another task
        - [x] Nested completed subtask
        - [ ] Nested incomplete subtask
    - [x] One more completed task

    ## Links

    [OpenNote](https://github.com/YangDai2003/OpenNote-Compose)

    ## Images

    ![Kotlin](https://kotlinlang.org/docs/images/mascot-in-action.png)

    ## Blockquotes

    > This is a blockquote
    >
    > It can span multiple lines
    >
    > > Nested blockquotes are also possible

    ## Code

    Inline `code` with backticks

    ```kotlin
    fun createConfetti() {
        val colors = listOf("🔴", "🟠", "🟡", "🟢", "🔵", "🟣")
        repeat(20) {
            val color = colors.random()
            val position = (1..80).random()
            println(" ".repeat(position) + color)
            Thread.sleep(50)
        }
        println("🎉 Surprise! 🎉")
    }
    ```

    ## Footnotes

    Here is a sentence with a footnote[^1].

    [^1]: This is the footnote content.

    ## Tables

    | Header 1 | Header 2 | Header 3 |
    |------------|------------|------------|
    | Cell 1 | Cell 2 | Cell 3 |
    | Cell 4 | Cell 5 | Cell 6 |
    | Cell 7 | Cell 8 | Cell 9 |

    ### Table Alignment

    | Left-aligned | Center-aligned | Right-aligned |
    |:---------------|:-------------------:|---------------:|
    | Left         |     Center     |         Right |
    | Left         |     Center     |         Right |

    ## Horizontal Rule

    ---

    or

    ***

    or

    ___

    ## HTML in Markdown

    Markdown supports HTML tags when you need more control over formatting:

    <div style="color: red; text-align: center;">
      <p>This text is red and centered using HTML.</p>
    </div>

    <details>
      <summary>Click to expand!</summary>
    This content is hidden by default but can be expanded by clicking.
    </details>
""".trimIndent()
