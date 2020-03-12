package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.BaseMealServiceTest;

@ActiveProfiles(value = Profiles.JPA)
public class MealServiceJpaTest extends BaseMealServiceTest {
}