package com.dessert.sys.common.constants;

import org.apache.commons.lang.StringUtils;

import java.util.ResourceBundle;

public class PropertiesConfig {
    private ResourceBundle resourceBundle;

    public PropertiesConfig(String file) {
        resourceBundle = ResourceBundle.getBundle(file);
    }

    public String getPropByKey(String key) {
        return getPropByKey(key, "");
    }

    public String getPropByKey(String key, String def) {
        String temp;
        try {
            temp = resourceBundle.getString(key);
            return StringUtils.isEmpty(temp) ? def : temp;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getIntPropByKey(String key, int def) {
        int value = def;
        try {
            value = Integer.parseInt(getPropByKey(key));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return value;
    }
}
