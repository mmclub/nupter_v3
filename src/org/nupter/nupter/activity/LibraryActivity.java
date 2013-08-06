package org.nupter.nupter.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.nupter.nupter.R;

/**
 * Created with IntelliJ IDEA.
 * User: Foci
 * Date: 13-8-5
 * Time: 下午12:56
 * To change this template use File | Settings | File Templates.
 */

public class LibraryActivity extends Activity {

    private Button search_book;
    private EditText input_search_book;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbook);
        input_search_book = (EditText)this.findViewById(R.id.input_search_book);
        search_book = (Button)this.findViewById(R.id.search_book);
        search_book.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String s_book_name  = input_search_book.getText().toString();
                Intent intent = new Intent(LibraryActivity.this, BookListActivity.class);
                intent.putExtra("s_book_name", s_book_name);
                startActivity(intent);
            }
        });
    }
}
