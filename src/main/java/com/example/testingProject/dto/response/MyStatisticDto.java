package com.example.testingProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyStatisticDto {
    private String passPercentage;
    private String betterMe;
    private String worseMe;
}
