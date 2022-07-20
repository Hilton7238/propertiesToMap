package com.h.util;

import java.util.*;

public class PropertiesToMap {

    //a=1:{a=1} b.c=1:{b={}}
    private static void toMap(Map<String, String> map, Map result) {
        map.forEach((k, v) -> {
            if (k.toString().indexOf(".") == -1) {
                if (v.toString().indexOf(",") == -1)
                    result.put(k.toString(), v);
                else
                    result.put(k.toString(), toArray(v.toString()));

            } else {
                int index = k.toString().indexOf(".");
                result.put(k.toString().substring(0, index), new HashMap<String, Object>());
            }
        });
    }

    private static ArrayList<String> toArray(String str) {
        StringBuilder strBuilder = new StringBuilder(str);
        ArrayList<String> array = new ArrayList<>();
        while (strBuilder.indexOf(",") != -1) {
            array.add(strBuilder.substring(0, strBuilder.indexOf(",")));
            strBuilder.replace(0, strBuilder.length(), strBuilder.substring(strBuilder.indexOf(",") + 1));
        }
        array.add(strBuilder.toString());
        return array;
    }

    private static Map<String, String> filter(Map<String, String> map, String str) {
        Map<String, String> m = new HashMap<>();
        map.forEach((k, v) -> {
            if (k.toString().length() > str.length() && k.toString().substring(0, str.length()).equals(str)) {
                m.put(k.toString().substring(str.length() + 1), v);
            }
        });
        return m;
    }

    //main method:translate properties to map
    private static void propertiesToMap(Map<String, String> map, Map result) {
        toMap(map, result);
        result.forEach((k, v) -> {
            if (v instanceof Map) {
                Map<String, String> filter = filter(map, k.toString());
                propertiesToMap(filter, (Map) v);
            }
        });
    }

    //read properties and translate to properties to map
    public static void propertiesToMap(Properties properties, Map result) {
        Map<String, String> propertiesKeys = new HashMap<String, String>();
        StringBuilder str = new StringBuilder();
        Enumeration<Object> keys = properties.keys();
        while (keys.hasMoreElements()) {
            str.delete(0, str.length());
            str.append(keys.nextElement().toString());
            propertiesKeys.put(str.toString(), properties.get(str.toString()).toString());
        }
        propertiesToMap(propertiesKeys, result);
    }
}
