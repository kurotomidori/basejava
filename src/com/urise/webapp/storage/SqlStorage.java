package com.urise.webapp.storage;

import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.util.JsonParser;


import java.sql.*;
import java.util.*;


public class SqlStorage implements Storage{
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
    @Override
    public void clear() {
        SqlHelper.execute(connectionFactory,"DELETE FROM resume");
    }

    @Override
    public void update(Resume r) {
        SqlHelper.transactionalExecute(connectionFactory, conn -> {
            try(PreparedStatement ps = conn.prepareStatement("UPDATE resume r SET full_name = ? WHERE r.uuid = ?")) {
                ps.setString(2, r.getUuid());
                ps.setString(1, r.getFullName());
                if(ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            deleteContacts(conn, r);
            deleteSections(conn, r);
            insertContacts(conn, r);
            insertSections(conn, r);
            return null;
        });
    }



    @Override
    public void save(Resume r) {
        SqlHelper.transactionalExecute(connectionFactory, conn -> {
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContacts(conn, r);
            insertSections(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return SqlHelper.transactionalExecute(connectionFactory, conn -> {
            Resume r;
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid =? ")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                r = new Resume(uuid, rs.getString("full_name"));
            }
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact c WHERE c.resume_uuid =? ")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                       addContacts(rs, r);
                   }
            }
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM section s WHERE s.resume_uuid =? ")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSections(rs, r);
                }
            }
            return r;
        });
    }

    @Override
    public void delete(String uuid) {
        SqlHelper.execute(connectionFactory,"DELETE FROM resume r WHERE r.uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    if(ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(uuid);
                    }
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return SqlHelper.transactionalExecute(connectionFactory, conn -> {
            Map<String, Resume> map = new LinkedHashMap<>();
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    map.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact c")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addContacts(rs, map.get(rs.getString("resume_uuid")));
                }
            }
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM section s")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSections(rs, map.get(rs.getString("resume_uuid")));
                }
            }
            return new ArrayList<>(map.values());
        });
    }

    @Override
    public int size() {
        return SqlHelper.execute(connectionFactory,"SELECT COUNT (*) FROM resume",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    return rs.next() ? rs.getInt(1) : 0;
                });
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try(PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, Contact> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue().getContact());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection conn, Resume r) throws SQLException {
        deleteAttribute(conn, r, "DELETE FROM contact c WHERE c.resume_uuid = ?");
    }

    private void deleteSections(Connection conn, Resume r) throws SQLException {
        deleteAttribute(conn, r, "DELETE FROM section s WHERE s.resume_uuid = ?");
    }

    private void deleteAttribute(Connection conn, Resume r, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    private void addContacts(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("type");
        if (type != null) {
            r.setContact(ContactType.valueOf(type), new Contact(rs.getString("value")));
        }
    }

    private void addSections(ResultSet rs, Resume r) throws SQLException {
        String content = rs.getString("value");
        if(content != null) {
            SectionType type = SectionType.valueOf(rs.getString("type"));
            r.setSection(type, JsonParser.read(content, Section.class));
        }
    }

    private void insertSections(Connection conn, Resume r) throws SQLException {
        try(PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section<?>> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                String type = e.getKey().name();
                ps.setString(2, type);
                Section section = e.getValue();
                ps.setString(3, JsonParser.write(section, Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
