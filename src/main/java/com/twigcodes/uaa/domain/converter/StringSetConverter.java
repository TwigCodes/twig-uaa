package com.twigcodes.uaa.domain.converter;

import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {

    @Override
    public String convertToDatabaseColumn(Set<String> set) {
        if (set == null || set.size() == 0) {
            return "";
        }
        return String.join(",", set);
    }

    @Override
    public Set<String> convertToEntityAttribute(String joined) {
        if (joined == null || joined.length() == 0) {
            return new HashSet<>();
        }
        return Arrays.stream(joined.split(",")).collect(Collectors.toSet());
    }

}
