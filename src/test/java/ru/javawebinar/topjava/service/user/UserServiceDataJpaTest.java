package ru.javawebinar.topjava.service.user;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(value = Profiles.DATAJPA)
public class UserServiceDataJpaTest extends BaseUserServiceTest {
    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
        assertNotNull(user.getMeals());
        assertEquals(MealTestData.MEALS.size(), user.getMeals().size());
        // check without sort order
        assertTrue(MealTestData.MEALS.containsAll(user.getMeals()));
    }
}