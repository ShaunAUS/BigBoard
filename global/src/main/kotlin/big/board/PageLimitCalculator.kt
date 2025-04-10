package big.board

//페이지 그룹에 따라 보여줄 수 있는 아이템의 끝번호를 계산
//페이지당 몇 개씩 보여주냐, 한 번에 페이지 버튼 몇 개 보여줄 거냐를 기준으로 계산
class PageLimitCalculator {
    companion object {
        fun calculatePageLimit(page: Long, pageSize: Long, movablePageCount: Long): Long {
            return ((((page - 1) / movablePageCount) + 1) * pageSize * movablePageCount + 1)
        }
    }
}
