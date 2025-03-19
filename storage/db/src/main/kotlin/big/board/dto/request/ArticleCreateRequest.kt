package big.board.dto.request

data class ArticleCreateRequest(
    val title: String,
    val content: String,
    val writerId: Long,
    val boardId: Long,
){
    companion object {
        fun of(title: String, content: String, writerId: Long, boardId: Long): ArticleCreateRequest {
            return ArticleCreateRequest(
                title = title,
                content = content,
                writerId = writerId,
                boardId = boardId,
            )
        }
    }
}
