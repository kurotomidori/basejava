package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section<List<String>>{
    private List<String> content = new ArrayList<>();

    public ListSection(List<String> content) {
        Objects.requireNonNull(content, "content must not be null");
        this.content = content;
    }
    @Override
    public List<String> getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return content + "\n";
    }
}
