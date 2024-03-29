package com.example.guestbook.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "writer")
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;
    private String content;

    /*********
    * ManyToOne 관계를 지정해주어야
    * Member형을 사용할 수 있다.
    * ********/
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    public void changeTitle(String title){
        this.title = title;
    } // end of changeTitle


} // end of class Board
