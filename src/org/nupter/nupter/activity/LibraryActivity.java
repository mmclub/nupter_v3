package org.nupter.nupter.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.nupter.nupter.R;



public class LibraryActivity extends Activity {

    private Button searchBookButton;
    private EditText searchBookEditText;
    private Button showCollectionButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbook);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        searchBookEditText = (EditText)this.findViewById(R.id.searchBookEditText);
        searchBookButton = (Button)this.findViewById(R.id.searchBookBtton);
        showCollectionButton = (Button)this.findViewById(R.id.showCollectionButton);
        searchBookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String searchBookName = searchBookEditText.getText().toString();
                Intent intent = new Intent(LibraryActivity.this, BookListActivity.class);
                intent.putExtra("searchBookName", searchBookName);
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
