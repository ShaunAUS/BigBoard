package big.board.service

import big.board.dto.article.request.ArticleCreateRequest
import big.board.dto.article.request.ArticleUpdateRequest
import big.board.dto.article.response.ArticlePageResponse
import big.board.dto.article.response.ArticleResponse
import big.board.entity.Article
import big.board.repository.ArticleRepository
import big.board.snowflake.Snowflake
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val snowflake: Snowflake,
) {

    @Transactional
    fun create(request: ArticleCreateRequest): ArticleResponse {
        val savedArticle = articleRepository.save(Article.of(snowflake.nextId(), request))
        return ArticleResponse.from(savedArticle)
    }

    @Transactional
    fun update(articleId: Long, request: ArticleUpdateRequest): ArticleResponse {
        val article = articleRepository.findById(articleId).orElseThrow()
        article.update(request)
        return ArticleResponse.from(article)
    }

    fun read(articleId: Long): ArticleResponse {
        val article = articleRepository.findById(articleId).orElseThrow()
        return ArticleResponse.from(article)
    }

    @Transactional
    fun delete(articleId: Long) {
        articleRepository.deleteById(articleId)
    }

    fun readAll(boardId: Long, page: Long, pageSize: Long): ArticlePageResponse {
        return ArticlePageResponse.of(
            articleRepository.findAll(boardId, (page - 1), pageSize).map { ArticleResponse.from(it) },
            articleRepository.count(boardId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L)),
        )
    }

    // 무한스크룰
    fun readAllInfiniteScroll(boardId: Long, pageSize: Long, lastArticleId: Long?): List<ArticleResponse> {
        val articles = if (lastArticleId == null) {
            articleRepository.findAllInfiniteScroll(boardId, pageSize)
        } else {
            articleRepository.findAllInfiniteScroll(boardId, pageSize, lastArticleId)
        }

        return articles.map { ArticleResponse.from(it) }
    }
}
