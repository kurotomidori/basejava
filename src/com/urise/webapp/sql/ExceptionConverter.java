package com.urise.webapp.sql;

import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.StorageException;

import java.sql.SQLException;

public class ExceptionConverter {
    private ExceptionConverter() {
    }
    public static StorageException convert(SQLException e) {
        if (e != null && (e.getSQLState().equals("23505"))) {
                return new ExistStorageException(null);
        }
        return new StorageException(e);
    }
}
