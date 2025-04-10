package big.board.api

import big.board.dto.comment.request.CommentCreateRequest
import big.board.dto.comment.reesponse.CommentResponse
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestClient

class CommentApiTest {

    private val restClient = RestClient.create("http://localhost:9001")

    @Test
    fun create() {

        val response1 = createComment(CommentCreateRequest("my comment1",null, 1L, 1L))
        val response2 = createComment(CommentCreateRequest("my comment2",response1.commentId, 1L, 1L))
        val response3 = createComment(CommentCreateRequest("my comment3",response1.commentId, 1L, 1L))

        println("commentId=${response1.commentId}")
        println("\tcommentId=${response2.commentId}")
        println("\tcommentId=${response3.commentId}")
    }

    private fun createComment(request: CommentCreateRequest): CommentResponse {
        return restClient.post()
            .uri("/v1/comments")
            .body(request)
            .retrieve()
            .body(CommentResponse::class.java)!!
    }

    @Test
    fun read() {
        val response = restClient.get()
            .uri("/v1/comments/{commentId}", 123694721668214784L)
            .retrieve()
            .body(CommentResponse::class.java)

        println("response = $response")
    }

    @Test
    fun delete() {
        val commentIds = listOf(
            123694721668214784L,
            123694721986981888L,
            123694722045702144L,
        )

        commentIds.forEach { commentId ->
            restClient.delete()
                .uri("/v1/comments/{commentId}", commentId)
                .retrieve()

            println("Deleted commentId=$commentId")
        }
    }
}
