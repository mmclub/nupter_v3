package org.nupter.nupter.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.nupter.nupter.R;


/**
 * 社团模块一级菜单
 *
 * @author SuDongsheng
 */
public class ClubActivity extends Activity {
    private int height;
    private Intent chooseIntent;
    private MyAdapter adapter;
    private GridView mGridView;
    private String[] clubName = {"南京移动互联网开发者俱乐部", "青春南邮", "校学生会", "社团联合会", "校科学技术协会",
            "青志联", "南京邮电大学校研究生会", "南邮之声", "南邮青年", "南邮大艺团办公室", "南邮红会", "学通社"};
    private int[] clubImage = {R.drawable.img, R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
            R.drawable.img7, R.drawable.img8, R.drawable.img9, R.drawable.img10, R.drawable.img11};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        height = getWindowManager().getDefaultDisplay().getHeight();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // 实例化GridView
        mGridView = (GridView) findViewById(R.id.gridview);
        adapter = new MyAdapter(this);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(onItemClickListener);
    }


    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.view_club, null);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mGridView.getLayoutParams();
            view.setLayoutParams(new GridView.LayoutParams(linearParams.width, height / 5));
            ImageView imageView = (ImageView) view.findViewById(R.id.ItemImage);
            TextView textView = (TextView) view.findViewById(R.id.ItemText);
            imageView.setBackgroundResource(clubImage[i]);
            textView.setText(clubName[i]);
            return view;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent1=new Intent(ClubActivity.this,MainActivity.class);
                startActivity(intent1);
                this.finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private GridView.OnItemClickListener onItemClickListener = new GridView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            chooseIntent = new Intent();
            chooseIntent.setClass(ClubActivity.this, ClubDetailActivity.class);
            chooseIntent.putExtra("position", position);
            startActivity(chooseIntent);
        }
    };
}