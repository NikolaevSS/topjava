package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionSystemException;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger("");
    private static final Map<String, Long> TEST_DURATION_MAP = new HashMap<>();

    @Rule
    public final Stopwatch testStopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            super.finished(nanos, description);
            String test = description.getMethodName();
            Long duration = TimeUnit.NANOSECONDS.toMillis(nanos);
            LOGGER.info(String.format("Test \"%s\" was completed in: %d ms", test, duration));
            TEST_DURATION_MAP.put(test, duration);
        }
    };

    @Rule
    @SuppressWarnings("deprecation")
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private MealService service;
    @Autowired
    private MealRepository repository;

    @AfterClass
    public static void afterClass() {
        String testsDuration = TEST_DURATION_MAP.entrySet().stream()
                .map(entry -> String.format("%25s %4d ms", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n", "\n", "\n"));
        LOGGER.info(testsDuration);
    }

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        Assert.assertNull(repository.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage(equalTo("Not found entity with id=1"));
        service.delete(1, USER_ID);
    }

    @Test
    public void deleteNotOwn() {
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage(equalTo("Not found entity with id=100002"));
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void createRollbackTransaction() {
        Meal newMeal = getCreated();
        newMeal.setDateTime(null);
        expectedException.expect(TransactionSystemException.class);
        expectedException.expectMessage(equalTo("Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Error while committing the transaction"));
        service.create(newMeal, USER_ID);
    }

    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() {
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage(equalTo("Not found entity with id=1"));
        service.get(1, USER_ID);
    }

    @Test
    public void getNotOwn() {
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage(equalTo("Not found entity with id=100002"));
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage(equalTo("Not found entity with id=100002"));
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() {
        MEAL_MATCHER.assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetweenInclusive() {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenWithNullDates() {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(null, null, USER_ID), MEALS);
    }
}