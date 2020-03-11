package ru.javawebinar.topjava.service.meal;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(value = Profiles.JDBC)
public class MealServiceJdbcTest extends BaseMealServiceTest {
}