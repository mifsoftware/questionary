package com.example.testingProject.domain;

import com.example.testingProject.domain.base.PersistentObject;
import com.example.testingProject.domain.types.QuestionType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Question extends PersistentObject {

    //TODO: поменять названия text
    private String question;

    private QuestionType questionType;

    private User createdByUser;
}
