package big.board.comment.controller

import big.board.dto.comment.reesponse.CommentResponse
import big.board.dto.comment.request.CommentCreateRequest
import big.board.comment.service.CommentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
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
}
