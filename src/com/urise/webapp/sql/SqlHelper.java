package com.urise.webapp.sql;

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

}
