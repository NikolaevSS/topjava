package ru.javawebinar.topjava.util;

import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public final class ControllerUtil {
    private ControllerUtil() {
    }

    public static int getRequireParameter(HttpServletRequest request, @NonNull String parameter) {
        String paramId = Objects.requireNonNull(request.getParameter(parameter));
        return Integer.parseInt(paramId);
    }
}
