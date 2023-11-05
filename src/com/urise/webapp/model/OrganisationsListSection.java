package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class OrganisationsListSection extends Section<List<Organisation>>{
    private static final long serialVersionUID = 1L;
    private List<Organisation> organisations;

    public OrganisationsListSection(List<Organisation> organisations) {
        Objects.requireNonNull(organisations, "organisations must not be null");
        this.organisations = organisations;
    }
    @Override
    public List<Organisation> getContent() {
        return organisations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganisationsListSection that = (OrganisationsListSection) o;
        return Objects.equals(organisations, that.organisations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organisations);
    }
}
