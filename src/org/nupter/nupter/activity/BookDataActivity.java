package org.nupter.nupter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author WangTao
 */

public class BookDataActivity {
    public static List<Map<String, String>> getMaps() {
        List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();

        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("bookName", "Android");
        map1.put("bookAuthor", "Hello");
        map1.put("bookNum", "AK47");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("bookName", "IOS");
        map2.put("bookAuthor", "World");
        map2.put("bookNum", "AK48");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("bookName", "Java");
        map3.put("bookAuthor", "Are");
        map3.put("bookNum", "AK49");

        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("bookName", "PHP");
        map4.put("bookAuthor", "You");
        map4.put("bookNum", "AK50");

        listMaps.add(map1);
        listMaps.add(map2);
        listMaps.add(map3);
        listMaps.add(map4);

        return listMaps;
    }

    public static List<Map<String, String>> getLostList() {
        List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();

        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("lostName", "饭卡");
        map1.put("owner", "Tom");
        map1.put("phone", "18362971234");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("lostName", "手机");
        map2.put("owner", "Lisa");
        map2.put("phone", "18362973456");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("lostName", "身份证");
        map3.put("owner", "Tony");
        map3.put("phone", "18362972345");

        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("lostName", "钱包");
        map4.put("owner", "Monica");
        map4.put("phone", "18362974562");

        listMaps.add(map1);
        listMaps.add(map2);
        listMaps.add(map3);
        listMaps.add(map4);

        return listMaps;
    }
}
