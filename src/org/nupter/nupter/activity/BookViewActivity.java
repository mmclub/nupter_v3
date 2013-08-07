package org.nupter.nupter.activity;

/**
 * Created with IntelliJ IDEA.
 * User: Foci
 * Date: 13-8-6
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.nupter.nupter.R;
import org.nupter.nupter.data.BookCollection;

public class BookViewActivity extends Activity {
    private  String info="这是一本关于ios开发的基本教程，哈哈，你哈哈，我哈哈，大家哈哈哈！(ˇˍˇ）";
    private String name, author, num;
    private TextView bookNameTextView, bookAuthorTextView, bookNumTextView, bookInfoTextView;
    private Button collectButton;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookview);
        collectButton = (Button)this.findViewById(R.id.collectButton);
        bookNameTextView = (TextView)this.findViewById(R.id.book_name);
        bookAuthorTextView = (TextView)this.findViewById(R.id.bookAuthorTextView);
        bookNumTextView = (TextView)this.findViewById(R.id.bookNumTextView);
        bookInfoTextView = (TextView)this.findViewById(R.id.bookInfoTextview);
        Intent intent = getIntent();
        name = intent.getStringExtra(BookListActivity.EXTRA_BOOK_NAME);
        author = intent.getStringExtra(BookListActivity.EXTRA_BOOK_AUTHOR);
        num = intent.getStringExtra(BookListActivity.EXTRA_BOOK_NUM);
        bookNameTextView.setText(name);
        bookAuthorTextView.setText("作者：" + author);
        bookNumTextView.setText("书号：" + num);
        bookInfoTextView.setText(info);


        collectButton.setOnClickListener(new View.OnClickListener() {
            public Context ctx;
            public void onClick(View view) {
                BookCollection bookCollection = new BookCollection(ctx, name, author, num, info);
                bookCollection.save();
            }
        });
    }


}
