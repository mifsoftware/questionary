package com.example.testingProject.dto.response;

import com.example.testingProject.dto.response.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenDto {

    private UserDto user;
    private String token;
}
