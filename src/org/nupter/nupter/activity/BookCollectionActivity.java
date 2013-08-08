package org.nupter.nupter.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.nupter.nupter.R;
import org.nupter.nupter.data.BookCollection;
import org.nupter.nupter.utils.Log;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Foci
 * Date: 13-8-6
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
public class BookCollectionActivity extends ListActivity {
    List<BookCollection> bookCollections;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookCollections = BookCollection.listAll(BookCollection.class);
        setListAdapter(new BookCollectionAdapter(this));
        Boolean DataExit = bookCollections.isEmpty();
        if(DataExit){
            Toast toast = Toast.makeText(BookCollectionActivity.this, "还没有收藏", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
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

            return bookCollections.size();  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getItem(int arg0) {
            return bookCollections.get(arg0);  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getItemId(int i) {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            BookViewHolder holder = null;
            if (convertView == null) {

                holder=new BookViewHolder();

                convertView = bookInflater.inflate(R.layout.item_activity_book, null);
                holder.bookName = (TextView)convertView.findViewById(R.id.bookName);
                holder.bookAuthor = (TextView)convertView.findViewById(R.id.bookAuthor);
                holder.bookNum = (TextView)convertView.findViewById(R.id.bookNum);
                convertView.setTag(holder);

            }else {

                holder = (BookViewHolder)convertView.getTag();
            }

            holder.bookName.setText(bookCollections.get(position).name);
            holder.bookAuthor.setText(bookCollections.get(position).author);
            holder.bookNum.setText(bookCollections.get(position).bookNum);
            return convertView;
        }
    }
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        String bookName = bookCollections.get(position).name;
        String bookAuthor = bookCollections.get(position).author;
        String bookNum = bookCollections.get(position).bookNum;
        String bookInfo = bookCollections.get(position).bookInfo;
        Intent intent = new Intent(BookCollectionActivity.this, BookViewActivity.class);
        intent.putExtra(BookListActivity.EXTRA_BOOK_NAME, bookName);
        intent.putExtra(BookListActivity.EXTRA_BOOK_AUTHOR, bookAuthor);
        intent.putExtra(BookListActivity.EXTRA_BOOK_NUM, bookNum);
        intent.putExtra(BookListActivity.EXTRA_BOOK_INFO, bookInfo);
        startActivity(intent);
    }
}