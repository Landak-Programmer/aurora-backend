package com.aurora.backend.utils;

public class StringHelper {

    public static String capitalize(final String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str
                .substring(0, 1)
                .toUpperCase() + str.substring(1);
    }
}
