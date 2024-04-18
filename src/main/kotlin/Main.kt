// This main function was made in a hurry so it may not work properly
// Please check the test file for the correct usage of the TextWithChanges class
// Note you can only select indices which correspond to non whitespace characters

// Example usage: (don't actually enter the quotation marks)
// Enter: "Hello       World"
// Start position: 5 (this needs to be from 5-12 for this example)
// End position: 8
// Replacement text: "   "


fun main() {
    println("\nEnter the initial text (or 'quit' to exit):")
    println("Enter text here: ")
    val initialText = readlnOrNull() ?: ""
    if (initialText.lowercase() == "quit") {
        println("Goodbye!")
        return
    }

    val text = TextWithChanges(initialText)

    while (true) {
        println("\nCurrent text: ${text.applyChanges()}")
        println("Enter a command:")
        println("1. Add a change")
        println("2. Search for a non-whitespace character")
        println("3. Search for a line break")
        println("4. Count line breaks")
        println("5. Count simple spaces")
        println("6. Reset text")
        println("7. Quit")

        println("Enter command here: ")

        val command = readlnOrNull()?.toIntOrNull() ?: continue

        when (command) {
            1 -> {
                println("Enter the start position:")
                val start = readlnOrNull()?.toIntOrNull() ?: continue
                println("Enter the end position:")
                val end = readlnOrNull()?.toIntOrNull() ?: continue
                println("Enter the replacement text:")
                val replacement = readLine() ?: ""
                text.addChange(RangeInResult(PositionInResult(start), PositionInResult(end)), replacement)
            }
            2 -> {
                println("Enter the start position:")
                val start = readlnOrNull()?.toIntOrNull() ?: continue
                println("Enter the end position:")
                val end = readlnOrNull()?.toIntOrNull() ?: continue
                val range = RangeInResult(PositionInResult(start), PositionInResult(end))
                val result = text.search(range, Direction.FORWARD, WhatToSearch.NON_WHITESPACE)
                println("Search result: $result")
            }
            3 -> {
                println("Enter the start position:")
                val start = readlnOrNull()?.toIntOrNull() ?: continue
                println("Enter the end position:")
                val end = readlnOrNull()?.toIntOrNull() ?: continue
                val range = RangeInResult(PositionInResult(start), PositionInResult(end))
                val result = text.search(range, Direction.FORWARD, WhatToSearch.LINE_BREAKS)
                println("Search result: $result")
            }
            4 -> {
                println("Enter the start position:")
                val start = readlnOrNull()?.toIntOrNull() ?: continue
                println("Enter the end position:")
                val end = readlnOrNull()?.toIntOrNull() ?: continue
                val range = RangeInResult(PositionInResult(start), PositionInResult(end))
                val count = text.countLineBreaks(range)
                println("Line break count: $count")
            }
            5 -> {
                println("Enter the start position:")
                val start = readlnOrNull()?.toIntOrNull() ?: continue
                println("Enter the end position:")
                val end = readlnOrNull()?.toIntOrNull() ?: continue
                println("Enter the tab width:")
                val tabWidth = readlnOrNull()?.toIntOrNull() ?: continue
                val range = RangeInResult(PositionInResult(start), PositionInResult(end))
                val spaceCount = text.countSimpleSpaces(range, tabWidth)
                println("Space count: $spaceCount")
            }
            6 -> break
            7 -> {
                println("Goodbye!")
                return
            }
            else -> println("Invalid command. Please try again.")
        }
    }
}