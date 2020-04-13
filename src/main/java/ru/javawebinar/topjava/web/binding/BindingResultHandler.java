package ru.javawebinar.topjava.web.binding;

import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

public interface BindingResultHandler {
    default String parseErrors(BindingResult result) {
        StringJoiner joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(
                fe -> joiner.add(String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
        );
        return joiner.toString();
    }
}
