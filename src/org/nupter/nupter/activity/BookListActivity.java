package org.nupter.nupter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
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
import org.nupter.nupter.utils.JsoupBookList;


/**
 * @author WangTao
 */

public class BookListActivity extends ListActivity implements AbsListView.OnScrollListener {
    public static final String EXTRA_BOOK_NAME = "bookName";
    public static final String EXTRA_BOOK_AUTHOR = "bookAuthor";
    public static final String EXTRA_BOOK_NUM = "bookNum";
    public static final String EXTRA_BOOK_INFO = "bookInfo";
    public static final String EXTRA_BOOK_HREF = "bookHref";
    public static final String EXTRA_BOOK_Library = "bookLibrary";
    public static final String EXTRA_BOOK_Status = "bookStatus";
    private ProgressDialog progressDialog;
    private String searchBook, bookName, bookAuthor, bookNum, bookHref, postUrl, bookUrl;
    private ListView list;
    private LinearLayout footerLayout;
    private View view;
    private Map<String, String> map;
    private List<Map<String, String>> bookListMap= new ArrayList<Map<String, String>>();;
    private BookSearchListAdapter bookSearchListAdapter;
    private int lastItem;
    private int scrollState;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_search_book);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        list = this.getListView();
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.view_footer, null);
        footerLayout = (LinearLayout) view.findViewById(R.id.footerlayout);
        footerLayout.setVisibility(View.GONE);
        Intent intent = getIntent();
        searchBook = intent.getStringExtra("searchBookName");
        postUrl = "http://202.119.228.6:8080/opac/openlink.php?doctype=ALL&strSearchType=title&displaypg=20&sort=CATA_DATE&orderby=desc&location=ALL&strText="
                + searchBook + "&submit.x=-858&submit.y=-243&match_flag=forward&page=1";
        progressDialog = new ProgressDialog(BookListActivity.this);
        progressDialog.setTitle("努力加载中。。。");
        progressDialog.setMessage("南邮图书馆网站压力很大。。。");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new AsyncHttpClient().post(postUrl, null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        if (!putMsg(response)) {
                            progressDialog.dismiss();
                            Toast toast = Toast.makeText(BookListActivity.this, "试试更准确的书名？", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            bookSearchListAdapter = new BookSearchListAdapter(BookListActivity.this);
                            list.addFooterView(view);
                            list.setAdapter(bookSearchListAdapter);
                            list.setOnScrollListener(BookListActivity.this);
                            progressDialog.dismiss();
                        }
                    }

                }

        );


    }
    private Boolean putMsg(String response){
        JsoupBookList jsoupBookList = new JsoupBookList();
        bookListMap=jsoupBookList.parse(response,bookListMap);
        if(bookListMap.isEmpty()){
            return false;
        }
        return true;
    }
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        this.scrollState = i;
        if (lastItem >= bookSearchListAdapter.getCount() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            /*footerLayout.setVisibility(View.VISIBLE);*/
            postUrl=postUrl.substring(0,postUrl.length()-1)+(bookSearchListAdapter.getCount()/20+1);
            footerLayout.setVisibility(View.VISIBLE);
            new AsyncHttpClient().post(postUrl, null,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            if (!putMsg(response)) {
                                footerLayout.setVisibility(View.INVISIBLE);
                                Toast toast = Toast.makeText(BookListActivity.this, "试试更准确的书名？", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                bookSearchListAdapter.notifyDataSetChanged();
                                footerLayout.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
    }
    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        lastItem = i + i2;
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
