package big.board.entity

import big.board.dto.request.ArticleCreateRequest
import big.board.dto.request.ArticleUpdateRequest
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Article(
    @Id
    val articleId: Long? = null,
    title: String,
    content: String,
    boardId: Long, // shard key
    writerId: Long,
    createdAt: LocalDateTime,
    modifiedAt: LocalDateTime,
) {

    var boardId: Long = boardId
        protected set
    var writerId: Long = writerId
        protected set
    var createdAt: LocalDateTime = createdAt
        protected set
    var title: String = title
        protected set
    var content: String = content
        protected set
    var modifiedAt: LocalDateTime = modifiedAt
        protected set

    companion object {
        fun of(articleId: Long, request: ArticleCreateRequest): Article {
            return Article(
                articleId = articleId,
                title = request.title,
                content = request.content,
                boardId = request.boardId,
                writerId = request.writerId,
                createdAt = LocalDateTime.now(),
                modifiedAt = LocalDateTime.now(),
            )
        }
    }

    fun update(request: ArticleUpdateRequest) {
        this.title = request.title
        this.content = request.content
        this.modifiedAt = LocalDateTime.now()
    }

}




