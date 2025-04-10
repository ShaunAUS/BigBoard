package big.board.dto.comment.reesponse

import big.board.entity.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val commentId: Long,
    val content: String,
    val parentCommentId: Long,
    val articleId: Long,
    val writerId: Long,
    val isDeleted: Boolean,
    val createAt: LocalDateTime,
) {
    companion object {
        fun of(comment: Comment): CommentResponse {
            return CommentResponse(
                commentId = comment.commentId!!,
                content = comment.content,
                parentCommentId = comment.parentCommentId,
                articleId = comment.articleId,
                writerId = comment.writerId,
                isDeleted = comment.isDeleted,
                createAt = comment.createAt,
            )
        }
    }
}
