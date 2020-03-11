package ru.javawebinar.topjava.service.user;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(value = Profiles.JPA)
public class UserServiceJpaTest extends BaseUserServiceTest {
}