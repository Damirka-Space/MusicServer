package com.dam1rka.musicserver.entities;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;

    private String password;
    private String firstname;
    private String lastname;
    @CreatedDate
    private Date created;
    @LastModifiedDate
    private Date updated;

    private String gender;

    public UserEntity() {

    }
}