package com.urise.webapp.storage;

import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.Contact;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlHelper;


import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SqlStorage implements Storage{
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
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
            deleteContacts(r);
            insertContacts(conn, r);
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
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return SqlHelper.execute(connectionFactory,"" +
                        "   SELECT * FROM resume r " +
                        "LEFT JOIN contact c" +
                        "       ON r.uuid = c.resume_uuid" +
                        "    WHERE r.uuid =?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }

                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        addContacts(rs, r);
                    } while (rs.next());

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
        return SqlHelper.execute(connectionFactory,"SELECT * FROM resume r\n" +
                                                     "LEFT JOIN contact c ON r.uuid = c.resume_uuid\n" +
                                                     "ORDER BY full_name, uuid",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    Map<String, Resume> map = new LinkedHashMap<>();
                    while(rs.next()) {
                        String uuid = rs.getString("uuid");
                        Resume r = map.get(uuid);
                        if (r == null) {
                            r = new Resume(uuid, rs.getString("full_name"));
                            map.put(uuid, r);
                        }
                        addContacts(rs, r);
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

    private void deleteContacts(Resume r) {
        SqlHelper.execute(connectionFactory,"DELETE FROM contact c WHERE c.resume_uuid = ?", ps -> {
            ps.setString(1, r.getUuid());
            ps.execute();
            return null;
        });
    }

    private void addContacts(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("type");
        if (type != null) {
            r.setContact(ContactType.valueOf(type), new Contact(rs.getString("value")));
        }
    }
}
