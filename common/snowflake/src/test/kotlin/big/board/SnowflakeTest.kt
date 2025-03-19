package big.board

import big.board.snowflake.Snowflake
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.ArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.Future

class SnowflakeTest {
    private val snowflake = Snowflake()

    @Test
    fun nextIdTest() {
        // given
        val executorService = Executors.newFixedThreadPool(10)
        val futures = ArrayList<Future<List<Long>>>()
        val repeatCount = 1000
        val idCount = 1000

        // when
        for (i in 0 until repeatCount) {
            futures.add(executorService.submit<List<Long>> { generateIdList(snowflake, idCount) })
        }

        // then
        val result = ArrayList<Long>()
        for (future in futures) {
            val idList = future.get()
            for (i in 1 until idList.size) {
                assertThat(idList[i]).isGreaterThan(idList[i - 1])
            }
            result.addAll(idList)
        }
        assertThat(result.distinct().count()).isEqualTo((repeatCount * idCount).toLong())

        executorService.shutdown()
    }

    private fun generateIdList(snowflake: Snowflake, count: Int): List<Long> {
        val idList = ArrayList<Long>()
        var remainingCount = count
        while (remainingCount-- > 0) {
            idList.add(snowflake.nextId())
        }
        return idList
    }

    @Test
    fun nextIdPerformanceTest() {
        // given
        val executorService = Executors.newFixedThreadPool(10)
        val repeatCount = 1000
        val idCount = 1000
        val latch = CountDownLatch(repeatCount)

        // when
        val start = System.nanoTime()
        for (i in 0 until repeatCount) {
            executorService.submit {
                generateIdList(snowflake, idCount)
                latch.countDown()
            }
        }

        latch.await()

        val end = System.nanoTime()
        println("times = ${(end - start) / 1_000_000} ms")

        executorService.shutdown()
    }
}
