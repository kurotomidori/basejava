package com.urise.webapp.sql;

import com.urise.webapp.exeption.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    public static void execute(ConnectionFactory connectionFactory, String statement) {
        execute(connectionFactory, statement, PreparedStatement::execute);
    }
    public static <T> T execute(ConnectionFactory connectionFactory, String statement, SqlExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)
        ) {
            return executor.execute(ps);
        } catch (SQLException e) {
            throw ExceptionConverter.convert(e);
        }
    }

    public static <T> T transactionalExecute(ConnectionFactory connectionFactory, SqlTransaction<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw ExceptionConverter.convert(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

}
