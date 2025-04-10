package big.board.entity

import big.board.dto.comment.request.CommentCreateRequest
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Comment(
    content: String,
    parentCommentId: Long,
    articleId: Long,
    writerId: Long,
    isDeleted: Boolean,
    createAt: LocalDateTime,

    @Id
    val commentId: Long,
) {
    var content: String = content
        protected set
    var parentCommentId: Long = parentCommentId
        protected set
    var articleId: Long = articleId
        protected set
    var writerId: Long = writerId
        protected set
    var isDeleted: Boolean = isDeleted
        protected set
    var createAt: LocalDateTime = createAt
        protected set

    companion object {
        fun of(commentId: Long, parentComment: Comment?, commentCreateRequest: CommentCreateRequest): Comment {
            return Comment(
                commentId = commentId,
                content = commentCreateRequest.content,
                parentCommentId = parentComment?.commentId ?: commentId, // parent가 없으면 자기자신 commentId사용
                articleId = commentCreateRequest.articleId,
                writerId = commentCreateRequest.writerId,
                isDeleted = false,
                createAt = LocalDateTime.now(),
            )
        }
    }

    //상위 댓글이 없는경우 == 뎁스가 1인경우
    fun isRoot(): Boolean {
        return parentCommentId == commentId
    }

    fun delete() {
        this.isDeleted = true
    }

}
