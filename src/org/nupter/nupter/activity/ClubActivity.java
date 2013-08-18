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
public class ClubActivity extends Activity{
    private int height;
    private Intent chooseIntent;
    private MyAdapter adapter;
    private GridView mGridView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        height = getWindowManager().getDefaultDisplay().getHeight();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // 实例化GridView
        mGridView = (GridView) findViewById(R.id.gridview);
        adapter=new MyAdapter(this);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(onItemClickListener);
    }


    private class MyAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        public MyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 6;
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
            ImageView imageView=(ImageView)view.findViewById(R.id.ItemImage);
            TextView textView=(TextView)view.findViewById(R.id.ItemText);
            switch (i){
                case 0:
                    imageView.setBackgroundResource(R.drawable.img);
                    textView.setText("南京移动互联网开发者俱乐部");
                    break;
                case 1:
                    imageView.setBackgroundResource(R.drawable.img1);
                    textView.setText("青春南邮");
                    break;
                case 2:
                    imageView.setBackgroundResource(R.drawable.img2);
                    textView.setText("青志联");
                    break;
                case 3:
                    imageView.setBackgroundResource(R.drawable.img3);
                    textView.setText("社团联合会");
                    break;
                case 4:
                    imageView.setBackgroundResource(R.drawable.img4);
                    textView.setText("校科学技术协会");
                    break;
                case 5:
                    imageView.setBackgroundResource(R.drawable.img5);
                    textView.setText("校学生会");
                    break;
            }
            return view;
        }
    }
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

    private GridView.OnItemClickListener onItemClickListener = new GridView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            long page_id = -1;
            switch (position) {
                case 0:
                    page_id = 601415670;
                    break;
                case 1:
                    page_id = 600907477;
                    break;
                case 2:
                    page_id = 601003549;
                    break;
                case 3:
                    page_id = 600889745;
                    break;
                case 4:
                    page_id = 601017224;
                    break;
                case 5:
                    page_id = 600490284;
                    break;
                default:
                    break;
            }
            chooseIntent = new Intent();
            chooseIntent.setClass(ClubActivity.this, ClubDetailActivity.class);
            chooseIntent.putExtra("page_id", page_id);
            startActivity(chooseIntent);
        }
    };
}