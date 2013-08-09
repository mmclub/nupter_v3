package org.nupter.nupter.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.nupter.nupter.R;
import org.nupter.nupter.data.BookRecord;



/**
 * @author WangTao
 */

public class BookViewActivity extends Activity {
    private String info;
    private String name, author, num;
    private TextView bookNameTextView, bookAuthorTextView, bookNumTextView, bookInfoTextView;
    private Button collectButton;


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
        //当从Jsoup拿来数据时再将info赋值
        info = "这是一本关于ios开发的基本教程，哈哈，你哈哈，我哈哈，大家哈哈哈！(ˇˍˇ）";
        bookNameTextView.setText(name);
        bookAuthorTextView.setText("作者：" + author);
        bookNumTextView.setText("书号：" + num);
        bookInfoTextView.setText(info);


        collectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Boolean dataExit =  BookRecord.find(BookRecord.class, "name = ? and author = ?", new String[]{name, author}).isEmpty();
                if(dataExit){
                    BookRecord bookRecord = new BookRecord(BookViewActivity.this, name, author, num, info);
                    bookRecord.save();
                    Boolean dataAdded = BookRecord.find(BookRecord.class, "name = ? and author = ?", new String[]{name, author}).isEmpty();
                    if(!dataAdded){
                        Toast toast1 =  Toast.makeText(BookViewActivity.this, "收藏好了", Toast.LENGTH_LONG);

                        toast1.show();
                    }
                } else {
                    Toast toast1 = Toast.makeText(BookViewActivity.this, "收藏过了", Toast.LENGTH_LONG);
                    toast1.show();
                }
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
