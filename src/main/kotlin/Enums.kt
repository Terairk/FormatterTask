

enum class Direction { FORWARD, BACKWARD }

enum class WhatToSearch { NON_WHITESPACE, LINE_BREAKS, BOTH }

enum class SearchType {
    NOT_FOUND,
    FOUND_NON_WHITESPACE,
    FOUND_LINE_BREAK,
}

data class SearchResult(val type: SearchType, val position: PositionInResult?)

data class SpaceCount(val spaces: Int, val tabs: Int, val visualLength: Int)
