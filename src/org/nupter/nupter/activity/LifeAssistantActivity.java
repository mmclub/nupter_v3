package org.nupter.nupter.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.nupter.nupter.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * 生活小秘书板块主界面
 *
 * @author panlei e-mail:121531863@qq.com
 */

public class LifeAssistantActivity extends Activity {

    private ExpandableAdapter myAdapter;
    // 定义ViewPager对象
    private ViewPager viewPager;

    // 定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;

    // 定义一个ArrayList来存放View
    private ArrayList<View> views;

    //定义各个界面View对象
    private View view1, view2, view3, view4;

    private RadioGroup mRadiogroup;

    private RadioButton btn_0, btn_1, btn_2, btn_3;

    private List<String> SGgroupData;
    private List<List<String>> SGchildrenData;
    private int screenWidth;
    private ImageView imageView;

    private int currIndex = 0;
    // 当前的位置索引值

    @SuppressLint("NewApi")

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifeassistant);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initData();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        imageView = (ImageView) findViewById(R.id.imgView);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 4, 5));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:

                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void loadData(int posion) {
        SGgroupData = new ArrayList<String>();
        SGchildrenData = new ArrayList<List<String>>();
        if (posion == 0) {
            SGgroupData.add("图书馆介绍");
            SGgroupData.add("起居指南");
            SGgroupData.add("通信指南");

            List<String> sub1 = new ArrayList<String>();
            sub1.add(getString(R.string.tushuguanbuchong));
            SGchildrenData.add(sub1);
            List<String> sub2 = new ArrayList<String>();
            sub2.add(getString(R.string.qijuzhinan));
            SGchildrenData.add(sub2);
            List<String> sub3 = new ArrayList<String>();
            sub3.add(getString(R.string.tongxinzhinan));
            SGchildrenData.add(sub3);
        } else if (posion == 1) {
            SGgroupData.add("地铁一号线");
            SGgroupData.add("地铁一号线南延线");
            SGgroupData.add("地铁二号线");
            SGgroupData.add("97路");
            SGgroupData.add("D1路");
            SGgroupData.add("107路");
            SGgroupData.add("165路");
            SGgroupData.add("177路");
            SGgroupData.add("东上路");
            SGgroupData.add("310路");
            SGgroupData.add("108路");
            SGgroupData.add("146路");
            SGgroupData.add("321路");
            SGgroupData.add("322路");
            SGgroupData.add("323路");
            SGgroupData.add("324路");

            List<String> sub1 = new ArrayList<String>();
            sub1.add(getString(R.string.background1));
            SGchildrenData.add(sub1);
            List<String> sub2 = new ArrayList<String>();
            sub2.add(getString(R.string.background1nanyanxian));
            SGchildrenData.add(sub2);
            List<String> sub3 = new ArrayList<String>();
            sub3.add(getString(R.string.background2));
            SGchildrenData.add(sub3);
            List<String> sub4 = new ArrayList<String>();
            sub4.add(getString(R.string.route97));
            SGchildrenData.add(sub4);
            List<String> sub5 = new ArrayList<String>();
            sub5.add(getString(R.string.routeD1));
            SGchildrenData.add(sub5);
            List<String> sub6 = new ArrayList<String>();
            sub6.add(getString(R.string.route107));
            SGchildrenData.add(sub6);
            List<String> sub7 = new ArrayList<String>();
            sub7.add(getString(R.string.route165));
            SGchildrenData.add(sub7);
            List<String> sub8 = new ArrayList<String>();
            sub8.add(getString(R.string.route177));
            SGchildrenData.add(sub8);
            List<String> sub9 = new ArrayList<String>();
            sub9.add(getString(R.string.routeDS));
            SGchildrenData.add(sub9);
            List<String> sub10 = new ArrayList<String>();
            sub10.add(getString(R.string.route310));
            SGchildrenData.add(sub10);
            List<String> sub11 = new ArrayList<String>();
            sub11.add(getString(R.string.route108));
            SGchildrenData.add(sub11);
            List<String> sub12 = new ArrayList<String>();
            sub12.add(getString(R.string.route146));
            SGchildrenData.add(sub12);
            List<String> sub13 = new ArrayList<String>();
            sub13.add(getString(R.string.route321));
            SGchildrenData.add(sub13);
            List<String> sub14 = new ArrayList<String>();
            sub14.add(getString(R.string.route322));
            SGchildrenData.add(sub14);
            List<String> sub15 = new ArrayList<String>();
            sub15.add(getString(R.string.route323));
            SGchildrenData.add(sub15);
            List<String> sub16 = new ArrayList<String>();
            sub16.add(getString(R.string.route324));
            SGchildrenData.add(sub16);




        } else if (posion == 2) {
            SGgroupData.add("邮局");
            SGgroupData.add("派出所");
            SGgroupData.add("超市");
            SGgroupData.add("金融服务");
            SGgroupData.add("医院");
            SGgroupData.add("美容美发");
            SGgroupData.add("药店");
            SGgroupData.add("配钥匙");
            SGgroupData.add("修车点");

            List<String> sub1 = new ArrayList<String>();
            sub1.add(getString(R.string.youju));
            SGchildrenData.add(sub1);
            List<String> sub2 = new ArrayList<String>();
            sub2.add(getString(R.string.paichusuo));
            SGchildrenData.add(sub2);
            List<String> sub3 = new ArrayList<String>();
            sub3.add(getString(R.string.chaoshi));
            SGchildrenData.add(sub3);
            List<String> sub4 = new ArrayList<String>();
            sub4.add(getString(R.string.jinrongfuwu));
            SGchildrenData.add(sub4);
            List<String> sub5 = new ArrayList<String>();
            sub5.add(getString(R.string.yiyuan));
            SGchildrenData.add(sub5);
            List<String> sub6 = new ArrayList<String>();
            sub6.add(getString(R.string.meirongmeifa));
            SGchildrenData.add(sub6);
            List<String> sub7 = new ArrayList<String>();
            sub7.add(getString(R.string.yaodian));
            SGchildrenData.add(sub7);
            List<String> sub8 = new ArrayList<String>();
            sub8.add(getString(R.string.peiyaoshi));
            SGchildrenData.add(sub8);
            List<String> sub9 = new ArrayList<String>();
            sub9.add(getString(R.string.xiuchedian));
            SGchildrenData.add(sub9);

        } else if (posion == 3) {
            SGgroupData.add("三牌楼校区");
            SGgroupData.add("仙林校区");

            List<String> sub1 = new ArrayList<String>();
            sub1.add(getString(R.string.resttitle_sanpailou));
            SGchildrenData.add(sub1);
            List<String> sub2 = new ArrayList<String>();
            sub2.add(getString(R.string.resttitle_xianlin));
            SGchildrenData.add(sub2);

        }
    }


    private void initView() {
        //实例化各个界面的布局对象
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.view_schoolguide, null);
        view2 = mLi.inflate(R.layout.view_busroute, null);
        view3 = mLi.inflate(R.layout.view_localinformation, null);
        view4 = mLi.inflate(R.layout.view_resttime, null);


        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);


        // 实例化ArrayList对象
        views = new ArrayList<View>();

        // 实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views);

        mRadiogroup = (RadioGroup) findViewById(R.id.mRadiogroup);

        btn_0 = (RadioButton) findViewById(R.id.btn_0);
        btn_1 = (RadioButton) findViewById(R.id.btn_1);
        btn_2 = (RadioButton) findViewById(R.id.btn_2);
        btn_3 = (RadioButton) findViewById(R.id.btn_3);


        loadData(0);
        ExpandableListView expandableListView0 = (ExpandableListView)view1.findViewById(R.id.schoolGuideExpend);
        expandableListView0.setGroupIndicator(null);
        myAdapter = new ExpandableAdapter(LifeAssistantActivity.this, SGgroupData, SGchildrenData);
        expandableListView0.setAdapter(myAdapter);

        loadData(1);
        ExpandableListView expandableListView1 = (ExpandableListView)view2.findViewById(R.id.busRouteExpend);
        expandableListView1.setGroupIndicator(null);
        myAdapter = new ExpandableAdapter(LifeAssistantActivity.this, SGgroupData, SGchildrenData);
        expandableListView1.setAdapter(myAdapter);

        loadData(2);
        ExpandableListView expandableListView2 = (ExpandableListView)view3.findViewById(R.id.localInfoExpend);
        expandableListView2.setGroupIndicator(null);
        myAdapter = new ExpandableAdapter(LifeAssistantActivity.this, SGgroupData, SGchildrenData);
        expandableListView2.setAdapter(myAdapter);

        loadData(3);
        ExpandableListView expandableListView3 = (ExpandableListView) view4.findViewById(R.id.restTimeExpend);
        expandableListView3.setGroupIndicator(null);
        ExpandableAdapter myAdapter = new ExpandableAdapter(LifeAssistantActivity.this, SGgroupData, SGchildrenData);
        expandableListView3.setAdapter(myAdapter);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 设置监听
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btn_0) {
                    viewPager.setCurrentItem(0);

                } else if (checkedId == R.id.btn_1) {
                    viewPager.setCurrentItem(1);

                } else if (checkedId == R.id.btn_2) {
                    viewPager.setCurrentItem(2);

                } else if (checkedId == R.id.btn_3) {
                    viewPager.setCurrentItem(3);

                }
            }
        });
        // 设置适配器数据
        viewPager.setAdapter(vpAdapter);

        //将要分页显示的View装入数组中
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
    }

    public class ExpandableAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<String> SGgroupData;
        private List<List<String>> SGchildrenData;

        public ExpandableAdapter(Context context, List<String> SGgroupData, List<List<String>> SGchildrenData) {
            this.context = context;
            this.SGgroupData = SGgroupData;
            this.SGchildrenData = SGchildrenData;
        }


        public Object getChild(int groupPosition, int childPosition) {
            return SGchildrenData.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return SGchildrenData.get(groupPosition).size();
        }

        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            String text = ((String) getChild(groupPosition, childPosition));
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.view_child, null);
            TextView tv = (TextView) linearLayout.findViewById(R.id.childText);
            tv.setText(text);
            return linearLayout;
        }

        // group method stub
        public Object getGroup(int groupPosition) {
            return SGgroupData.get(groupPosition);
        }

        public int getGroupCount() {
            return SGgroupData.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String text = SGgroupData.get(groupPosition);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            RelativeLayout linearLayout = (RelativeLayout) layoutInflater.inflate(R.layout.view_group, null);
            TextView textView = (TextView) linearLayout.findViewById(R.id.groupText);
            textView.setText(text);
            ImageView imageView = (ImageView) linearLayout.findViewById(R.id.arrowImage);
            if (isExpanded) {
                imageView.setImageResource(R.drawable.expend_arrow_clicked);
            } else {
                imageView.setImageResource(R.drawable.expend_arrow_normal);
            }
            return linearLayout;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    btn_0.setChecked(true);
                    break;
                case 1:
                    btn_1.setChecked(true);
                    break;
                case 2:
                    btn_2.setChecked(true);
                    break;
                case 3:
                    btn_3.setChecked(true);
                    break;

            }
            currIndex = position;

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            for(int i=0;i<4;i++){
                if (arg0 == i) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    params.setMargins(screenWidth / 4*i+arg2 / 4, 0, 0, 0);
                    imageView.setLayoutParams(params);
                }
            }
        }
    }


}


class ViewPagerAdapter extends PagerAdapter {

    //界面列表
    private ArrayList<View> views;

    public ViewPagerAdapter(ArrayList<View> views) {
        this.views = views;
    }

    /**
     * 获得当前界面数
     */
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    /**
     * 初始化position位置的界面
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(views.get(position), 0);
        return views.get(position);


    }

    /**
     * 判断是否由对象生成界面
     */
    @Override
    public boolean isViewFromObject(View view, Object arg1) {
        return (view == arg1);
    }

    /**
     * 销毁position位置的界面
     */
    @Override
    public void destroyItem(ViewGroup view, int position, Object arg2) {
        ((ViewPager) view).removeView(views.get(position));
    }


}


        


    
  



    

