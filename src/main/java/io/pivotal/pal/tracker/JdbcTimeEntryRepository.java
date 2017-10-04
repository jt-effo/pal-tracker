package io.pivotal.pal.tracker;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, Date.valueOf(timeEntry.getDate()));
            ps.setInt(4, timeEntry.getHours());

            return ps;
        }, keyHolder);

        timeEntry.setId(keyHolder.getKey().longValue());

        return timeEntry;
    }

    @Override
    public TimeEntry find(Long id) {
        return jdbcTemplate.query("Select * from time_entries where id = ?",
                new Object[]{id},
                new ResultSetExtractor<TimeEntry>() {
                    private TimeEntry mapRow(ResultSet rs) throws SQLException {
                        return new TimeEntry(rs.getLong("id"),
                                rs.getLong("project_id"),
                                rs.getLong("user_id"),
                                rs.getDate("date").toLocalDate(),
                                rs.getInt("hours"));
                    }

                    @Override
                    public TimeEntry extractData(ResultSet rs) throws SQLException, DataAccessException {
                        return rs.next() ? mapRow(rs) : null;
                    }
                });

    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("select id, project_id, user_id, date, hours from time_entries",
                (rs, rowNum) -> new TimeEntry(rs.getLong("id"),
                        rs.getLong("project_id"),
                        rs.getLong("user_id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("hours")));
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("update time_entries " +
                    "set project_id = ?, " +
                    "user_id = ?, " +
                    "date = ?, " +
                    "hours = ? " +
                    "where id = ?");

            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, Date.valueOf(timeEntry.getDate()));
            ps.setInt(4, timeEntry.getHours());
            ps.setLong(5, id);

            return ps;
        });

        timeEntry.setId(id);
        return timeEntry;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from time_entries where id = ?", id);
    }
}
