import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith

class TextWithChangesTest : FunSpec({
    test("addChange with non-overlapping changes") {
        val text = TextWithChanges("hello world")
        text.addChange(RangeInResult(PositionInResult(5), PositionInResult(5)), " ")
        text.addChange(RangeInResult(PositionInResult(11), PositionInResult(11)), " ")
        text.applyChanges() shouldBe "hello  world "
    }

    test("addChange with overlapping changes completely inside") {
        val text = TextWithChanges("he   world")
        text.addChange(RangeInResult(PositionInResult(2), PositionInResult(5)), "  ")
        text.addChange(RangeInResult(PositionInResult(2), PositionInResult(4)), " ")
        text.applyChanges() shouldBe "he world"
    }

    test("addChange with adjacent changes") {
        val text = TextWithChanges("hello      world")
        text.addChange(RangeInResult(PositionInResult(5), PositionInResult(7)), " ")
        text.addChange(RangeInResult(PositionInResult(7), PositionInResult(11)), "")
        text.applyChanges() shouldBe "hello world"
    }

    test("search for non-whitespace character") {
        val text = TextWithChanges("hello world")
        val range = RangeInResult(PositionInResult(0), PositionInResult(5))
        val result = text.search(range, Direction.FORWARD, WhatToSearch.NON_WHITESPACE)
        result.type shouldBe SearchType.FOUND_NON_WHITESPACE
        result.position shouldBe PositionInResult(0)
    }

    test("search for line break") {
        val text = TextWithChanges("hello\nworld")
        val range = RangeInResult(PositionInResult(0), PositionInResult(11))
        val result = text.search(range, Direction.FORWARD, WhatToSearch.LINE_BREAKS)
        result.type shouldBe SearchType.FOUND_LINE_BREAK
        result.position shouldBe PositionInResult(5)
    }

    test("countLineBreaks") {
        val text = TextWithChanges("hello\nworld\n!")
        val range = RangeInResult(PositionInResult(0), PositionInResult(13))
        text.countLineBreaks(range) shouldBe 2
    }

    test("countSimpleSpaces") {
        val text = TextWithChanges("hello\tworld \n!")
        val range = RangeInResult(PositionInResult(0), PositionInResult(14))
        val spaceCount = text.countSimpleSpaces(range, tabWidth = 4)
        spaceCount.spaces shouldBe 1
        spaceCount.tabs shouldBe 1
        spaceCount.visualLength shouldBe 14
    }

    test("example before and after formatting") {
        val beforeFormatter = "while( true){foo( );}"
        val afterFormatter = "while (true)\n{\n    foo();\n}"

        val text = TextWithChanges(beforeFormatter)
        text.addChange(RangeInResult(PositionInResult(5), PositionInResult(5)), " ")
        text.addChange(RangeInResult(PositionInResult(6), PositionInResult(7)), "")
        text.addChange(RangeInResult(PositionInResult(12), PositionInResult(12)), "\n")
        text.addChange(RangeInResult(PositionInResult(13), PositionInResult(13)), "\n    ")
        text.addChange(RangeInResult(PositionInResult(17), PositionInResult(18)), "")
        text.addChange(RangeInResult(PositionInResult(20), PositionInResult(20)), "\n")
        text.applyChanges() shouldBe afterFormatter
    }

    test("should throw exception if delete non whitespace") {
        val exception =
            shouldThrow<IllegalArgumentException> {
                val text = TextWithChanges("hello world")
                text.addChange(RangeInResult(PositionInResult(0), PositionInResult(5)), "")
            }

        exception.message should startWith("Changes must only modify whitespace")
    }

    test("should throw exception if ranges intersect but not completely") {
        val exception =
            shouldThrow<IllegalArgumentException> {
                val text = TextWithChanges("hello      world")
                text.addChange(RangeInResult(PositionInResult(5), PositionInResult(8)), " ")
                text.addChange(RangeInResult(PositionInResult(7), PositionInResult(11)), " ")
            }

        exception.message should startWith("Changes must not overlap")
    }
})
