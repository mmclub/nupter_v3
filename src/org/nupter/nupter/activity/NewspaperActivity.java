package org.nupter.nupter.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * 团委手机报
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */


public class NewspaperActivity extends Activity {

    private PullToRefreshListView newspaperListview;
    private SimpleAdapter newspaperAdaper;
    private ArrayList<HashMap<String, Object>> newspaperList;
    private HashMap newspaperMap;
    public static final String EXTRA_NEWSPAPER_JSON = "newspaper_json";
    JSONObject newspaperJsonObject;
    JSONArray newspaperJsonArry;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspaper);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        newspaperList = new ArrayList<HashMap<String, Object>>();
        newspaperListview = (PullToRefreshListView)findViewById(R.id.newspaperListview);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        String rawString =    preferences.getString("json", "null");
       if (rawString.equals("null")){
           Toast.makeText(NewspaperActivity.this,"玩命加载中。。。",Toast.LENGTH_SHORT).show();
           update();
       }
        else{
         onUpdateSuccess(rawString);
           newspaperAdaper = new SimpleAdapter(NewspaperActivity.this, newspaperList, R.layout.view_notice_news,
                   new String[]{"Title"},
                   new int[]{R.id.Title});
           newspaperListview.setAdapter(newspaperAdaper);
           try {
             newspaperJsonObject =new JSONObject(rawString);
           } catch (Exception e){}
           }


        /**
         * Add Sound Event Listener
         */
        SharedPreferences sharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        boolean getSoundFlag = sharedPreferences.getBoolean("SoundFlag",true);
        if (getSoundFlag == true){
            SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(NewspaperActivity.this);
            soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
            soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
            soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
            newspaperListview.setOnPullEventListener(soundListener);}
        else {}

        newspaperListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                //To change body of implemented methods use File | Settings | File Templates.
                newspaperList = new ArrayList<HashMap<String, Object>>();
                update();

            }
        });

        newspaperListview.setOnItemClickListener(newspaperonListItemClick);

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }



    public void update(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://nuptapi.nupter.org/newspaper/", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {

                try{
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("json", response);
                    editor.commit();
                    onUpdateSuccess(response);
                    newspaperJsonObject = new JSONObject(response);
                    newspaperAdaper = new SimpleAdapter(NewspaperActivity.this, newspaperList, R.layout.view_newspaper_title,
                            new String[]{"Title"},
                            new int[]{R.id.newsPaperTitle});
                    newspaperListview.setAdapter(newspaperAdaper);
                    newspaperListview.onRefreshComplete();


                }catch (Exception e){
                }

            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                 onUpdateFailure("网络抽风～请稍后再试");
            }
        });

    }


    public void onUpdateFailure(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onUpdateSuccess(String response){
        try {
            newspaperJsonArry = new JSONObject(response).getJSONArray("array");
            for (int i = 0; i < newspaperJsonArry.length(); i++) {
                newspaperJsonObject = newspaperJsonArry.getJSONObject(i);
                Log.i("TAG",newspaperJsonObject.toString());
                newspaperMap = new HashMap<String, Object>();
                newspaperMap.put("Title",
                        newspaperJsonObject.getString("title"));
                newspaperList.add(newspaperMap);


            }
        } catch (Exception e) {

        }
    }
    private AdapterView.OnItemClickListener newspaperonListItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //To change body of implemented methods use File | Settings | File Templates.
        try{
            Intent intent = new Intent(NewspaperActivity.this, WebviewActivity.class);
            intent.putExtra(WebviewActivity.EXTRA_TITLE, newspaperJsonObject.getJSONArray("array").getJSONObject(position - 1).getString("title"));
            intent.putExtra(WebviewActivity.EXTRA_URL, newspaperJsonObject.getJSONArray("array").getJSONObject(position - 1).getString("url"));
            startActivity(intent);
            } catch (Exception e){

            }
         }



    } ;

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
