package org.nupter.nupter.activity;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.nupter.nupter.R;
import org.nupter.nupter.data.BookRecord;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 图书的具体视图
 *
 * @author WangTao
 */

public class BookViewActivity extends ListActivity {
    private String info;
    private String name, author, num, href;
    private TextView bookNameTextView, bookAuthorTextView, bookNumTextView, bookInfoTextView;
    private Button collectButton;
    private LibraryTableAdapter libraryTableAadaper;
    private List<Map<String, String>> bookStatusList;
    private Map<String, String> map;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookview);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        collectButton = (Button) this.findViewById(R.id.collectButton);
        bookNameTextView = (TextView) this.findViewById(R.id.bookNameTextView);
        bookAuthorTextView = (TextView) this.findViewById(R.id.bookAuthorTextView);
        bookNumTextView = (TextView) this.findViewById(R.id.bookNumTextView);
        bookInfoTextView = (TextView) this.findViewById(R.id.bookInfoTextview);
        Intent intent = getIntent();
        name = intent.getStringExtra(BookListActivity.EXTRA_BOOK_NAME);
        author = intent.getStringExtra(BookListActivity.EXTRA_BOOK_AUTHOR);
        num = intent.getStringExtra(BookListActivity.EXTRA_BOOK_NUM);
        info = intent.getStringExtra(BookListActivity.EXTRA_BOOK_INFO);
        href = intent.getStringExtra(BookListActivity.EXTRA_BOOK_HREF);
        //当从Jsoup拿来数据时再将info赋值
        bookNameTextView.setText(name);
        bookAuthorTextView.setText("作者：" + author);
        bookNumTextView.setText("书号：" + num);
        bookInfoTextView.setText("【简介】 "+info);

        new AsyncHttpClient().post(href, null,
                new AsyncHttpResponseHandler() {
                    public void onSuccess(String response) {
                        String bookLibrary = "", bookStatus = "";
                        bookStatusList = new ArrayList<Map<String, String>>();
                        Document doc = Jsoup.parse(response);
                        Elements table = doc.getElementsByTag("table");
                        Elements trElements = table.select("tr");
                        int trLength = trElements.size();
                        for (int i = 1; i < trLength; i++) {
                            Elements tdElements = trElements.get(i).select("td");
                            bookLibrary = tdElements.get(4).text();
                            bookStatus = tdElements.get(5).text();

                            map = new HashMap<String, String>();
                            map.put("bookLibrary", bookLibrary);
                            map.put("bookStatus", bookStatus);
                            bookStatusList.add(map);

                        }
                        Log.d("bt", bookStatusList.toString());
                        setListAdapter(new SimpleAdapter(BookViewActivity.this, bookStatusList, R.layout.item_book_table,
                                new String[]{BookListActivity.EXTRA_BOOK_Library, BookListActivity.EXTRA_BOOK_Status},
                                new int[]{R.id.libraryTextView, R.id.bookStatusTextView}));
//                        libraryTableAadaper = new LibraryTableAdapter(BookViewActivity.this);
//                        setListAdapter(libraryTableAadaper);
                    }
                });


        Boolean dataExit = BookRecord.find(BookRecord.class, "name = ? and author = ?", new String[]{name, author}).isEmpty();
        if (!dataExit) {
            collectButton.setBackgroundResource(R.drawable.icon_collected);
        }

        collectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Boolean dataExit = BookRecord.find(BookRecord.class, "name = ? and author = ?", new String[]{name, author}).isEmpty();
                if (dataExit) {
                    BookRecord bookRecord = new BookRecord(BookViewActivity.this, name, author, num, info, href);
                    bookRecord.save();
                    Boolean dataAdded = BookRecord.find(BookRecord.class, "name = ? and author = ?", new String[]{name, author}).isEmpty();
                    if (!dataAdded) {
                        collectButton.setBackgroundResource(R.drawable.icon_collected);
                        Toast toast1 = Toast.makeText(BookViewActivity.this, "收藏成功", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                } else {
                    List<BookRecord> bookRecord = BookRecord.find(BookRecord.class, "name = ? and author = ?", new String[]{name, author});
                    bookRecord.get(0).delete();
                    Boolean dataDeleted = BookRecord.find(BookRecord.class, "name = ? and author = ?", new String[]{name, author}).isEmpty();
                    if (dataDeleted) {
                        collectButton.setBackgroundResource(R.drawable.icon_no_collect);
                        Toast toast1 = Toast.makeText(BookViewActivity.this, "取消成功", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }
            }
        });
    }


    public final class LibraryTableHolder {
        public TextView bookLibrary;
        public TextView bookStatus;
    }

    public class LibraryTableAdapter extends BaseAdapter {
        private LayoutInflater bookInflater;

        public LibraryTableAdapter(Context context) {
            this.bookInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return bookStatusList.size();  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getItem(int ag0) {

            return bookStatusList.get(ag0);  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getItemId(int i) {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LibraryTableHolder holder = null;
            if (convertView == null) {

                holder = new LibraryTableHolder();

                convertView = bookInflater.inflate(R.layout.item_book_table, null);
                holder.bookLibrary = (TextView) convertView.findViewById(R.id.libraryTextView);
                holder.bookStatus = (TextView) convertView.findViewById(R.id.bookStatusTextView);

                convertView.setTag(holder);

            } else {

                holder = (LibraryTableHolder) convertView.getTag();
            }

            holder.bookLibrary.setText(bookStatusList.get(position).get("bookLibrary"));
            holder.bookStatus.setText(bookStatusList.get(position).get("bookStatus"));
            Log.d("bt", bookStatusList.get(position).get("bookLibrary"));
            return convertView;  //To change body of implemented methods use File | Settings | File Templates.
        }
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