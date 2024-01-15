package com.example.guestbook.repository;

import com.example.guestbook.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1, 100).forEach(i->{
            Member member = Member.builder()
                    .email("user"+i+"abc.com")
                    .name("USER"+i)
                    .password("1234")
                    .build();
            memberRepository.save(member);
        });

    } // end of insertMembers()
}
