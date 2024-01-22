<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>


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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry" type="java.util.Map.Entry<com.urise.webapp.model.ContactType,com.urise.webapp.model.Contact>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue().getContact())%><br/>
        </c:forEach>
    </p>
    <br/>
    <c:forEach var="sectionEntry" items="${resume.sections}">
        <jsp:useBean id="sectionEntry" type="java.util.Map.Entry<com.urise.webapp.model.SectionType,com.urise.webapp.model.Section>"/>
        <h2>${sectionEntry.key.title}</h2>
        <c:choose>
            <c:when test="${sectionEntry.key.equals(SectionType.PERSONAL) || sectionEntry.key.equals(SectionType.OBJECTIVE)}">
                <%=sectionEntry.getValue().getContent()%><br/>
            </c:when>
            <c:when test="${sectionEntry.key.equals(SectionType.ACHIEVEMENT) || sectionEntry.key.equals(SectionType.QUALIFICATIONS)}">
                <ul>
                    <c:forEach var="textLine" items="${sectionEntry.value.content}">
                        <jsp:useBean id="textLine" type="java.lang.String"/>
                        <li>${textLine}</li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:when test="${sectionEntry.key.equals(SectionType.EXPERIENCE) || sectionEntry.key.equals(SectionType.EDUCATION)}">
                <ul>
                    <c:forEach var="organisation" items="${sectionEntry.value.content}">
                        <jsp:useBean id="organisation" type="com.urise.webapp.model.Organisation"/>
                        <li>
                            <h3><a href="${organisation.organisationUrl}">${organisation.organisationName}</a></h3>
                            <ul>
                                <c:forEach var="position" items="${organisation.positions}">
                                    <jsp:useBean id="position" type="com.urise.webapp.model.Organisation.Position"/>
                                    <li>
                                        <h4>${position.position}</h4>
                                        <%=DateUtil.dateToString(position.getBeginDate())%> - <%=DateUtil.dateToString(position.getEndDate())%><br/>
                                        ${position.description}
                                    </li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
        </c:choose>
        <br/>
    </c:forEach>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
