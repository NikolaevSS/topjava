package ru.javawebinar.topjava.service.user;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(value = Profiles.JDBC)
public class UserServiceJdbcTest extends BaseUserServiceTest {
}