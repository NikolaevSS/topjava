package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"jdbc", "postgres"})
public class MealServiceJdbcTest extends BaseMealServiceTest {
}