package org.nupter.nupter.utils;

import android.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: helloworld
 * Date: 13-9-1
 * Time: 下午8:10
 * To change this template use File | Settings | File Templates.
 */
public class JsoupBookList {
    private Map<String, String> map;
    public String name = "", href = "", num = "", author = "", press = "", authorAndPress = "", spanString = "";

    public List<Map<String, String>> parse(String response, List<Map<String, String>> bookListMap) {
        Document doc = Jsoup.parse(response);
        Elements bookLists = doc.getElementsByClass("list_books");
        int listSize = bookLists.size();
        for (int i = 0; i < listSize; i++) {
            Elements h3 = bookLists.get(i).select("h3");
            int h3Size = h3.size();
            for (int i_h3 = 0; i_h3 < h3Size; i_h3++) {
                Elements a = h3.get(i_h3).getElementsByTag("a");
                href = a.attr("href");
                name = a.text();
                Elements span = h3.get(i_h3).select("span");
                spanString = span.get(i_h3).text();
                num = h3.text().substring(spanString.length() + name.length());
                Pattern pattern = Pattern.compile("^\\d*\\.");
                Matcher matcher = pattern.matcher(name);
                name = matcher.replaceAll("");
            }

            Elements p = bookLists.get(i).select("p");

            int pSize = p.size();
            for (int i_p = 0; i_p < pSize; i_p++) {
                Elements span = p.get(i_p).select("span");
                String pSpanString = span.get(i_p).text();
                authorAndPress = p.get(i_p).text().substring(pSpanString.length());
                Pattern pattern = Pattern.compile("\\著.*");
                Matcher matcher = pattern.matcher(authorAndPress);
                author = matcher.replaceAll("");
                android.util.Log.d("bt", author);
            }
            if (!spanString.matches("(.*)期刊(.*)")) {
                map = new HashMap<String, String>();
                map.put("bookName", name);
                map.put("bookNum", num);
                map.put("bookHref", href);
                map.put("bookAuthor", author);
                bookListMap.add(map);

            }
        }
        return bookListMap;
    }
}
