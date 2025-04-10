package big.board.comment.service

import big.board.dto.comment.reesponse.CommentPageResponse
import big.board.dto.comment.reesponse.CommentResponse
import big.board.dto.comment.request.CommentCreateRequest
import big.board.entity.Comment
import big.board.repository.CommentRepository
import big.board.snowflake.Snowflake
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val snowflake: Snowflake,
    private val commentRepository: CommentRepository
) {

    @Transactional
    fun create(request: CommentCreateRequest): CommentResponse {
        val parent = findParent(request)
        val savedComment = commentRepository.save(
            Comment.of(snowflake.nextId(), parent, request),
        )
        return CommentResponse.of(savedComment)
    }

    @Transactional
    fun read(commentId: Long): CommentResponse {
        return CommentResponse.of(commentRepository.findById(commentId).orElseThrow())

    }

    fun readAll(articleId: Long, page: Long, pageSize: Long): CommentPageResponse {
        val commentResponses = commentRepository.findAll(articleId, (page - 1) * pageSize, pageSize).stream()
            .map { CommentResponse.of(it) }
            .toList()
        val pageCount = commentRepository.count(articleId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L))
        return CommentPageResponse.of(comments = commentResponses, commentCount = pageCount)
    }

    //무한스크룰
    fun readAllInfinite(articleId: Long, lastParentCommentId: Long?, lastCommentId: Long?, limit: Long): List<CommentResponse> {
        val comments = if (lastParentCommentId == null || lastCommentId == null) {
            commentRepository.findAllInfiniteScroll(articleId, limit)
        } else {
            commentRepository.findAllInfiniteScroll(articleId, lastParentCommentId, lastCommentId, limit)
        }
        return comments.map { CommentResponse.of(it) }
    }


    //하위 댓글이 있으면 완전삭제 x
    //하위 댓글이 없으면 완전삭제 o
    @Transactional
    fun delete(commentId: Long) {
        val comment = findNotDeletedComment(commentId)
        comment?.let {

            if (hasChildrentComment(comment)) {
                comment.delete()
            } else {
                deleteComment(comment)
            }
        }
    }

    //진짜삭제
    private fun deleteComment(comment: Comment) {
        commentRepository.delete(comment)

        if (hasParentComment(comment)) {
            commentRepository.findById(comment.commentId)
                .filter { isNotDeleted(it) }
                .filter { hasNotChildrentComment(it) }
                .ifPresent { commentRepository.delete(it) }
        }
    }

    private fun hasParentComment(comment: Comment) = !comment.isRoot()

    private fun hasNotChildrentComment(comment: Comment?): Boolean {
        return !hasNotChildrentComment(comment!!)
    }

    //자기자신 + 부모 코멘트 2개 이기 때문에 2로 조회
    private fun hasChildrentComment(comment: Comment): Boolean {
        return commentRepository.countBy(comment.articleId, comment.commentId!!, 2L) == 2L
    }


    private fun findNotDeletedComment(commentId: Long): Comment? {
        return commentRepository.findById(commentId)
            .filter { isNotDeleted(it) }
            .orElse(null)
    }


    private fun findParent(request: CommentCreateRequest): Comment? {
        val parentCommentId = request.parentCommentId ?: return null
        return commentRepository.findById(parentCommentId)
            .filter { isNotDeleted(it) }
            .filter { it.isRoot() }
            .orElseThrow()
    }

    private fun isNotDeleted(it: Comment) = !it.isDeleted


}
