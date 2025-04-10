package big.board.data

import big.board.dto.comment.request.CommentCreateRequest
import big.board.entity.Comment
import big.board.snowflake.Snowflake
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class DataInitializer {

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate

    private val snowflake = Snowflake()

    companion object {
        const val BULK_INSERT_SIZE = 2000
        const val EXECUTE_COUNT = 6000
    }

    private val latch = CountDownLatch(EXECUTE_COUNT)

    @Test
    fun initialize() {
        val executorService = Executors.newFixedThreadPool(10)

        repeat(EXECUTE_COUNT) {
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
        transactionTemplate.executeWithoutResult {
            var prev: Comment? = null

            repeat(BULK_INSERT_SIZE) { i ->
                val commentId = snowflake.nextId()
                val request = CommentCreateRequest(
                    content = "content $i",
                    parentCommentId = if (i % 2 == 0) null else prev?.commentId,
                    articleId = 1L,
                    writerId = 1L
                )
                val comment = Comment.of(commentId, prev, request)
                prev = comment
                entityManager.persist(comment)
            }
        }
    }
}
