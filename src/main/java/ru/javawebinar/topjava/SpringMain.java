package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            User user = adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
            SecurityUtil.setAuthUserId(user.getId());
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            // Create
            Meal meal = mealRestController.save(
                    new Meal(LocalDateTime.now(), user.getId(), "Test", 100500)
            );
            assert (nonNull(meal.getId()) && meal.getId() > 0);
            // Update
            assert (meal.getCalories() == 100500);
            meal = mealRestController.save(
                    new Meal(meal.getId(), LocalDateTime.now(), user.getId(), null, -1)
            );
            assert (isNull(meal.getDescription()) && meal.getCalories() == -1);
            // Get
            assert (nonNull(mealRestController.get(meal.getId())));
            List<Meal> meals = mealRestController.getAll();
            assert (!meals.isEmpty());
            meals = mealRestController.getFiltered(LocalDate.now(), LocalDate.now());
            assert (!meals.isEmpty());
            // Delete
            mealRestController.delete(meal.getId());
        }
    }
}
