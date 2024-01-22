package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.ResumeTestData;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                break;
            case "edit":
                r = storage.get(uuid);
                for(SectionType type : SectionType.values()) {
                    Section sec = r.getSection(type);
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL:
                            if (sec == null) {
                                r.setSection(type, TextSection.EMPTY);
                            }
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            if (sec == null) {
                                r.setSection(type, ListSection.EMPTY);
                            }
                            break;
                        case EDUCATION:
                        case EXPERIENCE:
                            OrganisationsListSection orgSection = (OrganisationsListSection) sec;
                            List<Organisation> emptyOrganisationFirst = new ArrayList<>();
                            emptyOrganisationFirst.add(Organisation.EMPTY);
                            if(sec != null) {
                                for(Organisation org : orgSection.getContent()) {
                                    List<Organisation.Position> firstEmptyPosition = new ArrayList<>();
                                    firstEmptyPosition.add(Organisation.Position.EMPTY);
                                    firstEmptyPosition.addAll(org.getPositions());
                                    emptyOrganisationFirst.add(new Organisation(org.getOrganisationName(), org.getOrganisationUrl(), firstEmptyPosition));
                                }
                            }
                            r.setSection(type, new OrganisationsListSection(emptyOrganisationFirst));
                            break;
                    }
                }
                break;
            case "new":
                r = Resume.EMPTY;
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal" );
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r;
        final boolean isExist = uuid != null && uuid.trim().length() != 0;
        if (isExist) {
            r = storage.get(uuid);
        } else {
            r = new Resume(fullName);
        }
        r.setFullName(fullName);
        for (ContactType type: ContactType.values()) {
            String value = request.getParameter(type.name());
            if(value != null && value.trim().length() != 0) {
                r.setContact(type, new Contact(value));
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type: SectionType.values()) {
            if(type.equals(SectionType.PERSONAL) || type.equals(SectionType.OBJECTIVE)) {
                String value = request.getParameter(type.name());
                if(value != null && value.trim().length() != 0) {
                    r.setSection(type, new TextSection(value));
                } else {
                    r.getSections().remove(type);
                }
            } else if (type.equals(SectionType.ACHIEVEMENT) || type.equals(SectionType.QUALIFICATIONS)) {
                String value = request.getParameter(type.name());
                if(value != null && value.trim().length() != 0) {
                    r.setSection(type, new ListSection(value.split("\n")));
                } else {
                    r.getSections().remove(type);
                }
            } else {
                List<Organisation> organisationsList = new ArrayList<>();
                String[] organisationsNames = request.getParameterMap().get(type.name() + "_name");
                for (int i = 0; i < organisationsNames.length; i++) {
                    if (organisationsNames[i] != null && organisationsNames[i].trim().length() != 0){
                        String link = request.getParameter(type.name() + "_" + i + "_link");
                        List<Organisation.Position> positionsList = new ArrayList<>();
                        String[] positionsNames = request.getParameterMap().get(type.name() + "_"  + i + "_position");
                        String[] positionsBeginning = request.getParameterMap().get(type.name() + "_"  + i + "_beg_date");
                        String[] positionsEnding = request.getParameterMap().get(type.name() + "_"  + i + "_end_date");
                        String[] positionsDescription = request.getParameterMap().get(type.name() + "_"  + i + "_description");
                        for (int j = 0; j < positionsNames.length; j++) {
                            if(positionsNames[j] != null && positionsNames[j].trim().length() != 0) {
                                positionsList.add(new Organisation.Position(positionsNames[j], positionsDescription[j], DateUtil.of(positionsBeginning[j]), DateUtil.of(positionsEnding[j])));
                            }
                        }
                        organisationsList.add(new Organisation(organisationsNames[i], link, positionsList));
                    }
                }
                if(!organisationsList.isEmpty()) {
                    r.setSection(type, new OrganisationsListSection(organisationsList));
                } else {
                    r.getSections().remove(type);
                }
            }
        }
        if(isExist) {
            storage.update(r);
        } else {
            storage.save(r);
        }
        response.sendRedirect("resume");
    }
}
