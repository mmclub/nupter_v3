package org.nupter.nupter.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.nupter.nupter.R;



public class LibraryActivity extends Activity {

    private Button searchBookBtton;
    private EditText searchBookEditText;
    private Button showCollectionButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbook);
        searchBookEditText = (EditText)this.findViewById(R.id.searchBookEditText);
        searchBookBtton = (Button)this.findViewById(R.id.searchBookBtton);
        showCollectionButton = (Button)this.findViewById(R.id.showCollectionButton);
        searchBookBtton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String s_book_name = searchBookEditText.getText().toString();
                Intent intent = new Intent(LibraryActivity.this, BookListActivity.class);
                intent.putExtra("s_book_name", s_book_name);
                startActivity(intent);
            }
        });
        showCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LibraryActivity.this, BookCollectionActivity.class);
                startActivity(intent);

            }
        });

    }
}
