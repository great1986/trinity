package com.example.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    /***
     * 페이지 번호 page
     * 페이지 내 목록 개수 size
     */
    private int page;
    private int size;
    private String type;
    private String keyword;

    /***
     * 페이지가 1부터 시작하고
     * 페이지 내 10개 목록 출력되도록 설정
     */
    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }
    /****
     * PageRequestDTO의 목적: JPA가 사용할 Pageable 객체 생성
     * page-1: JPA는 페이지 번호가 0부터 시작하므로, 1페이지의 경우 0이 되도록 page-1로 처리 (추후에 수정 필요한 상황 발생할 수 있음)
     * sort: 정렬의 형태는 한정짓지 않기 위해 parameter로 받음
     * **/
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort);
    }// end of getPageable

} // end of class PageRequestDTO
