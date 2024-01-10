package com.example.guestbook.repository;


import com.querydsl.core.types.Predicate;
import com.querydsl.core.BooleanBuilder; // 1
import com.querydsl.core.types.dsl.BooleanExpression; // 2
import org.junit.jupiter.api.Test; // 3
import org.springframework.beans.factory.annotation.Autowired; // 4
import org.springframework.boot.test.context.SpringBootTest; // 5
import org.springframework.data.domain.Page; //6
import org.springframework.data.domain.PageRequest; // 7
import org.springframework.data.domain.Pageable; // 8
import org.springframework.data.domain.Sort; // 9
import com.example.guestbook.entity.Guestbook; // 10
import com.example.guestbook.entity.QGuestbook; // 11

import java.util.stream.IntStream; // 12

@SpringBootTest
public class GuestbookRepositoryTest {

    @Autowired
    private GuestbookRepository guestbookRepository;

//    @Test
//    public void insertDummies(){
//        IntStream.rangeClosed(1, 300).forEach( i -> {
//            Guestbook guestbook = Guestbook.builder()
//                    .title("Title..." + i)
//                    .content("Content..." + i)
//                    .writer("user" + (i % 10))
//                    .build();
//            System.out.println(guestbookRepository.save(guestbook));
//        }); // end of IntStream
//    } // end of insertDummies()

//    @Test
//    public void testQuery1(){
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
//
//        // 1. Q도메인 클래스 얻어온다.
//        QGuestbook qGuestbook = QGuestbook.guestbook;
//
//        String keyword = "1";
//
//        // 2. BooleanBuilder 선언: where문에 들어가는 조건 삽입
//        BooleanBuilder builder = new BooleanBuilder();
//
//        // 3. 제목(title)에 '1'이 들어가는 엔티티 검색
//        BooleanExpression expression = qGuestbook.title.contains(keyword);
//
//        // 4. 검색조건: and 나 or와 결합시킨다
//        builder.and(expression);
//
//        //5. GuestbookRepository에 추가된 QuerydslPredicateExecutor 인터페이스의 findAll() 사용
//        // 자동으로 QuerydslPredicateExecutor<Guestbook>가 추가되어지 않아서 내가 임의로 추가함.
//        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
//
//        result.stream().forEach(guestbook -> {
//            System.out.println(guestbook);
//        });
//
//    } // end of testQuery1()

    // 다중항목 검색: 제목(title) 혹은 내용(content)에 특정 키워드가 있고, gno가 0보다 크다
    @Test
    public void testQuery2(){
        // 1. Pageable 설정
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        // 2. QGuestbook 초기화
        QGuestbook qGuestbook = QGuestbook.guestbook;

        // 3. keyword 값 설정
        String keyword = "1";

        // 4. BooleanBuilder 생성
        BooleanBuilder builder = new BooleanBuilder();

        // 5-1. BooleanExpression 설정 - title
        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        // 5-2. BooleanExpression 설정 - content
        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        // 6. ⑤의 expression을 or로 결합
        BooleanExpression exAll = exTitle.or(exContent);

        // 7. builder에 expression 추가
        builder.and(exAll);

        // 8. builder에 expression 추가
        builder.and(qGuestbook.gno.gt(0L));

        // 9. findAll로 builder 실행
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        // 10. 결과 출력
        result.stream().forEach(System.out::println);

    } // end of testQuery2()

} // end of public class GuestbookRepositoryTest
