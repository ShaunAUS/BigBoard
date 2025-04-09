package big.board

import big.board.dto.response.ArticlePageResponse
import big.board.dto.response.ArticleResponse
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.client.RestClient

class ArticleApiTest {
    private val restClient = RestClient.create("http://localhost:9000")

    @Test
    fun createTest() {
        val response = create(
            ArticleCreateRequest(
                title = "hi",
                content = "my content",
                writerId = 1L,
                boardId = 1L,
            ),
        )
        println("response = $response")
    }

    private fun create(request: ArticleCreateRequest): ArticleResponse? {
        return restClient.post()
            .uri("/v1/articles")
            .body(request)
            .retrieve()
            .body(ArticleResponse::class.java)
    }

    @Test
    fun readTest() {
        val response = read(121530268440289280L)
        println("response = $response")
    }

    private fun read(articleId: Long): ArticleResponse? {
        return restClient.get()
            .uri("/v1/articles/{articleId}", articleId)
            .retrieve()
            .body(ArticleResponse::class.java)
    }

    @Test
    fun updateTest() {
        update(121530268440289280L)
        val response = read(121530268440289280L)
        println("response = $response")
    }

    private fun update(articleId: Long) {
        restClient.put()
            .uri("/v1/articles/{articleId}", articleId)
            .body(ArticleUpdateRequest("hi 2", "my content 22"))
            .retrieve()
    }

    @Test
    fun deleteTest() {
        restClient.delete()
            .uri("/v1/articles/{articleId}", 121530268440289280L)
            .retrieve()
    }

    @Test
    fun readAllTest() {
        val response = restClient.get()
            .uri("/v1/articles?boardId=1&page=1&pageSize=30")
            .retrieve()
            .body(ArticlePageResponse::class.java)
    }

    @Test
    fun readAllInfiniteScrollTest() {
        val articles1 = restClient.get()
            .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5")
            .retrieve()
            .body(object : ParameterizedTypeReference<List<ArticleResponse>>() {})

        println("firstPage")
        for (articleResponse in articles1!!) {
            println("articleResponse.getArticleId() = " + articleResponse.articleId)
        }

        val lastArticleId = articles1.last().articleId
        val articles2 = restClient.get()
            .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5&lastArticleId=%s".format(lastArticleId))
            .retrieve()
            .body(object : ParameterizedTypeReference<List<ArticleResponse>>() {})

        println("secondPage")
        for (articleResponse in articles2!!) {
            println("articleResponse.getArticleId() = " + articleResponse.articleId)
        }
    }

    data class ArticleCreateRequest(
        val title: String,
        val content: String,
        val writerId: Long,
        val boardId: Long,
    )

    data class ArticleUpdateRequest(
        val title: String,
        val content: String,
    )
}
