package ru.javawebinar.topjava.repository.jdbc.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Date;

@Repository
@Profile(Profiles.HSQL_DB)
public class HsqldbJdbcMealRepository extends BaseJdbcMealRepository<Date> {
    @Autowired
    public HsqldbJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected Date getDateTimeArg(LocalDateTime localDateTime) {
        return DateTimeUtil.toDate(localDateTime);
    }
}
