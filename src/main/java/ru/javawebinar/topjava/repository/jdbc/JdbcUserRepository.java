package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.validation.Validator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {
    private static final RowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            Validator validator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(validator, user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId());
        }
        List<Role> roles = new ArrayList<>(user.getRoles());
        if (!roles.isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES (?,?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, user.getId());
                            ps.setString(2, roles.get(i).name());
                        }

                        @Override
                        public int getBatchSize() {
                            return roles.size();
                        }
                    });
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_role r on u.id = r.user_id WHERE u.id=?", this::extractUsers, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_role r on u.id = r.user_id WHERE email=?", this::extractUsers, email);
        return DataAccessUtils.uniqueResult(users); // по поводу uniqueResult не совсем поняла идею. У нас есть уникальный индекс на email, т.е. этот запрос всегда вернет уникального пользователя
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_role r on u.id = r.user_id ORDER BY name, email", this::extractUsers);
    }

    private List<User> extractUsers(ResultSet rs) throws SQLException {
        Map<Integer, User> userMap = new LinkedHashMap<>();
        while (rs.next()) {
            int userId = rs.getInt("id");
            User user = userMap.get(userId);
            if (user != null) {
                user.getRoles().add(Role.valueOf(rs.getString("role")));
            } else {
                user = ROW_MAPPER.mapRow(rs, rs.getRow());
                userMap.put(userId, user);
                String role = rs.getString("role");
                EnumSet<Role> roles = role != null ? EnumSet.of(Role.valueOf(role)) : EnumSet.noneOf(Role.class);
                user.setRoles(roles);
            }
        }
        return new ArrayList<>(userMap.values());
    }
}
