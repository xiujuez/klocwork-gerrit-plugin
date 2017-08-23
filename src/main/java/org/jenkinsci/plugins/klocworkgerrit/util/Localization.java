package org.jenkinsci.plugins.klocworkgerrit.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */

public final class Localization {

    public static final String MESSAGES = "messages";

    private Localization() {
    }

    public static String getLocalized(String s){
        ResourceBundle messages = ResourceBundle.getBundle(MESSAGES);
        return messages.getString(s);
    }

    public static String getLocalized(String s, Object... params){
        String string = getLocalized(s);
        return String.format(string, params);
    }

    public static String getLocalized(String s, Locale l){
        ResourceBundle messages = ResourceBundle.getBundle(MESSAGES, l);
        return messages.getString(s);
    }
}
