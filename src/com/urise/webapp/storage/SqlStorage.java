package com.urise.webapp.storage;

import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlHelper;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;



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
        SqlHelper.execute(connectionFactory,"UPDATE resume r SET full_name = ? WHERE r.uuid = ?",
                ps -> {
                    ps.setString(1, r.getFullName());
                    ps.setString(2, r.getUuid());
                    if(ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(r.getUuid());
                    }
                    return null;
                });
    }

    @Override
    public void save(Resume r) {
        SqlHelper.execute(connectionFactory,"INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                ps -> {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, r.getFullName());
                    ps.execute();
                    return null;
                });
    }

    @Override
    public Resume get(String uuid) {
        return SqlHelper.execute(connectionFactory,"SELECT * FROM resume r WHERE r.uuid =?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    return new Resume(uuid, rs.getString("full_name"));
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
        List<Resume> sortedList = new ArrayList<>();
        SqlHelper.execute(connectionFactory,"SELECT * FROM resume r ORDER BY full_name, uuid",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()) {
                        sortedList.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
                    }
                    return null;
                });

        return sortedList;
    }

    @Override
    public int size() {
        return SqlHelper.execute(connectionFactory,"SELECT COUNT (*) FROM resume",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    return rs.next() ? rs.getInt(1) : 0;
                });
    }
}
