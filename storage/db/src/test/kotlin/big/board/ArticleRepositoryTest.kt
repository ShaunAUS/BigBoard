package big.board

import big.board.entity.Article
import big.board.repository.ArticleRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.hibernate.query.sqm.tree.SqmNode.log
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

private val logger = KotlinLogging.logger {}

@SpringBootTest
class ArticleRepositoryTest(
    @Autowired private val articleRepository: ArticleRepository,
) {

    @Test
    fun findAllTest() {
        val articles = articleRepository.findAll(1L, 1499970L, 30L)
        logger.info { "articles.size  = ${articles.size}" }
        for (article in articles) {
            logger.info { "article = $article" }
        }
    }


    @Test
    fun countTest() {
        val articles = articleRepository.count(1L, 10000L)
        logger.info { "article count = $articles" }
    }

    @Test
    fun findInfiniteScrollTest() {
        val articles = articleRepository.findAllInfiniteScroll(1L, 30L)
        for (article in articles) {
            logger.info { "articleId = ${article.articleId}" }
        }

        val lastArticleId = articles.last().articleId
        val articles2 = articleRepository.findAllInfiniteScroll(1L, 30L, lastArticleId!!)
        for (article in articles2) {
            logger.info { "articleId = ${article.articleId}" }
        }
    }


}
