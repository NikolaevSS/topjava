package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"datajpa", "postgres"})
public class UserServiceDataJpaTest extends BaseUserServiceTest {
}