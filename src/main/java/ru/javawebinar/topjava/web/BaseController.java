package ru.javawebinar.topjava.web;

import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public abstract class BaseController {
    protected int getRequireParameter(HttpServletRequest request, @NonNull String parameter) {
        String paramId = Objects.requireNonNull(request.getParameter(parameter));
        return Integer.parseInt(paramId);
    }
}
