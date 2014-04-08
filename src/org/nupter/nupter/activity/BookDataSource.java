package org.nupter.nupter.activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author WangTao
 */

public class BookDataSource
{
    private static List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();

    public static List<Map<String, String>> getMaps(String searchBook) throws IOException {
        String postUrl = "http://202.119.228.6:8080/opac/openlink.php?doctype=ALL&strSearchType=title&displaypg=20&sort=CATA_DATE&orderby=desc&location=ALL&strText="
                + searchBook + "&submit.x=-858&submit.y=-243&match_flag=forward";

        new AsyncHttpClient().post(postUrl, null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        String name = "", href = "", num = "",author = "", press = "", authorAndPress = "";
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
                                String spanString = span.get(i_h3).text();
                                num = h3.text().substring(spanString.length() + name.length());
                            }

                            Elements p = bookLists.get(i).select("p");

                            int pSize = p.size();
                            for (int i_p = 0; i_p < pSize; i_p++) {
                                Elements span = p.get(i_p).select("span");
                                String spanString = span.get(i_p).text();
                                authorAndPress = p.get(i_p).text().substring(spanString.length());
                                String authorAndPressArray[] = authorAndPress.split(" ");
//                                author = authorAndPressArray[1];
//                                press = authorAndPressArray[2];
                            }

                            Map<String, String> map = new HashMap<String, String>();
                            map.put("bookName", name);
                            map.put("bookNum", num);
                            map.put("bookHref", href);
                            map.put("bookAuther",authorAndPress);
                            listMaps.add(map);

                        }
                        Log.d("jsoup_t", listMaps.toString());
                    }

                });
//        try {
//            Document doc = Jsoup.connect(postUrl)
//                    .timeout(5000)
//                    .get();
//            Elements bookLists = doc.getElementsByClass("list_books");
//            int listSize = bookLists.size();
//            for (int i = 0; i < listSize; i++) {
//                Elements h3 = bookLists.get(i).select("h3");
//                int h3Size = h3.size();
//                Log.d("jsoup_all", h3.toString());
//                for (int ih3 = 0; ih3 < h3Size; ih3++) {
//
//                    Elements a = h3.get(ih3).getElementsByTag("a");
//                    String bookHref = a.attr("href");
//                    String bookName = a.text();
//
//                    Elements span = h3.get(ih3).select("span");
//                    String spanString = span.get(ih3).text();
//                    String bookNum = h3.text().substring(spanString.length() + bookName.length());
//                    Log.d("jsoup_t", bookNum.toString());
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("bookName", "《Android》");
        map1.put("bookAuthor", "Hello");
        map1.put("bookNum", "AK47");
        listMaps.add(map1);
        return listMaps;
    }


}
