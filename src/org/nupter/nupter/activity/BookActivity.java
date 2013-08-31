package org.nupter.nupter.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.nupter.nupter.R;
import org.nupter.nupter.data.BookRecord;
import android.widget.AdapterView.OnItemLongClickListener;
import org.nupter.nupter.utils.NetUtils;

import java.security.PrivateKey;
import java.util.List;


/**
 * 图书馆模块
 *
 * @author WangTao
 */


public class BookActivity extends ListActivity {

    private Button searchBookButton;
    private EditText searchBookEditText;
    List<BookRecord> bookRecords;
    BaseAdapter bookCollectionAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbook);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        searchBookEditText = (EditText) this.findViewById(R.id.searchBookEditText);
        searchBookButton = (Button) this.findViewById(R.id.searchBookBtton);
        searchBookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (NetUtils.isNewworkConnected()) {
                    String searchBookName = searchBookEditText.getText().toString();
                    Intent intent = new Intent(BookActivity.this, BookListActivity.class);
                    intent.putExtra("searchBookName", searchBookName);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(BookActivity.this, "网络没有连接啊！",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }

    public void onStart() {
        super.onStart();
        bookRecords = BookRecord.listAll(BookRecord.class);
        bookCollectionAdapter = new BookCollectionAdapter(this);
        setListAdapter(bookCollectionAdapter);
//        Boolean DataExit = bookRecords.isEmpty();
//        if (DataExit) {
//            Toast toast = Toast.makeText(BookActivity.this, "还没有收藏", Toast.LENGTH_SHORT);
//            toast.show();
//        } else {
//        }
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        String bookName = bookRecords.get(position).name;
        String bookAuthor = bookRecords.get(position).author;
        String bookNum = bookRecords.get(position).bookNum;
        String bookInfo = bookRecords.get(position).bookInfo;
        String bookHref = bookRecords.get(position).href;
        Intent intent = new Intent(BookActivity.this, BookViewActivity.class);
        intent.putExtra(BookListActivity.EXTRA_BOOK_NAME, bookName);
        intent.putExtra(BookListActivity.EXTRA_BOOK_AUTHOR, bookAuthor);
        intent.putExtra(BookListActivity.EXTRA_BOOK_NUM, bookNum);
        intent.putExtra(BookListActivity.EXTRA_BOOK_INFO, bookInfo);
        intent.putExtra(BookListActivity.EXTRA_BOOK_HREF, bookHref);
        startActivity(intent);
    }

    public final class BookViewHolder {
        public TextView bookName;
        public TextView bookAuthor;
        public TextView bookNum;
        public Button delete;
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            BookViewHolder holder = null;
            if (convertView == null) {

                holder = new BookViewHolder();

                convertView = bookInflater.inflate(R.layout.item_collection_book, null);
                holder.bookName = (TextView) convertView.findViewById(R.id.bookName);
                holder.bookAuthor = (TextView) convertView.findViewById(R.id.bookAuthor);
                holder.bookNum = (TextView) convertView.findViewById(R.id.bookNum);
                holder.delete = (Button) convertView.findViewById(R.id.deleteButton);
                convertView.setTag(holder);

            } else {

                holder = (BookViewHolder) convertView.getTag();
            }

            holder.bookName.setText(bookRecords.get(position).name);
            holder.bookAuthor.setText("作者:" + bookRecords.get(position).author);
            holder.bookNum.setText("书号:" + bookRecords.get(position).bookNum);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookActivity.this);
                    alertDialog.setTitle("删除");
                    alertDialog.setMessage("确定删除吗？");
                    alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int p) {
                            String name = bookRecords.get(position).name;
                            String author = bookRecords.get(position).author;
                            List<BookRecord> bookRecord = BookRecord.find(BookRecord.class, "name = ? and author = ?", new String[]{name, author});
                            bookRecord.get(0).delete();
                            Boolean dataDeleted = BookRecord.find(BookRecord.class, "name = ? and author = ?", new String[]{name, author}).isEmpty();
                            if (dataDeleted) {
                                bookRecords = BookRecord.listAll(BookRecord.class);
                                bookCollectionAdapter.notifyDataSetChanged();
                            }

                        }
                    });
                    alertDialog.setNegativeButton("否", null);
                    alertDialog.show();
                }
            });
            return convertView;
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
