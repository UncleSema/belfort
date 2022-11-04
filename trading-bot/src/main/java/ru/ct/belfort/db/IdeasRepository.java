package ru.ct.belfort.db;

import org.springframework.stereotype.Service;
import ru.ct.belfort.IdeaDTO;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void insert(IdeaDTO idea) {
        final String QUERY = "INSERT INTO ideas(score, time) VALUES(?, CURRENT_TIMESTAMP(0))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY)) {
            preparedStatement.setDouble(1, idea.score());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public List<IdeaEntity> selectAll() {
        final String QUERY = "SELECT * FROM ideas";
        List<IdeaEntity> ideas = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery(QUERY)) {
            while (resultSet.next()) {
                ideas.add(
                        IdeaEntity.builder()
                                .id(resultSet.getInt("id"))
                                .score(resultSet.getDouble("score"))
                                .time(resultSet.getTimestamp("time"))
                                .build()
                );
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return ideas;
    }

    public void deleteAll() {
        final String QUERY = "DELETE FROM ideas";
        try {
            statement.executeUpdate(QUERY);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void destructor() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
