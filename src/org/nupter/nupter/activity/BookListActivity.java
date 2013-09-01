package org.nupter.nupter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.nupter.nupter.R;


/**
 * @author WangTao
 */

public class BookListActivity extends ListActivity {
    public static final String EXTRA_BOOK_NAME = "bookName";
    public static final String EXTRA_BOOK_AUTHOR = "bookAuthor";
    public static final String EXTRA_BOOK_NUM = "bookNum";
    public static final String EXTRA_BOOK_INFO = "bookInfo";
    public static final String EXTRA_BOOK_HREF = "bookHref";
    public static final String EXTRA_BOOK_Library = "bookLibrary";
    public static final String EXTRA_BOOK_Status = "bookStatus";
    private ProgressDialog progressDialog;
    private String searchBook, bookName, bookAuthor, bookNum, bookHref, postUrl, bookUrl;
    private Map<String, String> map;
    private List<Map<String, String>> bookListMap;
    private BookSearchListAdapter bookSearchListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_search_book);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        searchBook = intent.getStringExtra("searchBookName");
        postUrl = "http://202.119.228.6:8080/opac/openlink.php?doctype=ALL&strSearchType=title&displaypg=20&sort=CATA_DATE&orderby=desc&location=ALL&strText="
                + searchBook + "&submit.x=-858&submit.y=-243&match_flag=forward";
        progressDialog = new ProgressDialog(BookListActivity.this);
        progressDialog.setTitle("努力加载中。。。");
        progressDialog.setMessage("南邮图书馆网站压力很大。。。");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new AsyncHttpClient().post(postUrl, null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        String name = "", href = "", num = "", author = "", press = "", authorAndPress = "", spanString = "";
                        bookListMap = new ArrayList<Map<String, String>>();

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
                                Log.d("bt", author);
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
                        Log.d("jsoup_t", bookListMap.toString());
                        Boolean resultEmpty = bookListMap.isEmpty();
                        if (resultEmpty) {
                            progressDialog.dismiss();
                            Toast toast = Toast.makeText(BookListActivity.this, "试试更准确的书名？", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            bookSearchListAdapter = new BookSearchListAdapter(BookListActivity.this);
                            setListAdapter(bookSearchListAdapter);
                            progressDialog.dismiss();
                        }
                    }

                }

        );
    }

//    public void onStart() {
//        super.onStart();
//        progressDialog = new ProgressDialog(BookListActivity.this);
//        progressDialog.setTitle("努力加载中。。。");
//        progressDialog.setMessage("南邮图书馆网站压力很大。。。");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//        new AsyncHttpClient().post(postUrl, null,
//                new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(String response) {
//                        String name = "", href = "", num = "", author = "", press = "", authorAndPress = "", spanString = "";
//                        bookListMap = new ArrayList<Map<String, String>>();
//
//                        Document doc = Jsoup.parse(response);
//                        Elements bookLists = doc.getElementsByClass("list_books");
//                        int listSize = bookLists.size();
//                        for (int i = 0; i < listSize; i++) {
//
//                            Elements h3 = bookLists.get(i).select("h3");
//                            int h3Size = h3.size();
//                            for (int i_h3 = 0; i_h3 < h3Size; i_h3++) {
//
//                                Elements a = h3.get(i_h3).getElementsByTag("a");
//                                href = a.attr("href");
//                                name = a.text();
//                                Elements span = h3.get(i_h3).select("span");
//                                spanString = span.get(i_h3).text();
//                                num = h3.text().substring(spanString.length() + name.length());
//                                Pattern pattern = Pattern.compile("^\\d*\\.");
//                                Matcher matcher = pattern.matcher(name);
//                                name = matcher.replaceAll("");
//                            }
//
//                            Elements p = bookLists.get(i).select("p");
//
//                            int pSize = p.size();
//                            for (int i_p = 0; i_p < pSize; i_p++) {
//                                Elements span = p.get(i_p).select("span");
//                                String pSpanString = span.get(i_p).text();
//                                authorAndPress = p.get(i_p).text().substring(pSpanString.length());
//                                Pattern pattern = Pattern.compile("\\著.*");
//                                Matcher matcher = pattern.matcher(authorAndPress);
//                                author = matcher.replaceAll("");
//                                Log.d("bt", author);
//                            }
//                            if (!spanString.matches("(.*)期刊(.*)")) {
//                                map = new HashMap<String, String>();
//                                map.put("bookName", name);
//                                map.put("bookNum", num);
//                                map.put("bookHref", href);
//                                map.put("bookAuthor", author);
//                                bookListMap.add(map);
//                            }
//                        }
//                        Log.d("jsoup_t", bookListMap.toString());
//                        Boolean resultEmpty = bookListMap.isEmpty();
//                        if (resultEmpty) {
//                            progressDialog.dismiss();
//                            Toast toast = Toast.makeText(BookListActivity.this, "试试更准确的书名？", Toast.LENGTH_SHORT);
//                            toast.show();
//                        } else {
//                            bookSearchListAdapter = new BookSearchListAdapter(BookListActivity.this);
//                            setListAdapter(bookSearchListAdapter);
//                            progressDialog.dismiss();
//                        }
//                    }
//
//                }
//
//        );
//
//
//    }

    public final class BookViewHolder {
        public TextView bookName;
        public TextView bookAuthor;
        public TextView bookNum;
    }

    public class BookSearchListAdapter extends BaseAdapter {
        private LayoutInflater bookInflater;

        public BookSearchListAdapter(Context context) {
            this.bookInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            return bookListMap.size();  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getItem(int arg0) {
            return bookListMap.get(arg0);  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getItemId(int i) {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            BookViewHolder holder = null;
            if (convertView == null) {

                holder = new BookViewHolder();

                convertView = bookInflater.inflate(R.layout.item_search_book, null);
                holder.bookName = (TextView) convertView.findViewById(R.id.bookName);
                holder.bookAuthor = (TextView) convertView.findViewById(R.id.bookAuthor);
                holder.bookNum = (TextView) convertView.findViewById(R.id.bookNum);
                convertView.setTag(holder);

            } else {

                holder = (BookViewHolder) convertView.getTag();
            }

            holder.bookName.setText(bookListMap.get(position).get("bookName"));
            holder.bookAuthor.setText("作者:" + bookListMap.get(position).get("bookAuthor"));
            holder.bookNum.setText("书号:" + bookListMap.get(position).get("bookNum"));
            return convertView;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        bookName = bookListMap.get(position).get("bookName");
        bookAuthor = bookListMap.get(position).get("bookAuthor");
        bookNum = bookListMap.get(position).get("bookNum");
        bookHref = bookListMap.get(position).get("bookHref");
        bookUrl = "http://202.119.228.6:8080/opac/" + bookHref;
        Log.d("bookView", bookUrl);

        new AsyncHttpClient().post(bookUrl, null,
                new AsyncHttpResponseHandler() {
                    public void onSuccess(String response) {
                        Intent intent = new Intent(BookListActivity.this, BookViewActivity.class);
                        intent.putExtra(EXTRA_BOOK_NAME, bookName);
                        intent.putExtra(EXTRA_BOOK_AUTHOR, bookAuthor);
                        intent.putExtra(EXTRA_BOOK_NUM, bookNum);
                        intent.putExtra(EXTRA_BOOK_HREF, bookUrl);
                        String bookInfo = "";
                        Document doc = Jsoup.parse(response);
                        Elements list = doc.getElementsByClass("booklist");
                        int listLenght = list.size();
                        for (int i = 0; i < listLenght; i++) {
                            String allString = list.get(i).text();

                            String infoString = list.get(i).getElementsByTag("dt").text();
                            Log.d("bookView", "test" + infoString);
                            if (infoString.matches("(.*)提要文摘附注(.*)")) {
                                bookInfo = list.get(i).getElementsByTag("dd").text();
                                Log.d("bookInfo", bookInfo);
                            }
                        }
                        intent.putExtra(EXTRA_BOOK_INFO, bookInfo);
                        startActivity(intent);
                    }
                });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
