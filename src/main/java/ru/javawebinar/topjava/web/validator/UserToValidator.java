package ru.javawebinar.topjava.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;

@Component
public class UserToValidator implements Validator {
    private final UserService userService;

    public UserToValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo userTo = (UserTo) target;
        boolean notUniqueEmail =
                userService.getAll().stream()
                        .anyMatch(user ->
                                user.getEmail().equals(userTo.getEmail()) && !user.getId().equals(userTo.getId())
                        );
        if (notUniqueEmail) {
            errors.rejectValue(
                    "email", "user.notUniqueEmail", "User with this email already exists"
            );
        }
    }
}
