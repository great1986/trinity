package com.example.guestbook.repository;

import com.example.guestbook.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 게시글 상세 불러오기 1 - 댓글 없음
    @Query("select b, w from Board b left join b.writer w where b.bno = :bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    // 게시글 목록 불러오기 (게시글 + 작성자 + 댓글 수)
    @Query(value = "SELECT b, w, count(r) FROM Board b LEFT JOIN  b.writer w LEFT JOIN Reply r ON r.board = b",
    countQuery = "SELECT count(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    // 게시글 상세2 - 댓글 수 포함 
    @Query(value = "SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w LEFT OUTER JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);


}
