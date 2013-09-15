package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.nupter.nupter.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 13-9-15
 * Time: 上午9:08
 * To change this template use File | Settings | File Templates.
 */
public class BorrowBookList extends ListActivity {
    private String libBookUrl = "http://202.119.228.6:8080/reader/book_lst.php";
    private String libCookie;
    private ListView libraryBorrowList;
    private Map<String, String> map;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_borrow_book);

        Intent intent = getIntent();
        libCookie = intent.getStringExtra("libCookie");
        new LibraryBookList().start();
        Log.d("lib_co", libCookie);

    }


    public class LibraryBookList extends Thread {
        List<Map<String, String>> libBorrowList = new ArrayList<Map<String, String>>();
        ;

        public void run() {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(libBookUrl);

                httpget.setHeader("Cookie", libCookie);

                HttpResponse response = httpClient.execute(httpget);

                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                String libBookHtml = getHtmlString(inputStream);
                Log.d("lib_book_re2", libBookHtml);

                Document doc = Jsoup.parse(libBookHtml);
                Elements table = doc.getElementsByTag("table");

                Elements trElements = table.select("tr");

                int trSize = trElements.size();
                Log.d("lib_t", trSize + "");
                for (int i = 1; i < trSize; i++) {
                    Elements tdElements = trElements.get(i).select("td");
                    Log.d("lib_t", tdElements.toString());
                    String bookNum = tdElements.get(0).text();
                    Elements blueClass = trElements.get(i).getElementsByClass("blue");
                    String bookName = blueClass.text();
                    Elements deadLineElements = trElements.get(i).select("font");
                    String deadLine = deadLineElements.text();
                    Log.d("lib_t", bookNum + "-" + bookName + "-" + deadLine);
                    if (!bookName.isEmpty()) {
                        map = new HashMap<String, String>();
                        map.put("bookName", bookName);
                        map.put("bookNum", bookNum);
                        map.put("deadLine", "应还日期:" + deadLine);
                        libBorrowList.add(map);
                    }
                    Log.d("libBorrowList", libBorrowList.toString());
                }
                setListAdapter(new SimpleAdapter(BorrowBookList.this, libBorrowList, R.layout.item_borrow_book, new String[]{"bookName", "bookNum", "deadLine"}, new int[]{R.id.borrowBookName, R.id.borrowBookNum, R.id.deadLine}));

            } catch (Exception e) {
                Log.d("lib_book_ex", e.toString());
            }


        }

        public String getHtmlString(InputStream inputStream) {
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            String html = "";
            try {
                int j = 0;
                int bufferSize;
                while ((bufferSize = inputStream.read(buffer)) != -1) {
                    outSteam.write(buffer, 0, bufferSize);
                }
                html = new String(outSteam.toByteArray(), "UTF-8");
                outSteam.close();
            } catch (Exception e) {
                Log.d("lib_book_ex", "None");
            }
            return html;
        }
    }

}
