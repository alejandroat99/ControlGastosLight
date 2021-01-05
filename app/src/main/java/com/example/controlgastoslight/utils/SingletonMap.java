package com.example.controlgastoslight.utils;

import java.util.HashMap;
import java.util.Map;

public class SingletonMap {
    private static Map<String, Object> map;

    public static synchronized Map<String, Object> getSingletonMap(){
        if(map == null){
            map = new HashMap<>();
        }
        return map;
    }

    public static synchronized void putSingletonMap(String key, Object value){
        if(map == null){
            map = new HashMap<>();
        }
        map.put(key, value);
    }

    public static synchronized Object getSingletonMap(String key){
        if (map != null){
            return map.get(key);
        }
        return null;
    }
}
