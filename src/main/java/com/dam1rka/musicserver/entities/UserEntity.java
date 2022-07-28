package com.dam1rka.musicserver.entities;
import lombok.Data;

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

    private Date created;
    private Date updated;

    private String gender;

    public UserEntity() {

    }
}