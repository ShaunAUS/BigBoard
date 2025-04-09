package big.board.dto.response

class ArticlePageResponse(
    private val articles: List<ArticleResponse>,
    private val articleCount: Long,
) {
    companion object {
        fun of(articles: List<ArticleResponse>, articleCount: Long): ArticlePageResponse {
            return ArticlePageResponse(articles, articleCount)
        }
    }
}
