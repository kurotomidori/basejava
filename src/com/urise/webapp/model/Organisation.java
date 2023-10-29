package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Organisation {
    private String organisationName;
    private String organisationUrl;
    private String position;
    private String content;
    private LocalDate beginDate;
    private LocalDate endDate;

    public Organisation(String organisationName, String organisationUrl, String position, String content, LocalDate beginDate, LocalDate endDate) {
        this.organisationName = organisationName;
        this.organisationUrl = organisationUrl;
        this.position = position;
        this.content = content;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public String getOrganisationUrl() {
        return organisationUrl;
    }

    public String getPosition() {
        return position;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organisation that = (Organisation) o;
        return Objects.equals(organisationName, that.organisationName) && Objects.equals(organisationUrl, that.organisationUrl) && Objects.equals(position, that.position) && Objects.equals(content, that.content) && Objects.equals(beginDate, that.beginDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organisationName, organisationUrl, position, content, beginDate, endDate);
    }

    @Override
    public String toString() {
        return "\n" + organisationName + "\n" + organisationUrl + "\n" + position + "\n" + content + beginDate + " - " + endDate;
    }
}
