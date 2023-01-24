package com.example.testingProject.service.impl;

import com.example.testingProject.domain.User;
import com.example.testingProject.dto.response.MyStatisticDto;
import com.example.testingProject.dto.response.StatisticDto;
import com.example.testingProject.repository.UserAnswerRepository;
import com.example.testingProject.repository.UserRepository;
import com.example.testingProject.security.UserDetailsWithId;
import com.example.testingProject.service.StatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final UserRepository userRepository;
    private final UserAnswerRepository userAnswerRepository;

    @Override
    public MyStatisticDto getMyStatistic(User user) {
        var myCountRight = userAnswerRepository.getCountRightAnswerByUserId(user.getId());
        var countUserStartTesting = userAnswerRepository.getCountUsersStartTesting();

        String passPercentage = (double)myCountRight / 5 * 100 + "%";

        if(countUserStartTesting == 0 || countUserStartTesting == 1)
            return new MyStatisticDto(passPercentage, "0%", "0%");

        String betterMe = (double)userAnswerRepository.getCountUsersBetterMe(user.getId(), myCountRight) /
                (countUserStartTesting - 1) * 100 + "%";
        String worseMe = (double)userAnswerRepository.getCountUsersWorseMe(user.getId(), myCountRight) /
                (countUserStartTesting - 1) * 100 + "%";
        return new MyStatisticDto(passPercentage, betterMe, worseMe);
    }

    @Override
    public StatisticDto getAllStatistic() {
        Long allUsers = userRepository.count();
        Long startTesting = userAnswerRepository.getCountUsersStartTesting();
        Long passTesting = userAnswerRepository.getCountUsersPassTesting();
        Long rightPassTesting = userAnswerRepository.getCountUsersRightPassTesting();

        return new StatisticDto(allUsers,startTesting,passTesting,rightPassTesting);
    }
}
