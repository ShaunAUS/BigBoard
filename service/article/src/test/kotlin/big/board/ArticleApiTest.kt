package big.board

import big.board.dto.response.ArticleResponse
import org.junit.jupiter.api.Test
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
                boardId = 1L
            )
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


    data class ArticleCreateRequest(
        val title: String,
        val content: String,
        val writerId: Long,
        val boardId: Long
    )

    data class ArticleUpdateRequest(
        val title: String,
        val content: String
    )
}
