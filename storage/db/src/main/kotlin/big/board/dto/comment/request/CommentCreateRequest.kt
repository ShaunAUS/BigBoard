package big.board.dto.comment.request

data class CommentCreateRequest(
    val content: String,
    val parentCommentId: Long?,
    val articleId: Long,
    val writerId: Long,
)
