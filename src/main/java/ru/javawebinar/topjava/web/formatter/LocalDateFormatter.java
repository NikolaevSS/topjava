package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class LocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(@Nullable String text, @Nullable Locale locale) {
        return DateTimeUtil.parseLocalDate(text);
    }

    @Override
    public String print(@Nullable LocalDate localDate, @Nullable Locale locale) {
        return Optional.ofNullable(localDate)
                .map(date -> date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .orElse("");
    }
}
