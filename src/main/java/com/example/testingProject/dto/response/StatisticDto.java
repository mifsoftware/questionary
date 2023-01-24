package com.example.testingProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDto {
    private Long allUsers;
    private Long startTesting;
    private Long passTesting;
    private Long rightPassTesting;
}
