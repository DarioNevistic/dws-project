package com.dnevi.healthcare.util;

import java.util.Objects;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");

    private StringUtil() {
    }

    public static boolean isEmail(String email) {
        return EMAIL_PATTERN.matcher(Objects.requireNonNull(email, "Email can't be null"))
                .matches();
    }
}
