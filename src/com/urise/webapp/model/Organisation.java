package com.urise.webapp.model;

import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organisation implements Serializable {
    public static final Organisation EMPTY = new Organisation("","", Position.EMPTY);
    private static final long serialVersionUID = 1L;
    private String organisationName;
    private String organisationUrl;
    private List<Position> positions;

    public Organisation() {
    }

    public Organisation(String organisationName, String organisationUrl, Position... positions) {
        Objects.requireNonNull(organisationName, "organisationName must not be null");
        Objects.requireNonNull(positions, "position must not be null");
        this.organisationName = organisationName;
        this.organisationUrl = organisationUrl;
        this.positions = Arrays.asList(positions);
    }

    public Organisation(String organisationName, String organisationUrl, List<Position> positions) {
        Objects.requireNonNull(organisationName, "organisationName must not be null");
        Objects.requireNonNull(positions, "position must not be null");
        this.organisationName = organisationName;
        this.organisationUrl = organisationUrl;
        this.positions = positions;
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

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        public static final Position EMPTY = new Position();
        private String position;
        private String description;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate beginDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private  LocalDate endDate;

        public Position() {
        }

        public Position(String position, String description, LocalDate beginDate, LocalDate endDate) {
            this.position = position;
            this.description = description;
            this.beginDate = beginDate;
            this.endDate = endDate;
        }

        public String getPosition() {
            return position;
        }

        public String getDescription() {
            return description;
        }

        public LocalDate getBeginDate() {
            return beginDate;
        }

        public LocalDate getEndDate() {
            return endDate;
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
