package org.nupter.nupter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import org.nupter.nupter.R;
import org.nupter.nupter.utils.Weather;




/**
 * 主界面Activity
 */

public class MainActivity extends Activity {
    
    private ImageButton libraryIB;
    private ImageButton scheduleIB;
    private ImageButton lifeAssistantIB;
    private ImageButton nuptNewsIB;
    private ImageButton associationIB;
    private ImageButton newspaperIB;
    private ImageButton lostAndFoundIB;
    private ImageButton schoolMapIB;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);
        
        libraryIB = (ImageButton)findViewById(R.id.libraryIB);
        scheduleIB = (ImageButton)findViewById(R.id.schedulIB);
        lifeAssistantIB = (ImageButton)findViewById(R.id.lifeAssistantIB);
        nuptNewsIB = (ImageButton)findViewById(R.id.nuptNewsIB);
        associationIB = (ImageButton)findViewById(R.id.associationIB);
        newspaperIB = (ImageButton)findViewById(R.id.newspaperIB);
        lostAndFoundIB = (ImageButton)findViewById(R.id.lostAndFoundIB);
        schoolMapIB = (ImageButton)findViewById(R.id.schoolMapIB);
        
        
        libraryIB.setOnClickListener(IBListener);
        scheduleIB.setOnClickListener(IBListener);
        lifeAssistantIB.setOnClickListener(IBListener);
        nuptNewsIB.setOnClickListener(IBListener);
        associationIB.setOnClickListener(IBListener);
        newspaperIB.setOnClickListener(IBListener);
        lostAndFoundIB.setOnClickListener(IBListener);
        schoolMapIB.setOnClickListener(IBListener);
                
        


    }
    
   private OnClickListener IBListener = new OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            switch (v.getId()) {
            case R.id.libraryIB:
                 
                intent.setClass(MainActivity.this, LibraryActivity.class);
                startActivity(intent);
                break;
            /*case R.id.schedulIB:
                 
                intent2.setClass(MainActivity.this, ScheduleActivity.class);
                startActivity(intent2);
                break;*/
            case R.id.lifeAssistantIB:
                 
                intent.setClass(MainActivity.this, LifeAssistantActivity.class);
                startActivity(intent);
                break;
            /*case R.id.nuptNewsIB:
                
                intent.setClass(MainActivity.this, LibraryActivity.class);
                startActivity(intent);
                break;*/
            /*case R.id.associationIB:
                 
                intent5.setClass(MainActivity.this, LibraryActivity.class);
                startActivity(intent5);
                break;*/
            /*case R.id.newspaperIB:
                 
                intent.setClass(MainActivity.this, LibraryActivity.class);
                startActivity(intent);
                break;*/
            case R.id.lostAndFoundIB:
                 
                intent.setClass(MainActivity.this, LostAndFoundActivity.class);
                startActivity(intent);
                break;
            case R.id.schoolMapIB:
                 
                intent.setClass(MainActivity.this, BaiduMapActivity.class);
                startActivity(intent);
                break;
            default:
                break;
            }
            
        }
   };
}
   
   
        
    
    
    

