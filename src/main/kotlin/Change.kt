// IntRange by default has endInclusive as constructor parameters
// which may lead to confusions, need to be careful not to use constructor

data class PositionInResult(val offset: Int, val change: Change? = null, val offsetInChange: Int = 0)

data class RangeInResult(val start: PositionInResult, val end: PositionInResult)

data class Change(val range: IntRange, val replacement: String)

/*
data class PositionInResult(val offset: Int, val change: Change? = null)
data class RangeInResult(val start: PositionInResult, val end: PositionInResult)
*/
