package com.dam1rka.musicserver.dtos;

import lombok.Data;

@Data
public class UserRegistrationDto {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String firstname;
    private String lastname;
    private String birthday;
    private String Gender;

}
