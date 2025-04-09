package big.board

import big.board.service.PageLimitCalculator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PageLimitCalculatorTest {

    @Test
    fun calculatorTest() {
        calculatePageLimit(1, 30, 10, 301)
        calculatePageLimit(4, 30, 10, 301)
        calculatePageLimit(7, 30, 10, 301)
        calculatePageLimit(11, 30, 10, 601)
        calculatePageLimit(17, 30, 10, 601)
    }

    private fun calculatePageLimit(page: Long, pageSize: Long, movablePageCount: Long, expected: Long) {
        val result = PageLimitCalculator.calculatePageLimit(page, pageSize, movablePageCount)
        assertThat(result).isEqualTo(expected)
    }
}
