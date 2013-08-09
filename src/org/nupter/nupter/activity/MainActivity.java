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

import org.json.*;
import org.nupter.nupter.R;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;




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
    private TextView weatherTV;
    private TextView cityTV;
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
        weatherTV = (TextView)findViewById(R.id.weatherTextView);
        cityTV = (TextView)findViewById(R.id.cityTextView);
        weatherIcon = (ImageView)findViewById(R.id.weatherIcon);
        
        
        libraryIB.setOnClickListener(IBListener);
        scheduleIB.setOnClickListener(IBListener);
        lifeAssistantIB.setOnClickListener(IBListener);
        nuptNewsIB.setOnClickListener(IBListener);
        associationIB.setOnClickListener(IBListener);
        newspaperIB.setOnClickListener(IBListener);
        lostAndFoundIB.setOnClickListener(IBListener);
        schoolMapIB.setOnClickListener(IBListener);
      
        
        new NanjingWeather().getNanjingWeather();
                
        


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
                 
                intent.setClass(MainActivity.this, BaiduMapActivity.class);
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
               @Override
               public void onSuccess(String response) {
                   Log.d("tag",response);
                   try
                   {
                       JSONObject jsonObject = new JSONObject(response).getJSONObject("weatherinfo");
                       String city = jsonObject.getString("city");
                       int weathericon =jsonObject.getInt("img1");
                       String template = jsonObject.getString("temp1");
                       
                       weatherTV.setText(template);
                       cityTV.setText(city);
                       
                       switch (weathericon) {
                    case 0:
                        weatherIcon.setImageResource(R.drawable.a_0);
                        break;
                    case 1:
                        weatherIcon.setImageResource(R.drawable.a_1);
                        break;
                    case 2:
                        weatherIcon.setImageResource(R.drawable.a_2);
                        break;
                    case 3:
                        weatherIcon.setImageResource(R.drawable.a_3);
                        break;
                    case 4:
                        weatherIcon.setImageResource(R.drawable.a_4);
                        break;
                    case 5:
                        weatherIcon.setImageResource(R.drawable.a_5);
                        break;
                    case 6:
                        weatherIcon.setImageResource(R.drawable.a_6);
                        break;
                    case 7:
                        weatherIcon.setImageResource(R.drawable.a_7);
                        break;
                    case 8:
                        weatherIcon.setImageResource(R.drawable.a_8);
                        break;
                    case 9:
                        weatherIcon.setImageResource(R.drawable.a_9);
                        break;
                    case 10:
                        weatherIcon.setImageResource(R.drawable.a_10);
                        break;
                    case 11:
                        weatherIcon.setImageResource(R.drawable.a_11);
                        break;
                    case 12:
                        weatherIcon.setImageResource(R.drawable.a_12);
                        break;
                    case 13:
                        weatherIcon.setImageResource(R.drawable.a_13);
                        break;
                    case 14:
                        weatherIcon.setImageResource(R.drawable.a_14);
                        break;
                    case 15:
                        weatherIcon.setImageResource(R.drawable.a_15);
                        break;
                    case 16:
                        weatherIcon.setImageResource(R.drawable.a_16);
                        break;
                    case 17:
                        weatherIcon.setImageResource(R.drawable.a_17);
                        break;
                    case 18:
                        weatherIcon.setImageResource(R.drawable.a_18);
                        break;
                    case 19:
                        weatherIcon.setImageResource(R.drawable.a_19);
                        break;
                    case 20:
                        weatherIcon.setImageResource(R.drawable.a_20);
                        break;
                    case 21:
                        weatherIcon.setImageResource(R.drawable.a_21);
                        break;
                    case 22:
                        weatherIcon.setImageResource(R.drawable.a_22);
                        break;
                    case 23:
                        weatherIcon.setImageResource(R.drawable.a_23);
                        break;
                    case 24:
                        weatherIcon.setImageResource(R.drawable.a_24);
                        break;
                    case 25:
                        weatherIcon.setImageResource(R.drawable.a_25);
                        break;
                    case 26:
                        weatherIcon.setImageResource(R.drawable.a_26);
                        break;
                    case 27:
                        weatherIcon.setImageResource(R.drawable.a_27);
                        break;
                    case 28:
                        weatherIcon.setImageResource(R.drawable.a_28);
                        break;
                    case 29:
                        weatherIcon.setImageResource(R.drawable.a_29);
                        break;
                    case 30:
                        weatherIcon.setImageResource(R.drawable.a_30);
                        break;
                    case 31:
                        weatherIcon.setImageResource(R.drawable.a_31);
                        break;

                    default:
                        break;
                    }
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
   
   
        
    
    
    

