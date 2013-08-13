package org.nupter.nupter.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
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
        setContentView(R.layout.list_search_book);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        bookListMap = BookDataActivity.getMaps();
//        adapter = new SimpleAdapter(BookListActivity.this, bookListMap,
//                R.layout.item_collection_book, new String[]{"bookName",
//                "bookAuthor", "bookNum"},
//                new int[]{R.id.bookName, R.id.bookAuthor,
//                        R.id.bookNum});
        setListAdapter(new BookSearchListAdapter(this));
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

                convertView = bookInflater.inflate(R.layout.item_collection_book, null);
                holder.bookName = (TextView) convertView.findViewById(R.id.bookName);
                holder.bookAuthor = (TextView) convertView.findViewById(R.id.bookAuthor);
                holder.bookNum = (TextView) convertView.findViewById(R.id.bookNum);
                convertView.setTag(holder);

            } else {

                holder = (BookViewHolder) convertView.getTag();
            }

            holder.bookName.setText(bookListMap.get(position).get("bookName"));
            holder.bookAuthor.setText("作者："+bookListMap.get(position).get("bookAuthor"));
            holder.bookNum.setText("书号:"+bookListMap.get(position).get("bookNum"));
            return convertView;
        }
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
