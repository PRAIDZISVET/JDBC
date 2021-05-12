package by.itacademy.jdbc.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Language {
    EN("en"), RU("ru");

    private String name;

    Language(String name) {
        this.name = name;
    }

    public static Language getByName(String name) {
        return Arrays.stream(values())
                .filter(it -> it.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
