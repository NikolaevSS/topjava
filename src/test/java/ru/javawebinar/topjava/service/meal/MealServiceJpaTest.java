package ru.javawebinar.topjava.service.meal;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(value = Profiles.JPA)
public class MealServiceJpaTest extends BaseMealServiceTest {
}