package big.board.comment.service

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
