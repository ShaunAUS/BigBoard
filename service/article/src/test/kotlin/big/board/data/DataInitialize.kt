package big.board.data

import big.board.dto.request.ArticleCreateRequest
import big.board.entity.Article
import big.board.snowflake.Snowflake
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest()
class DataInitializer {
    @PersistenceContext()
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate

    val snowflake = Snowflake()
    val latch = CountDownLatch(EXECUTE_COUNT)

    companion object {
        const val BULK_INSERT_SIZE = 2000
        const val EXECUTE_COUNT = 6000
    }

    @Test
    @Throws(InterruptedException::class)
    fun initialize() {
        val executorService = Executors.newFixedThreadPool(10)
        for (i in 0 until EXECUTE_COUNT) {
            executorService.submit {
                insert()
                latch.countDown()
                println("latch.getCount() = ${latch.count}")
            }
        }
        latch.await()
        executorService.shutdown()
    }

    fun insert() {
        transactionTemplate.executeWithoutResult { status ->
            for (i in 0 until BULK_INSERT_SIZE) {
                val article = Article.of(
                    snowflake.nextId(),
                    ArticleCreateRequest.of("title$i", "content$i", 1L, 1L),
                )
                entityManager.persist(article)
            }
        }
    }

}
