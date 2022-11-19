package ru.ct.belfort.db;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.ct.belfort.UserDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final UserEntityMapper mapper = new UserEntityMapper();

    public List<UserEntity> selectAll() {
        return jdbcTemplate.query("SELECT * FROM users", mapper);
    }

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class UserEntityMapper implements RowMapper<UserEntity> {
        @Override
        public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return UserEntity.builder()
                    .id(rs.getInt("id"))
                    .str(rs.getString("str"))
                    .build();
        }
    }
}
