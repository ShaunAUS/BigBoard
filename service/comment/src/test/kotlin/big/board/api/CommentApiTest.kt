package big.board.api

import big.board.dto.comment.reesponse.CommentPageResponse
import big.board.dto.comment.request.CommentCreateRequest
import big.board.dto.comment.reesponse.CommentResponse
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
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
    fun readAll() {
        val response = restClient.get()
            .uri("/v1/comments?articleId=1&page=1&pageSize=10")
            .retrieve()
            .body(CommentPageResponse::class.java)!!

        val comments = response.comments
        println("comments.size = ${comments.size}")
        comments.forEach { comment ->
            if (isTwoDepth(comment)) {
                print("\t")
            }
            println("comment.commentId = ${comment.commentId}")
        }
    }

    @Test
    fun readAllInfiniteScroll() {
        // 첫 번째 페이지 요청
        val responses1: List<CommentResponse> = restClient.get()
            .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5")
            .retrieve()
            //ParameterizedTypeReference : 런타임에 제네릭 타입을 명확하게 참조할 수 있게 도와줌
            .body(object : ParameterizedTypeReference<List<CommentResponse>>() {})!!

        println("firstPage")
        responses1.forEach { comment ->
            if (isTwoDepth(comment)) {
                print("\t")
            }
            println("comment.commentId = ${comment.commentId}")
        }

        // 마지막 댓글 정보를 이용해 두 번째 페이지 요청
        val lastParentCommentId = responses1.last().parentCommentId
        val lastCommentId = responses1.last().commentId

        val responses2: List<CommentResponse> = restClient.get()
            .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5&lastParentCommentId=$lastParentCommentId&lastCommentId=$lastCommentId")
            .retrieve()
            .body(object : ParameterizedTypeReference<List<CommentResponse>>() {})!!

        println("secondPage")
        responses2.forEach { comment ->
            if (isTwoDepth(comment)) {
                print("\t")
            }
            println("comment.commentId = ${comment.commentId}")
        }
    }

    //commentId 와 parentCommentId가 다르면 2뎁스
    private fun isTwoDepth(comment: CommentResponse) = comment.commentId != comment.parentCommentId


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
