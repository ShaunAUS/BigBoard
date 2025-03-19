package big.board.service

import big.board.snowflake.Snowflake
import big.board.dto.request.ArticleCreateRequest
import big.board.dto.request.ArticleUpdateRequest
import big.board.dto.response.ArticleResponse
import big.board.entity.Article
import big.board.repository.ArticleRepository
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


}
