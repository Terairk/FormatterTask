

import java.util.TreeSet

class TextWithChanges(val originalText: String) {
    private val changes = TreeSet<Change>(compareBy({ it.range.first }, { it.range.last }))

    fun addChange(
        range: RangeInResult,
        replacement: String,
    ) {
        // Convert RangeInResult to a range in the original text
        val originalRange = rangeInResultToOriginalRange(range)

        // Check that the change only modifies whitespace
        require(originalText.substring(originalRange).isWhitespace() && replacement.isWhitespace()) {
            "Changes must only modify whitespace"
        }

        // Check for overlapping or adjacent changes
        val overlappingChange = changes.floor(Change(originalRange, replacement))
        if (overlappingChange != null && overlappingChange.range.last >= originalRange.first) {
            // Update or remove the overlapping change
            // First conjunct may be unnecessary due to nature of floor
            if (overlappingChange.range.first <= originalRange.first &&
                overlappingChange.range.last >= originalRange.last
            ) {
                // New change is completely inside the old change, update the old change
                val updatedReplacement =
                    overlappingChange.replacement.substring(0, originalRange.first - overlappingChange.range.first) +
                        replacement +
                        overlappingChange.replacement.substring(originalRange.last - overlappingChange.range.first)
                changes.remove(overlappingChange)
                changes.add(Change(overlappingChange.range, updatedReplacement))
            } else if (overlappingChange.range == originalRange) {
                // New change completely replaces the old change, remove the old change
                changes.remove(overlappingChange)
                changes.add(Change(originalRange, replacement))
            } else {
                require(
                    overlappingChange.range.first > originalRange.last ||
                        overlappingChange.range.last < originalRange.first,
                ) {
                    "Changes must not overlap"
                }
                // Changes are adjacent since they can't be overlapping
                // so we merge them

                val mergedRange = overlappingChange.range.first..originalRange.last
                val mergedReplacement = overlappingChange.replacement + replacement
                changes.remove(overlappingChange)
                changes.add(Change(mergedRange, mergedReplacement))
            }
        } else {
            // No overlapping or adjacent changes, add the new change
            changes.add(Change(originalRange, replacement))
        }
    }

    fun search(
        range: RangeInResult,
        direction: Direction,
        whatToSearch: WhatToSearch,
    ): SearchResult {
        // Convert the RangeInResult to a range in the original text
        val start = rangeInResultToOriginalRange(range).first
        val end = rangeInResultToOriginalRange(range).last

        // Determine the step based on the search direction
        val step = if (direction == Direction.FORWARD) 1 else -1
        // Set the initial position based on the search direction
        var currentPosition = if (direction == Direction.FORWARD) start else end

        // Iterate over the positions in the range
        while (currentPosition in start..end) {
            // Convert the current position to a PositionInResult
            val positionInResult = originalPositionToPositionInResult(currentPosition)
            // Get the character at the current PositionInResult
            val char = getCharAtPositionInResult(positionInResult)

            // Check if the character matches the search criteria
            when (whatToSearch) {
                WhatToSearch.NON_WHITESPACE -> {
                    if (!char.isWhitespace()) {
                        return SearchResult(SearchType.FOUND_NON_WHITESPACE, positionInResult)
                    }
                }

                WhatToSearch.LINE_BREAKS -> {
                    if (char == '\n') {
                        return SearchResult(SearchType.FOUND_LINE_BREAK, positionInResult)
                    }
                }

                WhatToSearch.BOTH -> {
                    if (!char.isWhitespace() || char == '\n') {
                        return SearchResult(SearchType.FOUND_NON_WHITESPACE, positionInResult)
                    }
                }
            }

            // Move to the next position based on the step
            currentPosition += step
        }

        // If no match is found, return a SearchResult with found=false and null position
        return SearchResult(SearchType.NOT_FOUND, null)
    }

    fun countLineBreaks(range: RangeInResult): Int {
        // Convert the RangeInResult to a range in the original text
        val start = rangeInResultToOriginalRange(range).first
        val end = rangeInResultToOriginalRange(range).last

        var count = 0
        // Iterate over the positions in the range
        for (i in start..end) {
            // Convert the current position to a PositionInResult
            val positionInResult = originalPositionToPositionInResult(i)
            // Check if the character at the current PositionInResult is a line break
            if (getCharAtPositionInResult(positionInResult) == '\n') {
                // If it's a line break, increment the count
                count++
            }
        }

        // Return the total count of line breaks
        return count
    }

    fun countSimpleSpaces(
        range: RangeInResult,
        tabWidth: Int,
    ): SpaceCount {
        // Convert the RangeInResult to a range in the original text
        val start = rangeInResultToOriginalRange(range).first
        val end = rangeInResultToOriginalRange(range).last

        var spaces = 0
        var tabs = 0
        var visualLength = 0
        var currentColumn = 0

        // Iterate over the positions in the range
        for (i in start..end) {
            // Convert the current position to a PositionInResult
            val positionInResult = originalPositionToPositionInResult(i)
            // Get the character at the current PositionInResult
            val char = getCharAtPositionInResult(positionInResult)

            when (char) {
                ' ' -> {
                    // If it's a space, increment the spaces count and the current column
                    spaces++
                    currentColumn++
                }
                '\t' -> {
                    // If it's a tab, increment the tabs count and calculate the new column position
                    tabs++
                    currentColumn += tabWidth - (currentColumn % tabWidth)
                }
                '\n' -> {
                    // If it's a line break, reset the current column to 0
                    currentColumn = 0
                }
                else -> {
                    // If it's any other character, increment the current column
                    currentColumn++
                }
            }

            // Update the visual length if the current column exceeds the previous visual length
            visualLength = maxOf(visualLength, currentColumn)
        }

        // Return a SpaceCount object with the counts of spaces, tabs, and the visual length
        return SpaceCount(spaces, tabs, visualLength)
    }

    fun applyChanges(): String {
        // Create a StringBuilder with the original text
        val result = StringBuilder(originalText)

        // Iterate over the changes in reverse order
        for (change in changes.reversed()) {
            // Replace the range in the StringBuilder with the replacement string of the change
            result.replace(change.range.first, change.range.last + 1, change.replacement)
        }

        // Return the resulting text as a string
        return result.toString()
    }

    private fun rangeInResultToOriginalRange(range: RangeInResult): IntRange {
        // Convert the start and end positions of the RangeInResult to positions in the original text
        val start = positionInResultToOriginalPosition(range.start)
        val end = positionInResultToOriginalPosition(range.end)
        // Return an IntRange representing the range in the original text
        return start until end
    }

    private fun originalPositionToPositionInResult(position: Int): PositionInResult {
        // Find the change that covers the given position using the floor method of the TreeSet
        val change = changes.floor(Change(position..position, ""))
        return if (change != null && position in change.range) {
            // If a change is found and the position is within its range,
            // calculate the offset within the change and create a PositionInResult with the change and offset
            val offsetInChange = position - change.range.first
            PositionInResult(position, change, offsetInChange)
        } else {
            // If no change is found or the position is not within any change's range,
            // create a PositionInResult with just the position
            PositionInResult(position)
        }
    }

    private fun positionInResultToOriginalPosition(positionInResult: PositionInResult): Int {
        return if (positionInResult.change != null) {
            // If the PositionInResult has a change associated with it,
            // calculate the position in the original text by adding the start of the change's range
            // and the offset within the change e
            return positionInResult.change.range.first + positionInResult.offsetInChange
        } else {
            // If the PositionInResult doesn't have a change associated with it,
            // return the offset as the position in the original text
            positionInResult.offset
        }
    }

    private fun getCharAtPositionInResult(positionInResult: PositionInResult): Char {
        return if (positionInResult.change != null) {
            // If the PositionInResult has a change associated with it,
            // retrieve the character from the change's replacement string at the corresponding offset
            positionInResult.change.replacement[positionInResult.offsetInChange]
        } else {
            // If the PositionInResult doesn't have a change associated with it,
            // retrieve the character from the original text at the offset
            originalText[positionInResult.offset]
        }
    }

    private fun String.isWhitespace(): Boolean {
        // Extension function to check if a string consists of only whitespace characters
        return all { it.isWhitespace() }
    }
}
