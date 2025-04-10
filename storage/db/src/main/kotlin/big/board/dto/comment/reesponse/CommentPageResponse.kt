package big.board.dto.comment.reesponse

data class CommentPageResponse(
    val comments: List<CommentResponse>,
    val commentCount: Long,
) {
    companion object {
        fun of(comments: List<CommentResponse>, commentCount: Long): CommentPageResponse {
            return CommentPageResponse(
                comments = comments,
                commentCount = commentCount,
            )
        }
    }
}
