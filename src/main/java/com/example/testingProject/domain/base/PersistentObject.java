package com.example.testingProject.domain.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nonapi.io.github.classgraph.json.Id;

import java.util.Date;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class PersistentObject {

    @Id
    protected Long id;

    protected Date createdAt;
    protected Date updatedAt;
    protected Date deletedAt;

}

