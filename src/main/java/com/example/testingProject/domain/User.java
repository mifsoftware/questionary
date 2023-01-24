package com.example.testingProject.domain;

import com.example.testingProject.domain.base.PersistentObject;
import com.example.testingProject.domain.types.UserRoles;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User extends PersistentObject {

    private String login;
    private String password;
    private UserRoles userRoles;


}
