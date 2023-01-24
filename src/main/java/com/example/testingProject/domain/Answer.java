package com.example.testingProject.domain;

import com.example.testingProject.domain.base.PersistentObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Answer extends PersistentObject {

    //TODO: name text
    private String answer;

    private Question question;

    //TODO: name createdBy
    private User createdByUser;

    private Boolean isRight;
}
