package com.urise.webapp.model;

import com.urise.webapp.util.DateUtil;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Organisation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String organisationName;
    private String organisationUrl;
    private List<Position> positions;

    public Organisation(String organisationName, String organisationUrl, Position... positions) {
        Objects.requireNonNull(organisationName, "organisationName must mor be null");
        Objects.requireNonNull(positions, "position must mor be null");
        this.organisationName = organisationName;
        this.organisationUrl = organisationUrl;
        this.positions = Arrays.asList(positions);
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public String getOrganisationUrl() {
        return organisationUrl;
    }

    public List<Position> getPositions() {
        return positions;
    }

    @Override
    public String toString() {
        return "Organisation{" +
                "organisationName='" + organisationName + '\'' +
                ", organisationUrl='" + organisationUrl + '\'' +
                ", positions=" + positions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organisation that = (Organisation) o;
        return Objects.equals(organisationName, that.organisationName) && Objects.equals(organisationUrl, that.organisationUrl) && Objects.equals(positions, that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organisationName, organisationUrl, positions);
    }

    public static class Position implements Serializable {
        private String position;
        private String description;
        private LocalDate beginDate;
        private  LocalDate endDate;

        public Position(String position, String description, LocalDate beginDate, LocalDate endDate) {
            this.position = position;
            this.description = description;
            this.beginDate = beginDate;
            this.endDate = endDate;
        }

        public Position(String position, String description, int beginYear, Month beginMonth, int endYear, Month endMonth) {
            this(position, description, DateUtil.of(beginYear, beginMonth), DateUtil.of(endYear, endMonth));
        }

        public Position(String position, String description, int beginYear, Month beginMonth) {
            this(position, description, DateUtil.of(beginYear, beginMonth), DateUtil.NOW);
        }

        @Override
        public String toString() {
            return "Position{" +
                    "position='" + position + '\'' +
                    ", description='" + description + '\'' +
                    ", beginDate=" + beginDate +
                    ", endDate=" + endDate +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position1 = (Position) o;
            return Objects.equals(position, position1.position) && Objects.equals(description, position1.description) && Objects.equals(beginDate, position1.beginDate) && Objects.equals(endDate, position1.endDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, description, beginDate, endDate);
        }
    }
}
