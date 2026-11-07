package com.lms.util;

import com.lms.exception.LmsException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Loads db.properties once and hands out JDBC connections. */
public final class DBConnection {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            PROPS.load(in);
            Class.forName(PROPS.getProperty("driver"));
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBConnection() { }

    public static Connection get() {
        try {
            return DriverManager.getConnection(PROPS.getProperty("url"),
                    PROPS.getProperty("user"), PROPS.getProperty("password"));
        } catch (SQLException e) {
            throw new LmsException("Could not connect to database: " + e.getMessage(), e);
        }
    }
}
