package big.board.controller

import big.board.dto.request.ArticleCreateRequest
import big.board.dto.request.ArticleUpdateRequest
import big.board.dto.response.ArticlePageResponse
import big.board.dto.response.ArticleResponse
import big.board.service.ArticleService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleController(
    private val articleService: ArticleService,
) {

    @GetMapping("/v1/articles/{articleId}")
    fun getArticle(@PathVariable articleId: Long): ArticleResponse {
        return articleService.read(articleId)
    }

    @GetMapping("/v1/articles")
    fun readAll(
        @RequestParam("boardId") boardId: Long,
        @RequestParam("page") page: Long,
        @RequestParam("pageSize") pageSize: Long,
    ): ArticlePageResponse {
        return articleService.readAll(boardId, page, pageSize)
    }

    // 무한스크룰
    @GetMapping("/v1/articles/infinite-scroll")
    fun readAllInfiniteScroll(
        @RequestParam("boardId") boardId: Long,
        @RequestParam("pageSize") pageSize: Long,
        @RequestParam(value = "lastArticleId", required = false) lastArticleId: Long,
    ): List<ArticleResponse> {
        return articleService.readAllInfiniteScroll(boardId, pageSize, lastArticleId)
    }

    @PostMapping("/v1/articles")
    fun createArticle(@RequestBody request: ArticleCreateRequest): ArticleResponse {
        return articleService.create(request)
    }

    @PutMapping("/v1/articles/{articleId}")
    fun updateArticle(@PathVariable articleId: Long, @RequestBody request: ArticleUpdateRequest): ArticleResponse {
        return articleService.update(articleId, request)
    }
    @DeleteMapping("/v1/articles/{articleId}")
    fun deleteArticle(@PathVariable articleId: Long) {
        articleService.delete(articleId)
    }
}
