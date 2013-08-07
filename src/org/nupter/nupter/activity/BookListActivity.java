package org.nupter.nupter.activity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.nupter.nupter.R;


public class BookListActivity extends ListActivity {
    private String s_book_name;
    private String s_url;
    private List<Map<String, String>> s_book_dataList = null;
    private TextView textView;
    private ListView b_list;
    private List<String> arrayData;
    private SimpleAdapter adapter;
    private String[] this_bookData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.r_book_list);
        s_book_dataList = BookDataActivity.getMaps();
        adapter = new SimpleAdapter(BookListActivity.this, s_book_dataList,
                R.layout.item_activity_book, new String[] { "r_book_name",
                "r_book_address", "r_book_num" },
                new int[] { R.id.r_book_name, R.id.r_book_address,
                        R.id.r_book_num });
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        this_bookData = new String[] { "IOS", "自然科学阅览室", "AK47" };
        //List<Map<String, String>> name = get_thisData(position);
        Intent intent = new Intent(BookListActivity.this, BookViewActivity.class);
        intent.putExtra("this_bookData", this_bookData);
        startActivity(intent);
    }
}
