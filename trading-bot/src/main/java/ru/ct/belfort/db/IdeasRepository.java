package ru.ct.belfort.db;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class IdeasRepository {
    private static Connection connection;
    private static Statement statement;

    static {
        try {
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost/trading_bot",
                            "postgres", "postgres");
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }


}
