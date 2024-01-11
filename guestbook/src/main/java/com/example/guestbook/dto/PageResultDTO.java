package com.example.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {
    /******
     * JPA를 사용하는 Repository에서 넘겨준 페이지 처리 결과: Page<Entity>
     * 1. Page<Entity>를 DTO객체로 변환
     * 2. 화면 출력에 필요한 페이지 정보 구성
     *  */

    private List<DTO> dtoList;

    /**
     * =========== 페이지 처리 ===========
     * tempEnd = (int)(Math.veil(페이지번호/10.0)) * 10;
     * 총 페이지 수: totalPage = getTotalpages()
     * 현재 페이지 번호: page
     * 페이지 당 출력될 목록 개수: size (10개)
     * 시작페이지 번호: start = temp - 9;
     * 끝페이지 번호: end = totalPage > tempEnd ? tempEnd: totalPage;
     * 이전페이지(boolean): next = totalPage > tempEnd
     * 다음페이지(boolean): prev = start > 1
     * 페이지 번호 목록(List<Integer>) pageList
     * **/
    private int             totalPage;
    private int             page;
    private int             size;
    private int             start;
    private int             end;
    private boolean         next;
    private boolean         prev;
    private List<Integer>   pageList;

     /**
      * Function<EN, DTO>: 엔티티 객체들을 DTO로 변환해주는 기능
      * */
     public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
         dtoList = result.stream().map(fn).collect(Collectors.toList());
         totalPage = result.getTotalPages(); // getTotalPages()는 Page의 내장함수!
         makePageList(result.getPageable());
     } // end of PageResultDTO

    private void makePageList(Pageable pageable){
         // pageable의 내장 함수 사용
         // getPageNumber는 0부터 시작하므로 +1 해준다
         this.page = pageable.getPageNumber() + 1;
         this.size = pageable.getPageSize();

         // temp and page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;
        // end = totalPage > tempEnd ? tempEnd : totalPage; 를
        // intelliJ가 아래와 같이 바꿔줌.
        end = Math.min(totalPage, tempEnd);

        prev = start > 1;
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    } // end of makePageList()

} // end of class PageResultDTO
