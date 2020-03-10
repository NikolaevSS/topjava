package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"jpa", "postgres"})
public class MealServiceJpaTest extends BaseMealServiceTest {
}