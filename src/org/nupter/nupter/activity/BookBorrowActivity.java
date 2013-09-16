package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.nupter.nupter.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 13-9-15
 * Time: 上午9:08
 * To change this template use File | Settings | File Templates.
 */
public class BookBorrowActivity extends Activity {
    private String libBookUrl = "http://202.119.228.6:8080/reader/book_lst.php";
    private String renewUrl;
    private String libCookie, cookieValue;
    private ListView libraryBorrowListView;
    private Map<String, String> map;
    private List<Map<String, String>> libBorrowList;
    private BookBorrowAdapter bookBorrowAdapter;
    private ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_borrow_book);
        libraryBorrowListView = (ListView) this.findViewById(R.id.libraryBorrowListView);
        Intent intent = getIntent();
        libCookie = intent.getStringExtra("libCookie");
        cookieValue = intent.getStringExtra("cookieValue");

        progressDialog = new ProgressDialog(BookBorrowActivity.this);
        progressDialog.setTitle("努力加载中。。。");
        progressDialog.setMessage("南邮图书馆压力很大。。。");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Log.d("ays_co", cookieValue);
//        new LibraryBookList().start();
        AsyncHttpClient libBorrowClient = new AsyncHttpClient();
        PersistentCookieStore libCookieStore = new PersistentCookieStore(this);
        BasicClientCookie libClientCookie = new BasicClientCookie("PHPSESSID", cookieValue);

        libClientCookie.setDomain("202.119.228.6");
        libClientCookie.setPath("/");
        libCookieStore.addCookie(libClientCookie);
        libBorrowClient.setCookieStore(libCookieStore);

        libBorrowClient.post(libBookUrl, null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        libBorrowList = new LinkedList<Map<String, String>>();
                        Log.d("asy_re", response);
                        Document doc = Jsoup.parse(response);
                        Elements table = doc.getElementsByTag("table");
                        if (table.isEmpty()) {
                            new AlertDialog.Builder(BookBorrowActivity.this)
                                    .setTitle("续借反馈")
                                    .setMessage("还没有借书哦")
                                    .setNegativeButton(
                                            "确定", null).show();
                        } else {
                            Elements trElements = table.select("tr");

                            int trSize = trElements.size();
                            Log.d("lib_t", trSize + "");
                            for (int i = 1; i < trSize; i++) {
                                Elements tdElements = trElements.get(i).select("td");
                                Log.d("lib_t", tdElements.toString());
                                String bookNum = tdElements.get(0).text();
                                String bookAuthor = tdElements.get(2).text();
                                Elements blueClass = trElements.get(i).getElementsByClass("blue");
                                String bookName = blueClass.text();
                                Elements deadLineElements = trElements.get(i).select("font");
                                String deadLine = deadLineElements.text();
                                Log.d("lib_t", bookNum + "-" + bookName + "-" + deadLine);
                                if (!bookName.isEmpty()) {
                                    map = new HashMap<String, String>();
                                    map.put("bookName", bookName);
                                    map.put("bookAuthor", bookAuthor);
                                    map.put("bookNum", bookNum);
                                    map.put("deadLine", "应还日期:" + deadLine);
                                    libBorrowList.add(map);
                                }
                            }
                            bookBorrowAdapter = new BookBorrowAdapter(BookBorrowActivity.this);
                            libraryBorrowListView.setAdapter(bookBorrowAdapter);
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public class BookBorrowHolder {
        TextView bookName, bookAuthor, deadLine;
        Button renew;
    }

    public class BookBorrowAdapter extends BaseAdapter {
        private LayoutInflater bookInflater;

        public BookBorrowAdapter(Context context) {
            this.bookInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return libBorrowList.size();  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getItem(int position) {
            return libBorrowList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getItemId(int position) {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            BookBorrowHolder holder = null;

            if (convertView == null) {

                holder = new BookBorrowHolder();

                convertView = bookInflater.inflate(R.layout.item_borrow_book, null);

                holder.renew = (Button) convertView.findViewById(R.id.renewButton);
                holder.bookName = (TextView) convertView.findViewById(R.id.borrowBookName);
                holder.bookAuthor = (TextView) convertView.findViewById(R.id.borrowBookAuthor);
                holder.deadLine = (TextView) convertView.findViewById(R.id.deadLine);
                convertView.setTag(holder);

            } else {

                holder = (BookBorrowHolder) convertView.getTag();
            }
            holder.bookName.setText(libBorrowList.get(position).get("bookName"));
            holder.bookAuthor.setText(libBorrowList.get(position).get("bookAuthor"));
            holder.deadLine.setText(libBorrowList.get(position).get("deadLine"));
            holder.renew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bookNum = libBorrowList.get(position).get("bookNum");
                    renewUrl = "http://202.119.228.6:8080/reader/ajax_renew.php?bar_code="+bookNum;
                    new AsyncHttpClient().post(renewUrl,null,new AsyncHttpResponseHandler(){
                        public void onSuccess(String response) {
                            Log.d("renew",response);
                            Document doc = Jsoup.parse(response);
                            Elements fontElements = doc.getElementsByTag("font");
                            String result = fontElements.text();
                            new AlertDialog.Builder(BookBorrowActivity.this)
                                    .setTitle("续借反馈")
                                    .setMessage(result)
                                    .setNegativeButton(
                                    "确定", null).show();


                        }

                    });
                    Log.d("lib_id", bookNum);
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            return convertView;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

}


//    public class LibraryBookList extends Thread {
//        List<Map<String, String>> libBorrowList = new ArrayList<Map<String, String>>();
//        SimpleAdapter simpleAdapter;
//        BookBorrowAdapter bookBorrowAdapter;
//
//        public void run() {
//            try {
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpGet httpget = new HttpGet(libBookUrl);
//
//                httpget.setHeader("Cookie", libCookie);
//
//                HttpResponse response = httpClient.execute(httpget);
//
//                HttpEntity entity = response.getEntity();
//                InputStream inputStream = entity.getContent();
//                String libBookHtml = getHtmlString(inputStream);
//                Log.d("lib_book_re2", libBookHtml);
//
//                Document doc = Jsoup.parse(libBookHtml);
//                Elements table = doc.getElementsByTag("table");
//
//                Elements trElements = table.select("tr");
//
//                int trSize = trElements.size();
//                Log.d("lib_t", trSize + "");
//                for (int i = 1; i < trSize; i++) {
//                    Elements tdElements = trElements.get(i).select("td");
//                    Log.d("lib_t", tdElements.toString());
//                    String bookNum = tdElements.get(0).text();
//                    Elements blueClass = trElements.get(i).getElementsByClass("blue");
//                    String bookName = blueClass.text();
//                    Elements deadLineElements = trElements.get(i).select("font");
//                    String deadLine = deadLineElements.text();
//                    Log.d("lib_t", bookNum + "-" + bookName + "-" + deadLine);
//                    if (!bookName.isEmpty()) {
//                        map = new HashMap<String, String>();
//                        map.put("bookName", bookName);
//                        map.put("bookNum", bookNum);
//                        map.put("deadLine", "应还日期:" + deadLine);
//                        libBorrowList.add(map);
//                    }
//                }
//                Log.d("libBorrowList", libBorrowList.toString());
//                bookBorrowAdapter = new BookBorrowAdapter(BookBorrowActivity.this);
//                libraryBorrowListView.setAdapter(bookBorrowAdapter);
////                simpleAdapter = new SimpleAdapter(BookBorrowActivity.this, libBorrowList,
////                        R.layout.item_borrow_book, new String[]{"bookName", "bookNum", "deadLine"},
////                        new int[]{R.id.borrowBookName, R.id.borrowBookNum, R.id.deadLine});
////                getListView().setAdapter(simpleAdapter);
////                simpleAdapter.notifyDataSetChanged();
//
//
//            } catch (Exception e) {
//                Log.d("lib_book_ex", e.toString());
//            }
//
//
//        }
//
////        public final class BookBorrowHolder {
////            TextView bookName, bookNum, deadLine;
////            Button renew;
////
////        }
//
////        public class BookBorrowAdapter extends BaseAdapter {
////            private LayoutInflater bookInflater;
////
////            public BookBorrowAdapter(Context context) {
////                this.bookInflater = LayoutInflater.from(context);
////            }
////
////            @Override
////            public int getCount() {
////                return libBorrowList.size();  //To change body of implemented methods use File | Settings | File Templates.
////            }
////
////            @Override
////            public Object getItem(int position) {
////                return libBorrowList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
////            }
////
////            @Override
////            public long getItemId(int position) {
////                return 0;  //To change body of implemented methods use File | Settings | File Templates.
////            }
////
////            @Override
////            public View getView(final int position, View convertView, ViewGroup parent) {
////                BookBorrowHolder holder = null;
////
////                if (convertView == null) {
////
////                    holder = new BookBorrowHolder();
////
////                    convertView = bookInflater.inflate(R.layout.item_borrow_book, null);
////
////                    holder.renew = (Button) convertView.findViewById(R.id.renewButton);
////                    holder.bookName = (TextView) convertView.findViewById(R.id.borrowBookName);
////                    holder.bookNum = (TextView) convertView.findViewById(R.id.borrowBookNum);
////                    holder.deadLine = (TextView) convertView.findViewById(R.id.deadLine);
////                    convertView.setTag(holder);
////
////                } else {
////
////                    holder = (BookBorrowHolder) convertView.getTag();
////                }
////                Log.d("lib_id", libBorrowList.get(position).get("bookName"));
////                holder.bookName.setText(libBorrowList.get(position).get("bookName"));
////                holder.bookNum.setText(libBorrowList.get(position).get("bookNum"));
////                holder.deadLine.setText(libBorrowList.get(position).get("deadLine"));
////                holder.renew.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        String bookNum = libBorrowList.get(position).get("bookNum");
////                        Log.d("lib_id", bookNum);
////                        //To change body of implemented methods use File | Settings | File Templates.
////                    }
////                });
////                return convertView;  //To change body of implemented methods use File | Settings | File Templates.
////            }
////        }
//
//        public String getHtmlString(InputStream inputStream) {
//            byte[] buffer = new byte[1024];
//            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//            String html = "";
//            try {
//                int j = 0;
//                int bufferSize;
//                while ((bufferSize = inputStream.read(buffer)) != -1) {
//                    outSteam.write(buffer, 0, bufferSize);
//                }
//                html = new String(outSteam.toByteArray(), "UTF-8");
//                outSteam.close();
//            } catch (Exception e) {
//                Log.d("lib_book_ex", "None");
//            }
//            return html;
//        }
//    }


