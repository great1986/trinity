package com.example.guestbook.service;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.dto.PageResultDTO;
import com.example.guestbook.entity.Guestbook;
import com.example.guestbook.entity.QGuestbook;
import com.example.guestbook.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService{
    private final GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO dto) {
        log.info("DTO--------------------------------");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);
        log.info(entity);
        repository.save(entity);
        return entity.getGno();
    }
    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO){

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        // 검색 조건 있을 경우까지 감안하여 처리
        BooleanBuilder booleanBuilder = getSearch(requestDTO);
        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable);
//         Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity)); // ←이 코드를
        // IntelliJ가 아래와 같이 변경
        Function<Guestbook, GuestbookDTO> fn = (this::entityToDto);
        return new PageResultDTO<>(result, fn);
    } // end of getList(PageRequestDTO requestDTO)

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = repository.findById(gno);
        // result.isPresent()? entityToDto(result.get()): null;
        // 위 코드를 인텔리제이가 아래처럼 바꿔줌
        return result.map(this::entityToDto).orElse(null);
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        Optional<Guestbook> result = repository.findById(dto.getGno());
        if(result.isPresent()){
            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }

    private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO){
        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;

        // 기본 검색 조건조차 입력되지 않고 검색 버튼이 눌려졌을 때
        // 검색을 실행할 조건 설정(그냥 모든 게시글 불러오는 것)
        BooleanExpression expression = qGuestbook.gno.gt(0L);
        booleanBuilder.and(expression);
        // type.trim().length() == 0
        if(type == null || type.trim().isEmpty()){
            return booleanBuilder;
        }

        // 검색 조건 type별 분류하여 검색어 입력
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        // 검색조건에 제목(title)이 있을 경우
        if(type.contains("t")){
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }

        // 검색조건에 내용(content)이 있을 경우
        if(type.contains("c")){
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }

        // 검색조건에 작성자(writer)가 있을 경우
        if(type.contains("w")){
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    } // end of getSearch()
} // end of class GuestbookServiceImpl
