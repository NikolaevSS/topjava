package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.BaseUserServiceTest;

@ActiveProfiles(value = Profiles.JDBC)
public class UserServiceJdbcTest extends BaseUserServiceTest {
}