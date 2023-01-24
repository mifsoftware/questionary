package com.example.testingProject.service.impl;

import com.example.testingProject.domain.User;
import com.example.testingProject.service.JWTMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

public class UserToTokenDetailsMapper {

    public JWTMapper.TokenDetails toTokenDetails(User user){
        return new JWTMapper.TokenDetails(user.getId(), user.getLogin());
    }
}
