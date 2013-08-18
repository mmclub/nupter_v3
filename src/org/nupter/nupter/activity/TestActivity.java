package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import org.nupter.nupter.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 * Date: 13-8-16
 * Time: 下午9:51
 * To change this template use File | Settings | File Templates.
 */
public class TestActivity extends Activity {
    private ListView mListView;
    private Intent intent;
    private String testString;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> msg = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> map;
    private String list[];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        intent = getIntent();
        testString = intent.getStringExtra("testString");
        list = testString.split("\\*");
        mListView = (ListView) findViewById(R.id.mListView);
        putMsg(list);
        adapter = new SimpleAdapter(this, msg, R.layout.view_test,
                new String[]{"test_name", "test_credit", "test_point", "test_score"},
                new int[]{R.id.test_name, R.id.test_credit, R.id.test_point, R.id.test_score});
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dialog dialog = new Dialog(TestActivity.this, R.style.classDialog) {
                    @Override
                    public boolean onTouchEvent(MotionEvent event) {
                        this.dismiss();
                        return true;
                    }
                };
                dialog.setContentView(R.layout.view_test_dialog);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                TextView test_time = (TextView) dialog.getWindow().findViewById(R.id.test_time);
                TextView test_id = (TextView) dialog.getWindow().findViewById(R.id.test_id);
                TextView test_college = (TextView) dialog.getWindow().findViewById(R.id.test_college);
                TextView test_name = (TextView) dialog.getWindow().findViewById(R.id.test_name);
                TextView test_credit = (TextView) dialog.getWindow().findViewById(R.id.testCredit);
                TextView test_point = (TextView) dialog.getWindow().findViewById(R.id.testPoint);
                TextView test_score = (TextView) dialog.getWindow().findViewById(R.id.testScore);
                TextView test_mark = (TextView) dialog.getWindow().findViewById(R.id.test_mark);
                TextView test_makeup_score = (TextView) dialog.getWindow().findViewById(R.id.test_makeup_score);
                TextView test_retake_score = (TextView) dialog.getWindow().findViewById(R.id.test_retake_score);
                TextView test_retake_mark = (TextView) dialog.getWindow().findViewById(R.id.test_retake_mark);

                String element[] = list[i].split("#");
                test_time.setText("学年：" + element[0] + "-" + element[1]);
                test_id.setText("课程代码：" + element[2]);
                test_college.setText("学院名称：" + element[12]);
                test_name.setText("课程名称：" + element[3]);
                test_credit.setText("学分：" + element[6]);
                test_point.setText("绩点：" + element[7]);
                test_score.setText("成绩：" + element[8]);
                test_mark.setText("辅修标记：" + element[9]);
                test_makeup_score.setText("补考成绩：" + element[10]);
                test_retake_score.setText("重修成绩：" + element[11]);
                test_retake_mark.setText("重修标记：" + element[14]);

            }
        });
    }

    public void putMsg(String[] list) {
        for (int i = 0; i < list.length - 1; i++) {
            String element[] = list[i].split("#");
            map = new HashMap<String, String>();
            map.put("test_name", element[3]);
            map.put("test_credit", element[6]);
            map.put("test_point", element[7]);
            map.put("test_score", element[8]);
            msg.add(map);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_schedule, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_login:
                Intent intent = new Intent(TestActivity.this, LoginScheduleActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
