package org.nupter.nupter.activity;

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
    private String searchBook, bookName, bookAuthor, bookNum,bookHref, postUrl;
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
//        try {
//            bookListMap = BookDataActivity.getMaps(searchBook);
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//        bookSearchListAdapter = new BookSearchListAdapter(BookListActivity.this);
//                       setListAdapter(bookSearchListAdapter);
    }

    public void onStart() {
        super.onStart();
        new AsyncHttpClient().post(postUrl, null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        String name = "", href = "", num = "", author = "", press = "", authorAndPress = "";
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
                                String longName = a.text();
                                name = a.text();
                                String[] longNameArray = longName.split("//.");
                                Log.d("jsoup_all", longNameArray.toString());
//                                name = longNameArray[1];

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
                            map = new HashMap<String, String>();
                            map.put("bookName", name);
                            map.put("bookNum", num);
                            map.put("bookHref", href);
                            map.put("bookAuthor", authorAndPress);
                            bookListMap.add(map);
                        }
                        Log.d("jsoup_t", bookListMap.toString());
                        Boolean resultEmpty = bookListMap.isEmpty();
                        if (resultEmpty) {
                            Toast toast = Toast.makeText(BookListActivity.this, "没有找到匹配的呀！", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            bookSearchListAdapter = new BookSearchListAdapter(BookListActivity.this);
                            setListAdapter(bookSearchListAdapter);
                        }
                    }

                    }

                    );


                }

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
                holder.bookAuthor.setText(bookListMap.get(position).get("bookAuthor"));
                holder.bookNum.setText("书号:" + bookListMap.get(position).get("bookNum"));
                return convertView;
            }
        }

        @Override
        protected void onListItemClick (ListView l, View v,int position, long id){

            super.onListItemClick(l, v, position, id);
            bookName = bookListMap.get(position).get("bookName");
            bookAuthor = bookListMap.get(position).get("bookAuthor");
            bookNum = bookListMap.get(position).get("bookNum");
            bookHref = bookListMap.get(position).get("bookHref");
            //Log.d("BOOK","FDsa");
            Intent intent = new Intent(BookListActivity.this, BookViewActivity.class);
            intent.putExtra(EXTRA_BOOK_NAME, bookName);
            intent.putExtra(EXTRA_BOOK_AUTHOR, bookAuthor);
            intent.putExtra(EXTRA_BOOK_NUM, bookNum);
            intent.putExtra(EXTRA_BOOK_HREF,bookHref);
            startActivity(intent);
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
