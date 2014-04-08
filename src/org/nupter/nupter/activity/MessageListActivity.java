package org.nupter.nupter.activity;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONObject;
import org.nupter.nupter.R;
import org.nupter.nupter.data.MessageRecord;
import org.nupter.nupter.utils.Log;

import java.util.List;


/**
 * 推送消息列表
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */

public class MessageListActivity extends ListActivity {


    List<MessageRecord> messageRecordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        String s = getIntent().getStringExtra("com.parse.Data");
        try {
            Log.d(s);
            JSONObject json = new JSONObject(s);
            MessageRecord record = new MessageRecord(this, ""  , json.getString("alert"));
            record.save();
        }catch (Exception e){

        }
        messageRecordList = MessageRecord.listAll(MessageRecord.class);
        if (messageRecordList.size() == 0) {
            Toast.makeText(this, "暂时没有推送消息", Toast.LENGTH_SHORT);
        }
        setListAdapter(new MyAdapter(this));
        getActionBar().setDisplayHomeAsUpEnabled(true);

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
            return messageRecordList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return messageRecordList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.item_message, null);
                holder.title = (TextView)convertView.findViewById(R.id.message_title);
                holder.content = (TextView)convertView.findViewById(R.id.message_content);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }

            holder.title.setText(messageRecordList.get(position).title);
            holder.content.setText(messageRecordList.get(position).content);



            return convertView;
        }

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

