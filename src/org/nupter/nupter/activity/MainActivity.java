package org.nupter.nupter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import org.json.*;
import org.nupter.nupter.R;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;




/**
 * 主界面Activity
 *
 * @author Panlei   e-mail: 121531863@qq.com
 *
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
    private ImageButton setIB;
    private ImageButton refreshIB;
    private TextView weatherTV;
    private TextView tempTV;
    private TextView tipsTV;
    private TextView dateTV;
    private TextView weekdayTV;
    private ImageView weatherIcon;
   
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
        setIB =(ImageButton)findViewById(R.id.setIB);
        refreshIB =(ImageButton)findViewById(R.id.refreshIB);
        weatherTV = (TextView)findViewById(R.id.weatherTextView);
        tempTV = (TextView)findViewById(R.id.tempTextView);
        tipsTV = (TextView)findViewById(R.id.tipsTextView);
        dateTV = (TextView)findViewById(R.id.dateTextView);
        weekdayTV = (TextView)findViewById(R.id.weekdayTextView);
        weatherIcon = (ImageView)findViewById(R.id.weatherIcon);
        
        
        libraryIB.setOnClickListener(IBListener);
        scheduleIB.setOnClickListener(IBListener);
        lifeAssistantIB.setOnClickListener(IBListener);
        nuptNewsIB.setOnClickListener(IBListener);
        associationIB.setOnClickListener(IBListener);
        newspaperIB.setOnClickListener(IBListener);
        lostAndFoundIB.setOnClickListener(IBListener);
        schoolMapIB.setOnClickListener(IBListener);
        setIB.setOnClickListener(IBListener);
        refreshIB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
             new   NanjingWeather().getNanjingWeather();
            }
        });
      
        
        new NanjingWeather().getNanjingWeather();
                
        


    }
    
   private OnClickListener IBListener = new OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            switch (v.getId()) {
            case R.id.libraryIB:
                 
                intent.setClass(MainActivity.this, BookActivity.class);
                startActivity(intent);
                break;
            case R.id.schedulIB:
                 
                intent.setClass(MainActivity.this, ClassTableActivity.class);
                startActivity(intent);
                break;
            case R.id.lifeAssistantIB:
                 
                intent.setClass(MainActivity.this, LifeAssistantActivity.class);
                startActivity(intent);
                break;
            case R.id.nuptNewsIB:
                
                intent.setClass(MainActivity.this, NewsActivity.class);
                startActivity(intent);
                break;
            case R.id.associationIB:
                 
                intent.setClass(MainActivity.this, ClubActivity.class);
                startActivity(intent);
                break;
            case R.id.newspaperIB:
                 
                intent.setClass(MainActivity.this, NewspaperActivity.class);
                startActivity(intent);
                break;
            case R.id.lostAndFoundIB:
                 
                intent.setClass(MainActivity.this, LostAndFoundActivity.class);
                startActivity(intent);
                break;
            case R.id.schoolMapIB:
                 
                intent.setClass(MainActivity.this, MapListActivity.class);
                startActivity(intent);
                break;
            case R.id.setIB:

                intent.setClass(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
            }
            
        }
   };
   
   class NanjingWeather{
       
       public static final String NOW_WEATHER_URL_1 = "http://www.weather.com.cn/data/sk/101190101.html";
       public static final String FUTURE_WEATHER_URL = "http://m.weather.com.cn/data/101190101.html";
       public static final String NOW_WEATHER_URL_2 = "http://www.weather.com.cn/data/cityinfo/101190101.html";


       public String getNanjingWeather(){
           AsyncHttpClient client = new AsyncHttpClient();
           client.get(FUTURE_WEATHER_URL, new AsyncHttpResponseHandler() {

               private  int [] strWeather = new int[]{R.drawable.a_0,R.drawable.a_1,R.drawable.a_2 ,
                       R.drawable.a_3,R.drawable.a_4,R.drawable.a_5,R.drawable.a_6,R.drawable.a_7,
                       R.drawable.a_8,R.drawable.a_9,R.drawable.a_10,R.drawable.a_11,R.drawable.a_12,
                       R.drawable.a_13,R.drawable.a_14,R.drawable.a_15,R.drawable.a_16,R.drawable.a_17,
                       R.drawable.a_18,R.drawable.a_19,R.drawable.a_20,R.drawable.a_21,R.drawable.a_22,
                       R.drawable.a_23,R.drawable.a_24,R.drawable.a_25,R.drawable.a_26,R.drawable.a_27,
                       R.drawable.a_28,R.drawable.a_29,R.drawable.a_30,R.drawable.a_31} ;
               @Override
               public void onSuccess(String response) {
                   Log.d("tag",response);

                   try
                   {
                       JSONObject jsonObject = new JSONObject(response).getJSONObject("weatherinfo");
                       String weather = jsonObject.getString("weather1");
                       int weathericon =jsonObject.getInt("img1");
                       String template = jsonObject.getString("temp1");
                       String tips = jsonObject.getString("index48_d");
                       String date = jsonObject.getString("date_y");
                       String weekday = jsonObject.getString("week");
                       
                       weatherTV.setText(weather);
                       tempTV.setText(template);
                       weatherIcon.setImageResource(strWeather[weathericon]);
                       tipsTV.setText(tips);
                       dateTV.setText(date);
                       weekdayTV.setText(weekday);


                   }
                   catch (JSONException e)
                   {
                       e.printStackTrace();
                   }

                   
               }

               @Override
               public void onFailure(Throwable throwable, String s) {

               }
           });

           return "";
       }
       
       
   }
}
   
   
        
    
    
    

