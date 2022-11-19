package ru.ct.belfort.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.ct.belfort.IdeaDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Service
@Slf4j
public class IdeasRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final IdeaEntityMapper mapper = new IdeaEntityMapper();

    @Autowired
    public IdeasRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(IdeaDTO idea) {
        jdbcTemplate.update("""
            INSERT INTO ideas(score, time) VALUES(?, CURRENT_TIMESTAMP(0))
        """, idea.score());
    }

    public List<IdeaEntity> selectAll() {
        return jdbcTemplate.query("SELECT * FROM ideas", mapper);
    }

    public Integer getRecordsAmount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ideas", Integer.class);
    }

    public IdeaEntity getLastRecord() {
        return jdbcTemplate.query("SELECT * FROM ideas ORDER BY time DESC LIMIT 1", mapper).get(0);
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM ideas");
    }

    private static class IdeaEntityMapper implements RowMapper<IdeaEntity> {

        @Override
        public IdeaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return IdeaEntity.builder()
                    .id(rs.getInt("id"))
                    .score(rs.getDouble("score"))
                    .time(rs.getTimestamp("time"))
                    .build();
        }
    }
}
