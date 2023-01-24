package com.example.testingProject.domain;

import com.example.testingProject.domain.base.PersistentObject;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswer extends PersistentObject {

    private Question question;

    private User user;

    private String answerFreeChoice;

    private Answer answerFourChoice;

    private boolean isRight;
}
