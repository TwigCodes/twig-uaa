package com.twigcodes.uaa.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {

    @Override
    public String convertToDatabaseColumn(Set<String> list) {
        // Java 8
        return String.join(",", list);
    }

    @Override
    public Set<String> convertToEntityAttribute(String joined) {
        return Arrays.stream(joined.split(",")).collect(Collectors.toSet());
    }

}
