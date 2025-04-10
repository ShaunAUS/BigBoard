package big.board.comment.controller

import big.board.dto.comment.reesponse.CommentResponse
import big.board.dto.comment.request.CommentCreateRequest
import big.board.comment.service.CommentService
import big.board.dto.comment.reesponse.CommentPageResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/v1/comments")
class CommentController(
    private val commentService: CommentService,
) {

    @GetMapping("/{commentId}")
    fun read(
        @PathVariable commentId: Long,
    ): CommentResponse {
        return commentService.read(commentId)
    }

    @PostMapping
    fun create(
        @RequestBody request: CommentCreateRequest,
    ): CommentResponse {
        println("request = $request")
        return commentService.create(request)
    }

    @DeleteMapping("/{commentId}")
    fun delete(
        @PathVariable commentId: Long,
    ) {
        commentService.delete(commentId)
    }

    @GetMapping()
    fun readAll(
        @RequestParam("articleId") articleId: Long,
        @RequestParam("page") page: Long,
        @RequestParam("pageSize") pageSize: Long,
    ): CommentPageResponse {
        return commentService.readAll(articleId, page, pageSize)
    }

    // 무한스크롤
    @GetMapping("/infinite-scroll")
    fun readAllInfinite(
        @RequestParam("articleId") articleId: Long,
        @RequestParam(value = "lastParentCommentId", required = false) lastParentCommentId: Long?,
        @RequestParam(value = "lastCommentId", required = false) lastCommentId: Long?,
        @RequestParam("pageSize") pageSize: Long,
    ): List<CommentResponse> {
        return commentService.readAllInfinite(articleId, lastParentCommentId, lastCommentId, pageSize)
    }


}
