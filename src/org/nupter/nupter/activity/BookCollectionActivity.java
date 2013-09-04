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
import com.umeng.analytics.MobclickAgent;
import org.nupter.nupter.R;
import org.nupter.nupter.data.BookRecord;

import java.util.List;


/**
 *
 * 收藏图书的具体视图
 *
 * @author WangTao
 */
public class BookCollectionActivity extends ListActivity {

    List<BookRecord> bookRecords;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        bookRecords = BookRecord.listAll(BookRecord.class);
        setListAdapter(new BookCollectionAdapter(this));
        Boolean DataExit = bookRecords.isEmpty();
        if(DataExit){
            Toast toast = Toast.makeText(BookCollectionActivity.this, "还没有收藏", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            setListAdapter(new BookCollectionAdapter(this));
        }
    }


    public final class BookViewHolder {
        public TextView bookName;
        public TextView bookAuthor;
        public TextView bookNum;
    }

    public class BookCollectionAdapter extends BaseAdapter {
        private LayoutInflater bookInflater;

        public BookCollectionAdapter(Context context) {
            this.bookInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            return bookRecords.size();  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getItem(int arg0) {
            return bookRecords.get(arg0);  //To change body of implemented methods use File | Settings | File Templates.
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

                convertView = bookInflater.inflate(R.layout.item_activity_book, null);
                holder.bookName = (TextView) convertView.findViewById(R.id.bookName);
                holder.bookAuthor = (TextView) convertView.findViewById(R.id.bookAuthor);
                holder.bookNum = (TextView) convertView.findViewById(R.id.bookNum);
                convertView.setTag(holder);

            } else {

                holder = (BookViewHolder) convertView.getTag();
            }

            holder.bookName.setText(bookRecords.get(position).name);
            holder.bookAuthor.setText(bookRecords.get(position).author);
            holder.bookNum.setText(bookRecords.get(position).bookNum);
            return convertView;
        }
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        String bookName = bookRecords.get(position).name;
        String bookAuthor = bookRecords.get(position).author;
        String bookNum = bookRecords.get(position).bookNum;
        String bookInfo = bookRecords.get(position).bookInfo;
        Intent intent = new Intent(BookCollectionActivity.this, BookViewActivity.class);
        intent.putExtra(BookListActivity.EXTRA_BOOK_NAME, bookName);
        intent.putExtra(BookListActivity.EXTRA_BOOK_AUTHOR, bookAuthor);
        intent.putExtra(BookListActivity.EXTRA_BOOK_NUM, bookNum);
        intent.putExtra(BookListActivity.EXTRA_BOOK_INFO, bookInfo);
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

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}