package big.board.dto.article.response

import big.board.entity.Article
import java.time.LocalDateTime

data class ArticleResponse(
    val articleId: Long,
    val title: String,
    val content: String,
    val writerId: Long,
    val boardId: Long,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
) {
    companion object {
        fun from(article: Article): ArticleResponse {
            return ArticleResponse(
                articleId = article.articleId,
                title = article.title,
                content = article.content,
                writerId = article.writerId,
                boardId = article.boardId,
                createdAt = article.createdAt,
                modifiedAt = article.modifiedAt,
            )
        }
    }
}




