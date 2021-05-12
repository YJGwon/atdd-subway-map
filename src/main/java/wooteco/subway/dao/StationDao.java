package wooteco.subway.dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.ExceptionStatus;
import wooteco.subway.exception.SubwayException;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {
    private static final RowMapper<Station> BASIC_STATION_ROW_MAPPER = (resultSet, rowNumber) -> {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        return new Station(id, name);
    };
    private static final int ROW_COUNTS_FOR_ID_NOT_FOUND = 0;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public StationDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public long save(Station station) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(station);
        try {
            return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource)
                    .longValue();
        } catch (DuplicateKeyException duplicateKeyException) {
            throw new SubwayException(ExceptionStatus.DUPLICATED_NAME);
        }
    }

    public List<Station> findAll() {
        String query = "select id, name from station";
        return jdbcTemplate.query(query, BASIC_STATION_ROW_MAPPER);
    }

    public Optional<Station> findById(long id) {
        String query = "select id, name from station where id = :id";
        try {
            Station station = jdbcTemplate.queryForObject(query, Collections.singletonMap("id", id), BASIC_STATION_ROW_MAPPER);
            return Optional.ofNullable(station);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }

    public void deleteById(long id) {
        String query = "delete from station where id = :id";
        int affectedRowCounts = jdbcTemplate.update(query, Collections.singletonMap("id", id));
        if (affectedRowCounts == ROW_COUNTS_FOR_ID_NOT_FOUND) {
            throw new SubwayException(ExceptionStatus.ID_NOT_FOUND);
        }
    }
}
