package ru.javawebinar.topjava.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz) || UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Integer id;
        String email;
        if (target instanceof User) {
            id = ((User) target).getId();
            email = ((User) target).getEmail();
        } else {
            id = ((UserTo) target).getId();
            email = ((UserTo) target).getEmail();
        }
        boolean notUniqueEmail =
                userService.getAll().stream()
                        .anyMatch(user ->
                                user.getEmail().equals(email) && !user.getId().equals(id)
                        );
        if (notUniqueEmail) {
            errors.rejectValue(
                    "email", "user.notUniqueEmail", "User with this email already exists"
            );
        }
    }
}
