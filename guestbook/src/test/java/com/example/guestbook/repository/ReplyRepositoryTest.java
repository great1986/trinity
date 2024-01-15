package com.example.guestbook.repository;


import com.example.guestbook.entity.Board;
import com.example.guestbook.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertReply(){
        IntStream.rangeClosed(1, 300).forEach(i->{
            long bno = (long)(Math.random() * 100) + 1;
            Board board = Board.builder().bno(bno).build();
            Reply reply = Reply.builder()
                    .text("Reply..." + i)
                    .board(board)
                    .replyer("guest") // 가입한 사용자 아니어도 작성할 수 있는 상태 가정
                    .build();
            replyRepository.save(reply);
        });
    } // end of insertReply()
}
