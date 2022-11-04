package ru.ct.belfort.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.ct.belfort.IdeaDTO;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class IdeasRepository {
    private Connection connection;
    private Statement statement;

    @Autowired
    public IdeasRepository(Environment env) {
        try {
            connection = DriverManager
                    .getConnection(
                            env.getProperty("spring.datasource.url"),
                            env.getProperty("spring.datasource.username"),
                            env.getProperty("spring.datasource.password")
                    );
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
        debugOutput();
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

    private void debugOutput() {
        log.info("Inserted");
        for (IdeaEntity it : selectAll()) {
            log.info(it.toString());
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
