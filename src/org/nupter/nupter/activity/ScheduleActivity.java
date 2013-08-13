package org.nupter.nupter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.nupter.nupter.R;

import java.util.ArrayList;


/**
 * 图书馆模块
 *
 * @author  sudongsheng
 */


public class ScheduleActivity extends Activity {
    private Intent intent;
    private ArrayList<String> list1 = new ArrayList<String>();
    private ArrayList<String> list2 = new ArrayList<String>();
    private ArrayList<String> list3 = new ArrayList<String>();
    private ArrayList<String> list4 = new ArrayList<String>();
    private ArrayList<String> list5 = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        intent=getIntent();
        list1= intent.getStringArrayListExtra("First");
        Log.i("str", list1.toString()+"");
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
