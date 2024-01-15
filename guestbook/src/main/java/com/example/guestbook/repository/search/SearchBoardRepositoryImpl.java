package com.example.guestbook.repository.search;

import com.example.guestbook.entity.Board;
import com.example.guestbook.entity.QBoard;
import com.example.guestbook.entity.QMember;
import com.example.guestbook.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.querydsl.core.Tuple;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        // log은 @Log4j2를 달아야 사용할 수 있음
        log.info("searchPage.........................................................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        // Tuple의 빨간 줄에는 import class가 없었다..
        // import 직접 입력함(입력하니 빨간 줄 사라짐)
        /******
         * jpqlQuery.select(board, member.email, replycount())는 아래 구문과 같다
         * SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
        ******/
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null) {
            String[] typeArr = type.split("");
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for(String t: typeArr) {
                switch(t) {
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                } // end of switch
            } // end of for
            booleanBuilder.and(conditionBuilder);
        } // end of if

        tuple.where(booleanBuilder);
        tuple.groupBy(board);

        this.getQuerydsl().applyPagination(pageable, tuple);
        List<Tuple> result = tuple.fetch();
        log.info("result: "  + result);

        long count = tuple.fetchCount();
        log.info("Count: " + count);

        return new PageImpl<Object[]>(
                result.stream().map(Tuple::toArray).collect(Collectors.toList()),
                pageable,
                count
        );
    } // end of searchPage
}
