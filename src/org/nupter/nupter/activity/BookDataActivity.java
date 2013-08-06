package org.nupter.nupter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Foci
 * Date: 13-8-6
 * Time: 上午11:02
 * To change this template use File | Settings | File Templates.
 */
public class BookDataActivity {
    public static List<Map<String, String>> getMaps() {
        List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();

        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("r_book_name", "Android");
        map1.put("r_book_address", "自然科学阅览室");
        map1.put("r_book_num", "2");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("r_book_name", "IOS");
        map2.put("r_book_address", "自然科学阅览室");
        map2.put("r_book_num", "1");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("r_book_name", "Java");
        map3.put("r_book_address", "自然科学阅览室");
        map3.put("r_book_num", "4");

        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("r_book_name", "PHP");
        map4.put("r_book_address", "自然科学阅览室");
        map4.put("r_book_num", "5");

        listMaps.add(map1);
        listMaps.add(map2);
        listMaps.add(map3);
        listMaps.add(map4);

        return listMaps;
    }
}
