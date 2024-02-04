package ru.javawebinar.topjava.web.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Component
public class DateTimeConverter implements ConditionalGenericConverter {

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.getAnnotation(ToDate.class) != null || targetType.getAnnotation(ToTime.class) != null;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> types = Set.of(
                new ConvertiblePair(String.class, LocalDate.class),
                new ConvertiblePair(String.class, LocalTime.class));
        return types;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.getAnnotation(ToDate.class) != null ? parseLocalDate((String) source) :
                targetType.getAnnotation(ToTime.class) != null ? parseLocalTime((String) source) : null;
    }
}
