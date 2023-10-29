package com.urise.webapp;

import com.urise.webapp.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = new Resume("uuid", "Григорий Кислин");
        List<String> achievements = new ArrayList<>();
        makeAchievements(achievements);
        List<String> qualifications = new ArrayList<>();
        makeQualifications(qualifications);
        List<Organisation> organisationsWork = new ArrayList<>();
        makeOrganisationsWork(organisationsWork);
        List<Organisation> organisationsEducation = new ArrayList<>();
        makeOrganisationsEdu(organisationsEducation);
        resume.setContact(ContactType.TELEPHONE, new Contact("+7(921) 855-0482"));
        resume.setContact(ContactType.SKYPE, new Contact("skype:grigory.kislin"));
        resume.setContact(ContactType.EMAIL, new Contact("gkislin@yandex.ru"));
        resume.setContact(ContactType.LINKED_IN, new Contact("https://www.linkedin.com/in/gkislin"));
        resume.setContact(ContactType.GIT_HUB, new Contact("https://github.com/gkislin"));
        resume.setContact(ContactType.STACKOVERFLOW, new Contact("https://stackoverflow.com/users/548473"));
        resume.setContact(ContactType.HOMEPAGE, new Contact("http://gkislin.ru/"));
        resume.setSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.setSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
        resume.setSection(SectionType.ACHIEVEMENT, new ListSection(achievements));
        resume.setSection(SectionType.QUALIFICATIONS, new ListSection(qualifications));
        resume.setSection(SectionType.EXPERIENCE, new OrganisationsListSection(organisationsWork));
        resume.setSection(SectionType.EDUCATION, new OrganisationsListSection(organisationsEducation));

        System.out.println(resume.getFullName());
        for (ContactType type : ContactType.values()) {
            System.out.print(type.getTitle() + " ");
            System.out.println(resume.getContact(type));
        }
        System.out.println("--------------------------------------------------------");

        for (SectionType type: SectionType.values()) {
            System.out.println(type.getTitle());
            System.out.println(resume.getSection(type).getContent());
        }
    }

    private static void makeAchievements(List<String> achievements) {
        achievements.add("Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет");
        achievements.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.");
        achievements.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk. ");
        achievements.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера. ");
        achievements.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        achievements.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django). ");
        achievements.add("Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
    }

    public static void makeQualifications(List<String> qualifications) {
        qualifications.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2 ");
        qualifications.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce ");
        qualifications.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB ");
    }

    public static void makeOrganisationsWork(List<Organisation> organisationsWork){
        organisationsWork.add(new Organisation("Java Online Projects","http://javaops.ru/","Автор проекта.","Создание, организация и проведение Java онлайн проектов и стажировок.", LocalDate.parse("2013-10-01"), null));
        organisationsWork.add(new Organisation("Wrike","https://www.wrike.com/","Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.",LocalDate.parse("2014-10-01"), LocalDate.parse("2016-01-01")));
    }

    public static void makeOrganisationsEdu(List<Organisation> organisationsEducation) {
        organisationsEducation.add(new Organisation("Coursera","https://www.coursera.org/course/progfun","'Functional Programming Principles in Scala' by Martin Odersky","",LocalDate.parse("2013-03-01"),LocalDate.parse("2013-05-01")));
        organisationsEducation.add(new Organisation("Luxoft","http://www.luxoft-training.ru/training/catalog/course.html?ID=22366","Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'","",LocalDate.parse("2011-03-01"),LocalDate.parse("2011-04-01")));
    }
}
