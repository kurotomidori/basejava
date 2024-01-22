<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.ListSection" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size="30" value="${resume.getContact(type).contact}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <c:forEach var="sectionEntry" items="${SectionType.values()}">
            <jsp:useBean id="sectionEntry" type="com.urise.webapp.model.SectionType"/>
            <dl>
                <dt>${sectionEntry.title}</dt>
                <c:choose>
                    <c:when test="${sectionEntry.equals(SectionType.PERSONAL) || sectionEntry.equals(SectionType.OBJECTIVE)}">
                        <dd><input type="text" name="${sectionEntry.name()}" size="100" value="${resume.getSection(sectionEntry).content}"></dd>
                    </c:when>
                    <c:when test="${sectionEntry.equals(SectionType.ACHIEVEMENT) || sectionEntry.equals(SectionType.QUALIFICATIONS)}">
                        <dd><textarea name="${sectionEntry.name()}" cols="100" rows="5"><%= resume.getSection(sectionEntry) == null ? "" : String.join("\n", ((ListSection) resume.getSection(sectionEntry)).getContent())%></textarea></dd>
                    </c:when>
                    <c:when test="${sectionEntry.equals(SectionType.EXPERIENCE) || sectionEntry.equals(SectionType.EDUCATION)}">
                        <c:forEach var="organisation" items="${resume.getSection(sectionEntry).content}" varStatus="counter">
                            <jsp:useBean id="organisation" type="com.urise.webapp.model.Organisation"/>
                            <dl>
                                <dt>Название организации</dt>
                                <dd><input type="text" name="${sectionEntry.name()}_name" size="100" value="${organisation.organisationName}"></dd><br/>
                                <dt>Сайт организации</dt>
                                <dd><input type="text" name="${sectionEntry.name()}_${counter.index}_link" size="100" value="${organisation.organisationUrl}"></dd><br/><br/>
                                <c:forEach var="position" items="${organisation.positions}">
                                    <jsp:useBean id="position" type="com.urise.webapp.model.Organisation.Position"/>
                                    <dt>Позиция</dt>
                                    <dd><input type="text" name="${sectionEntry.name()}_${counter.index}_position" size="100" value="${position.position}"></dd><br/>
                                    <dt>Начальная дата</dt>
                                    <dd><input type="date" name="${sectionEntry.name()}_${counter.index}_beg_date" size="100" value="${position.beginDate}"></dd><br/>
                                    <dt>Конечная дата</dt>
                                    <dd><input type="date" name="${sectionEntry.name()}_${counter.index}_end_date" size="100" value="${position.endDate}"></dd><br/>
                                    <dt>Описание</dt>
                                    <dd><input type="text" name="${sectionEntry.name()}_${counter.index}_description" size="100" value="${position.description}"></dd><br/><br/>
                                </c:forEach>
                            </dl><br/>
                        </c:forEach>
                    </c:when>
                </c:choose>
                <br/>
            </dl>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
