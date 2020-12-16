package com.rentpal.agreement.common;

/*
 * @author frank
 * @created 16 Dec,2020 - 1:41 AM
 */

import java.util.regex.Pattern;

public class Regex {
    public static final Pattern NOT_ALLOWED_CHARACTERS=Pattern.compile("^[^`~.><\":{}=+]+$");
    public static final Pattern POSTAL_CODE=Pattern.compile("^[0-9]{5}$");
    public static final Pattern FLOAT=Pattern.compile("^([0-9]{1,5}\\.[0-9]{1,2}|[0-9]{1,5})$");
    public static final Pattern INTEGER=Pattern.compile("^[0-9]{1,8}$");
    public static final Pattern ALPHA_NUM=Pattern.compile("^[a-zA-Z0-9]+$");
    public static final Pattern BOOLEAN=Pattern.compile("^(true|false)$");
}
