package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(@Nullable String text, @Nullable Locale locale) {
        return DateTimeUtil.parseLocalTime(text);
    }

    @Override
    public String print(@Nullable LocalTime localTime, @Nullable Locale locale) {
        return Optional.ofNullable(localTime)
                .map(time -> time.format(DateTimeFormatter.ISO_LOCAL_TIME))
                .orElse("");
    }
}
