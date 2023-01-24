package com.example.testingProject.service;

import com.example.testingProject.domain.User;
import com.example.testingProject.dto.response.MyStatisticDto;
import com.example.testingProject.dto.response.StatisticDto;
import com.example.testingProject.security.UserDetailsWithId;

public interface StatisticService {

    MyStatisticDto getMyStatistic(User user);
    StatisticDto getAllStatistic();
}
