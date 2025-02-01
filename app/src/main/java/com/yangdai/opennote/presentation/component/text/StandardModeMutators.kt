package com.yangdai.opennote.presentation.component.text

import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.ui.text.TextRange

fun TextFieldBuffer.moveCursorLeft() {
    if (selection.min > 0) {
        selection = TextRange(selection.min - 1, selection.min - 1)
    }
}

fun TextFieldBuffer.moveCursorRight() {
    if (selection.max < length) {
        selection = TextRange(selection.max + 1, selection.max + 1)
    }
}

private fun TextFieldBuffer.inlineWrap(
    startWrappedString: String,
    endWrappedString: String = startWrappedString,
    initialSelection: TextRange = selection
) = if (initialSelection.collapsed) {
    // No text selected, insert at cursor position and place cursor in the middle
    replace(initialSelection.min, initialSelection.min, startWrappedString + endWrappedString)
    selection = TextRange(
        initialSelection.min + startWrappedString.length,
        initialSelection.min + startWrappedString.length
    )
} else {
    replace(initialSelection.min, initialSelection.min, startWrappedString)
    replace(
        initialSelection.max + startWrappedString.length,
        initialSelection.max + startWrappedString.length,
        endWrappedString
    )
    selection = TextRange(
        initialSelection.min,
        initialSelection.max + startWrappedString.length + endWrappedString.length
    )
}

fun TextFieldBuffer.bold() = inlineWrap("**")

fun TextFieldBuffer.italic() = inlineWrap("_")

fun TextFieldBuffer.underline() = inlineWrap("++")

fun TextFieldBuffer.strikeThrough() = inlineWrap("~~")

fun TextFieldBuffer.mark() = inlineWrap("<mark>", "</mark>")

fun TextFieldBuffer.inlineBrackets() = inlineWrap("[", "]")

fun TextFieldBuffer.inlineBraces() = inlineWrap("{", "}")

fun TextFieldBuffer.inlineCode() = inlineWrap("`")

fun TextFieldBuffer.inlineMath() = inlineWrap("$")

fun TextFieldBuffer.quote() {
    val text = toString()
    val lineStart = text.take(selection.min)
        .lastIndexOf('\n')
        .takeIf { it != -1 }
        ?.let { it + 1 }
        ?: 0

    val initialSelection = selection

    replace(lineStart, lineStart, "> ")
    selection = TextRange(
        initialSelection.min + 2,
        initialSelection.max + 2
    )
}

fun TextFieldBuffer.tab() {
    val text = toString()
    val lineStart = text.take(selection.min)
        .lastIndexOf('\n')
        .takeIf { it != -1 }
        ?.let { it + 1 }
        ?: 0

    val initialSelection = selection

    replace(lineStart, lineStart, "\t")
    selection = TextRange(
        initialSelection.min + "\t".length,
        initialSelection.max + "\t".length
    )
}

fun TextFieldBuffer.unTab() {
    val text = toString()
    val lineStart = text.take(selection.min)
        .lastIndexOf('\n')
        .takeIf { it != -1 }
        ?.let { it + 1 }
        ?: 0

    val tabIndex = text.indexOf('\t', lineStart)
    val initialSelection = selection

    if (tabIndex != -1 && tabIndex < selection.min) {
        replace(tabIndex, tabIndex + 1, "")
        val tabLength = 1
        selection = TextRange(
            initialSelection.min - tabLength,
            initialSelection.max - tabLength
        )
    }
}

fun TextFieldBuffer.add(str: String) {
    val initialSelection = selection
    replace(initialSelection.min, initialSelection.max, str)
}

fun TextFieldBuffer.addInNewLine(str: String) {
    val text = toString()
    if (selection.min != 0 && text[selection.min - 1] != '\n') {
        // 如果不是换行符，那么就先添加一个换行符
        add("\n")
    }
    add(str)
}

fun TextFieldBuffer.addMermaid() {
    addInNewLine("<pre class=\"mermaid\">\n</pre>")
}

fun TextFieldBuffer.addHeader(level: Int) {
    addInNewLine("#".repeat(level) + " ")
}

fun TextFieldBuffer.addRule() {
    addInNewLine("\n")
    add("------")
    add("\n")
    add("\n")
}

fun TextFieldBuffer.addTask(task: String, checked: Boolean) {
    if (checked) {
        addInNewLine("- [x] $task")
    } else {
        addInNewLine("- [ ] $task")
    }
}

fun TextFieldBuffer.addTable(row: Int, column: Int) = addInNewLine(
    buildString {
        append("|")
        repeat(column) { append(" HEADER |") }
        append("\n|")
        repeat(column) { append(" :-----------: |") }
        repeat(row) {
            append("\n|")
            repeat(column) { append(" Element |") }
        }
    }
)