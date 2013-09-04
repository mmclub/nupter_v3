package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import org.nupter.nupter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 * Date: 13-8-16
 * Time: 下午9:51
 * To change this template use File | Settings | File Templates.
 */
public class TestActivity extends FragmentActivity {
    private ListView mListView;
    private String testString;
    private String list[];
    private ArrayList<ArrayList<ArrayList<String>>> lists = new ArrayList<ArrayList<ArrayList<String>>>();
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private List<String> titleList = new ArrayList<String>();
    private TextView average;
    //    private TextView total_number;
    private TextView total_point;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        testString = getIntent().getStringExtra("testString");
        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        if (!testString.equals("null")) {
            list = testString.split("\\$");
            String first_element[] = list[1].split("&");
            String title = first_element[0];
            String term = first_element[1];
            // Log.i("TAG", "title:" + title + "第一学期");
            titleList.add(title + "第一学期");
            ArrayList<ArrayList<String>> arrayLists = new ArrayList<ArrayList<String>>();
            int m = 0;
            for (int i = 0; i < list.length - 1; i++) {
                String element[] = list[i].split("&");
                ArrayList<String> arrayList = new ArrayList<String>();
                for (int j = 0; j < element.length; j++)
                    arrayList.add(element[j]);
                if ((i != 0) && (i != 1) && ((!title.equals(element[0])) || (!term.equals(element[1])))) {
                    lists.add(arrayLists);
                    arrayLists = new ArrayList<ArrayList<String>>();

                    //除大一第一学期外，其余每学期加上第一列，即课程名称，学分，绩点，成绩
                    String element2[] = list[0].split("&");
                    ArrayList<String> arrayList2 = new ArrayList<String>();
                    for (int k = 0; k < element2.length; k++)
                        arrayList2.add(element2[k]);
                    arrayLists.add(arrayList2);

                    titleList.add(element[0] + "\n" + (m++ % 2 == 0 ? "第二学期" : "第一学期"));
                }
                arrayLists.add(arrayList);
                title = element[0];
                term = element[1];
            }
            lists.add(arrayLists);
            average = (TextView) findViewById(R.id.average);
            total_point = (TextView) findViewById(R.id.total_point);
/*            total_number=(TextView)findViewById(R.id.total_number);
            total_number.setText(list[list.length-1].split("&")[0]);*/
            average.setText(list[list.length - 1].split("&")[1]);
            total_point.setText(list[list.length - 1].split("&")[2]);
            for (int i = 0; i < lists.size(); i++) {
     //           Log.i("str", lists.get(i).toString());
                fragmentList.add(new scoreFragment(lists.get(i)));
            }
            vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));
            PagerTabStrip pts = (PagerTabStrip) findViewById(R.id.pageTab);
            pts.setTextSpacing(10);
            //     pts.setTabIndicatorColor(getResources().getColor(android.R.color.holo_blue_bright));
            pts.setTextColor(Color.BLUE);
            pts.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            pts.setDrawFullUnderline(true);
        } else {

        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private List<String> titleList;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (titleList.size() > position) ? titleList.get(position) : "";
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

    class scoreFragment extends Fragment {
        private SimpleAdapter adapter;
        private ArrayList<HashMap<String, String>> msg = new ArrayList<HashMap<String, String>>();
        private HashMap<String, String> map;
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<ArrayList<String>>();

        public scoreFragment(ArrayList<ArrayList<String>> arrayLists) {
            this.arrayLists = arrayLists;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_test_fragment, container, false);
            mListView = (ListView) v.findViewById(R.id.mListView);
            for (int i = 0; i < arrayLists.size(); i++) {
                map = new HashMap<String, String>();
                map.put("testName", arrayLists.get(i).get(3));
                map.put("testCredit", arrayLists.get(i).get(6));
                map.put("testPoint", arrayLists.get(i).get(7));
                map.put("testScore", arrayLists.get(i).get(8));
                msg.add(map);
            }
            adapter = new SimpleAdapter(TestActivity.this, msg, R.layout.view_test,
                    new String[]{"testName", "testCredit", "testPoint", "testScore"},
                    new int[]{R.id.testName, R.id.testCredit, R.id.testPoint, R.id.testScore});
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
                    if (i > 0) {
                        dialog.setContentView(R.layout.view_test_dialog);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        TextView test_time = (TextView) dialog.getWindow().findViewById(R.id.test_time);
                        TextView test_id = (TextView) dialog.getWindow().findViewById(R.id.test_id);
                        TextView test_college = (TextView) dialog.getWindow().findViewById(R.id.test_college);
                        TextView test_name = (TextView) dialog.getWindow().findViewById(R.id.test_name);
                        TextView test_credit = (TextView) dialog.getWindow().findViewById(R.id.test_credit);
                        TextView test_point = (TextView) dialog.getWindow().findViewById(R.id.test_point);
                        TextView test_score = (TextView) dialog.getWindow().findViewById(R.id.test_score);
                        TextView test_mark = (TextView) dialog.getWindow().findViewById(R.id.test_mark);
                        TextView test_makeup_score = (TextView) dialog.getWindow().findViewById(R.id.test_makeup_score);
                        TextView test_retake_score = (TextView) dialog.getWindow().findViewById(R.id.test_retake_score);
                        TextView test_retake_mark = (TextView) dialog.getWindow().findViewById(R.id.test_retake_mark);

                        test_time.setText("学年：" + arrayLists.get(i).get(0));
                        test_id.setText("课程代码：" + arrayLists.get(i).get(2));
                        test_college.setText("学院名称：" + arrayLists.get(i).get(12));
                        test_name.setText("课程名称：" + arrayLists.get(i).get(3));
                        test_credit.setText("学分：" + arrayLists.get(i).get(6));
                        test_point.setText("绩点：" + arrayLists.get(i).get(7));
                        test_score.setText("成绩：" + arrayLists.get(i).get(8));
                        test_mark.setText("辅修标记：" + arrayLists.get(i).get(9));
                        test_makeup_score.setText("补考成绩：" + arrayLists.get(i).get(10));
                        test_retake_score.setText("重修成绩：" + arrayLists.get(i).get(11));
                        test_retake_mark.setText("重修标记：" + arrayLists.get(i).get(14));
                    }
                }
            });
            msg = new ArrayList<HashMap<String, String>>();
            return v;
        }
    }
/*            if(element[8].startsWith("6")||element[8].startsWith("7")||element[8].startsWith("8")||element[8].startsWith("9")||element[8].equals("100")||element[8].equals("合格")||element[8].equals("及格")||element[8].equals("中等")||element[8].equals("良好")||element[8].equals("优秀")){
                testScore.setTextColor(Color.GREEN);
                Log.i("str", "success");
            }else {
                testScore.setTextColor(Color.RED);
            }

        }
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_test, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.to_login:
                Intent intent1 = new Intent(TestActivity.this, LoginActivity.class);
                intent1.putExtra("JumpTo", "Test");
                startActivity(intent1);
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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
