package org.nupter.nupter.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import android.util.Log;
import org.nupter.nupter.MyApplication;

import java.util.ArrayList;

import static org.nupter.nupter.utils.Log.*;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 * Date: 13-8-12
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */
public class JsoupTable {
    Elements trs;

    public ArrayList<ArrayList<String>> parse(String html) {
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
            trs = doc.getElementById("Table1").getElementsByTag("tr");
            Elements tds[] = new Elements[5];
            int pos[] = {2, 4, 7, 9, 11};
            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
            ArrayList<String> list1 = new ArrayList<String>();
            ArrayList<String> list2 = new ArrayList<String>();
            ArrayList<String> list3 = new ArrayList<String>();
            ArrayList<String> list4 = new ArrayList<String>();
            ArrayList<String> list5 = new ArrayList<String>();
            list.add(list1);
            list.add(list2);
            list.add(list3);
            list.add(list4);
            list.add(list5);
            for (int i = 0; i < 5; i++) {
                tds[i] = trs.get(pos[i]).getElementsByTag("td");
                for (int j = 0; j < 5; j++) {
                    list.get(i).add(tds[i].get(j + (i + 1) % 2 + 1).text());
                }
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }
}
