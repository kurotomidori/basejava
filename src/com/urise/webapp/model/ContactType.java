package com.urise.webapp.model;

public enum ContactType {
    TELEPHONE("Тел.: "),
    SKYPE("Skype: "),
    EMAIL("Почта: "),
    LINKED_IN("Профиль LinkedIn"),
    GIT_HUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMEPAGE("Домашняя страница");
    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
