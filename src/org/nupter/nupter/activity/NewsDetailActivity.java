package org.nupter.nupter.activity;

import org.nupter.nupter.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NewsDetailActivity extends Activity{
    
    private TextView contentTV;
    private Intent contentIntent;
    private String getContent;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_content);
        contentIntent = getIntent();
        getContent =  contentIntent.getStringExtra("content");
        contentTV = (TextView)findViewById(R.id.contentTV);
        contentTV.setText(getContent);
        
    }

}
