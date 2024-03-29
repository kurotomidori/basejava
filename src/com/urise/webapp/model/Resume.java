package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final Resume EMPTY = new Resume();

    static {
        EMPTY.setSection(SectionType.OBJECTIVE, TextSection.EMPTY);
        EMPTY.setSection(SectionType.PERSONAL, TextSection.EMPTY);
        EMPTY.setSection(SectionType.ACHIEVEMENT, ListSection.EMPTY);
        EMPTY.setSection(SectionType.QUALIFICATIONS, ListSection.EMPTY);
        EMPTY.setSection(SectionType.EDUCATION, new OrganisationsListSection(Organisation.EMPTY));
        EMPTY.setSection(SectionType.EXPERIENCE, new OrganisationsListSection(Organisation.EMPTY));
    }

    private final Map<SectionType, Section<?>> sections = new EnumMap<>(SectionType.class);
    private final Map<ContactType, Contact> contacts = new EnumMap<>(ContactType.class);
    private String uuid;

    private String fullName;

    public void setSection(SectionType sectionType, Section<?> data) {
        sections.put(sectionType, data);
    }

    public void setContact(ContactType contactType, Contact data) {
        contacts.put(contactType, data);
    }


    public Map<SectionType, Section<?>> getSections() {
        return sections;
    }

    public Map<ContactType, Contact> getContacts() {
        return contacts;
    }

    public Section<?> getSection(SectionType sectionType) {
        return sections.get(sectionType);
    }

    public Contact getContact(ContactType contactType) {
        return contacts.get(contactType);
    }

    public Resume() {
    }

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }


    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return uuid + '(' + fullName + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(sections, resume.sections) && Objects.equals(contacts, resume.contacts) && Objects.equals(uuid, resume.uuid) && Objects.equals(fullName, resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections, contacts, uuid, fullName);
    }
}
