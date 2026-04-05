package com.example.house.controller.shared;

import java.time.LocalDate;
import java.time.YearMonth;

public final class ControllerInputParser {
    private ControllerInputParser() {
    }

    public static String required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " khong duoc de trong");
        }
        return value.trim();
    }

    public static String nullable(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public static int parseInt(String value, String field) {
        try {
            return Integer.parseInt(required(value, field));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(field + " phai la so nguyen");
        }
    }

    public static double parseDouble(String value, String field) {
        try {
            return Double.parseDouble(required(value, field));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(field + " phai la so");
        }
    }

    public static YearMonth parseMonth(String value) {
        try {
            return YearMonth.parse(required(value, "Thang"));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Thang phai theo dinh dang YYYY-MM");
        }
    }

    public static LocalDate requireDate(LocalDate value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " khong duoc de trong");
        }
        return value;
    }
}

