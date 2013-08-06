package org.nupter.nupter.activity;

/**
 * Created with IntelliJ IDEA.
 * User: Foci
 * Date: 13-8-6
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
import java.util.jar.Attributes.Name;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import org.nupter.nupter.R;

public class BookViewActivity extends Activity {
    private  String info="这是一本关于ios开发的基本教程，哈哈，你哈哈，我哈哈，大家哈哈哈！(ˇˍˇ）";
    private String[] book_Data;
    private TextView book_name,book_info;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookview);
        book_name = (TextView)this.findViewById(R.id.book_name);
        book_info = (TextView)this.findViewById(R.id.book_info);
        Intent intent = getIntent();
        book_Data = intent.getStringArrayExtra("this_bookData");
        book_name.setText(book_Data[0]);
        book_info.setText(info);
    }
}
