package com.example.testingProject.util;

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

@UtilityClass
public class EnumTools {
    @Nullable
    public static <T extends Enum<T>> T valueOf(@Nullable String name, Class<T> clazz) {
        if (name == null) {
            return null;
        }

        return Enum.valueOf(clazz, name);
    }
}
