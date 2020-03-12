package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.BaseUserServiceTest;

@ActiveProfiles(value = Profiles.JPA)
public class UserServiceJpaTest extends BaseUserServiceTest {
}