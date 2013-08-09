package org.nupter.nupter.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import org.nupter.nupter.R;


/**
 * @author WangTao
 */

public class BookListActivity extends ListActivity {
    public static final String EXTRA_BOOK_NAME = "bookName";
    public static final String EXTRA_BOOK_AUTHOR = "bookAuthor";
    public static final String EXTRA_BOOK_NUM = "bookNum";
    public static final String EXTRA_BOOK_INFO = "bookInfo";
    private String bookName, bookAuthor, bookNum;
    private List<Map<String, String>> bookListMap = null;
    private SimpleAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        bookListMap = BookDataActivity.getMaps();
        adapter = new SimpleAdapter(BookListActivity.this, bookListMap,
                R.layout.item_activity_book, new String[]{"bookName",
                "bookAuthor", "bookNum"},
                new int[]{R.id.bookName, R.id.bookAuthor,
                        R.id.bookNum});
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        bookName = bookListMap.get(position).get("bookName");
        bookAuthor = bookListMap.get(position).get("bookAuthor");
        bookNum = bookListMap.get(position).get("bookNum");
        //Log.d("BOOK","FDsa");
        Intent intent = new Intent(BookListActivity.this, BookViewActivity.class);
        intent.putExtra(EXTRA_BOOK_NAME, bookName);
        intent.putExtra(EXTRA_BOOK_AUTHOR, bookAuthor);
        intent.putExtra(EXTRA_BOOK_NUM, bookNum);
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
