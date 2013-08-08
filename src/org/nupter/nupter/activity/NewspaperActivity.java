package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONObject;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.Log;


/**
 * 团委手机报
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */


public class NewspaperActivity extends ListActivity {

    public static final String EXTRA_NEWSPAPER_JSON = "newspaper_json";

    JSONObject json;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
       String rawString =    preferences.getString("json", "null");
       if (! rawString.equals("null")){
           try{
               json = new JSONObject(rawString);
               onUpdateSuccess();
           }catch (Exception e){

           }

       }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(1, 1, 1, "Refresh");
        item.setIcon(android.R.drawable.ic_menu_rotate);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
                update();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }



    public void update(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://dl.dropboxusercontent.com/u/94363727/How%20to%20use%20the%20Public%20folder.txt", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("json", response);
                    editor.commit();
                    json = jsonObject;
                    onUpdateSuccess();


                }catch (Exception e){
                      onUpdateFailure("no json");
                }

            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                 onUpdateFailure("no network");
            }
        });

    }


    public void onUpdateFailure(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onUpdateSuccess(){
        Toast.makeText(this,PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext()).getString("json","null") , Toast.LENGTH_SHORT).show();
        setListAdapter(new MyAdapter(this));


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try{
            Intent intent = new Intent(this, WebviewActivity.class);
            intent.putExtra(WebviewActivity.EXTRA_TITLE, json.getJSONArray("array").getJSONObject(position).getString("title"));
            intent.putExtra(WebviewActivity.EXTRA_URL, json.getJSONArray("array").getJSONObject(position).getString("text"));
            startActivity(intent);
        } catch (Exception e){

        }
    }

    public final class ViewHolder{
        public TextView title;
        public TextView content;
    }


    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;


        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            try{
                return json.getJSONArray("array").length();
            } catch (Exception e){
                 return 0;
            }
        }

        @Override
        public JSONObject getItem(int arg0) {
            // TODO Auto-generated method stub
            try{
                return json.getJSONArray("array").getJSONObject(arg0);
            } catch (Exception e){
                return new JSONObject();
            }
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.item_message, null);
                holder.title = (TextView)convertView.findViewById(R.id.message_content);
                holder.content = (TextView)convertView.findViewById(R.id.message_content);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }

            try{
                JSONObject jsonObject = json.getJSONArray("array").getJSONObject(position);
                holder.title.setText(jsonObject.getString("title"));
                holder.content.setText(jsonObject.getString("title"));

            }catch (Exception e){

            }




            return convertView;
        }

    }


}