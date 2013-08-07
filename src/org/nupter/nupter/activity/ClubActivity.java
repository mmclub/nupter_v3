package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.renren.api.RenrenApiClient;

import org.json.simple.JSONArray;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.Log;


/**
 * 七彩社团板块主界面
 */

public class ClubActivity extends Activity {

    RenrenApiClient apiClient;
    JSONArray jsonArray;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.textView);

        new Thread(){
            @Override
            public void run() {
                super.run();
                jsonArray = apiClient.getInstance().getStatusService().getStatuses(601415670,1,10);
                if (jsonArray == null)
                    Log.d("null jsonarray");
                else
                    Log.d(jsonArray.toString());
                ClubActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                          textView.setText(jsonArray.toString());
                    }
                });
            }
        }.start();
    }
}