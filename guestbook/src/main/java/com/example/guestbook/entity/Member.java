package com.example.guestbook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Table(name="tbl_member")
public class Member extends BaseEntity{
    @Id
    private String email;

    private String password;

    private String name;
} // end of class Member
