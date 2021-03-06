package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.DrawableContainer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 * Date: 13-9-3
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleCustomSetting extends Activity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup bottomRg;
    private RadioButton rbBig, rbSmall;
    private View viewBig, viewSmall;
    private ViewPager mViewPager;
    private GridView gridViewBig;
    private GridView gridViewSmall;
    private ArrayList<Integer> backgroundBig = new ArrayList<Integer>();
    private ArrayList<Integer> backgroundSmall = new ArrayList<Integer>();
    private SharedPreferences preferences;
    private int j = 0;
    private int[][] color = new int[][]{{R.drawable.color_1, R.drawable.color_2, R.drawable.color_3, R.drawable.color_4, R.drawable.color_5, R.drawable.color_6},
            {R.drawable.pink_1, R.drawable.pink_2, R.drawable.pink_3, R.drawable.pink_1, R.drawable.pink_2, R.drawable.pink_3},
            {R.drawable.green_1, R.drawable.green_2, R.drawable.green_3, R.drawable.green_1, R.drawable.green_2, R.drawable.green_3},
            {R.drawable.blue_1, R.drawable.blue_2, R.drawable.blue_3, R.drawable.blue_1, R.drawable.blue_2, R.drawable.blue_3},
            {R.drawable.table_yellow, R.drawable.table_blue, R.drawable.table_green, R.drawable.table_orange, R.drawable.table_pink, R.drawable.table_red}};
    private ArrayList<ArrayList<Integer>> colors = new ArrayList<ArrayList<Integer>>();
    private int[] background_big = new int[]{R.drawable.colorbackground, R.drawable.pink_background, R.drawable.green_background, R.drawable.blue_background};
    private int[] background_small = new int[]{R.drawable.color_1, R.drawable.color_2, R.drawable.color_3, R.drawable.color_4, R.drawable.color_5, R.drawable.color_6, R.drawable.pink_1, R.drawable.pink_2, R.drawable.pink_3, R.drawable.green_1, R.drawable.green_2, R.drawable.green_3, R.drawable.blue_1, R.drawable.blue_2, R.drawable.blue_3, R.drawable.table_yellow, R.drawable.table_blue, R.drawable.table_green, R.drawable.table_orange, R.drawable.table_pink, R.drawable.table_red};
    private int[] select_smallBackground = new int[6];
    private ImageView imageView1;
    private int screenWidth;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_customsetting);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("秀出你的Style");
        preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        imageView1.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, 5));
        mViewPager = (ViewPager) findViewById(R.id.viewPagerBackground);
        bottomRg = (RadioGroup) findViewById(R.id.bottomRg);
        rbBig = (RadioButton) findViewById(R.id.rbBig);
        rbSmall = (RadioButton) findViewById(R.id.rbSmall);
        bottomRg.setOnCheckedChangeListener(this);
        initView();
        mViewPager.setAdapter(new MyPagerAdapter(viewBig, viewSmall));
        mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());
    }

    private void initView() {
        for (int i = 0; i < background_big.length; i++)
            backgroundBig.add(background_big[i]);
        backgroundBig.add(R.drawable.add_background);
        viewBig = getLayoutInflater().inflate(R.layout.view_schedule_customsetting_big, null);
        gridViewBig = (GridView) viewBig.findViewById(R.id.bigGridView);
        myAdapter = new MyAdapter(this, backgroundBig, 1);
        gridViewBig.setAdapter(myAdapter);
        gridViewBig.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                if (position > 3 && position < adapterView.getCount() - 1) {
                    new AlertDialog.Builder(ScheduleCustomSetting.this).setTitle("是否删除图片？").setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ArrayList<String> fileNameList = new FileUtils().readFileName("nupter/background");
                                    File file = new File(fileNameList.get(position - 4));
                                    file.delete();
                                    ScheduleCustomSetting.this.recreate();
                                }

                            }).setNegativeButton("取消", null).show();
                }
                return false;
            }
        });
        gridViewBig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < adapterView.getCount() - 1) {
                    Toast.makeText(ScheduleCustomSetting.this, "已选择大背景", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("custom_bigBackground", i);
                    editor.commit();
                } else if (i == adapterView.getCount() - 1) {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 1);
                }
            }
        });
        for (int i = 0; i < background_small.length; i++)
            backgroundSmall.add(background_small[i]);
        viewSmall = getLayoutInflater().inflate(R.layout.view_schedule_customsetting_small, null);
        gridViewSmall = (GridView) viewSmall.findViewById(R.id.smallGridView);
        gridViewSmall.setAdapter(new MyAdapter(this, backgroundSmall, 2));
        gridViewSmall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int m = j++;
                if (j <= 6)
                    Toast.makeText(ScheduleCustomSetting.this, "已选择" + (j > 6 ? 6 : j) + "个小格子,请选择6个小格子", 500).show();
                else
                    Toast.makeText(ScheduleCustomSetting.this, "<继续选择>默认放弃最初的选择", 500).show();
                while (m > 5) {
                    m = m - 6;
                }
                select_smallBackground[m] = background_small[i];
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            startPhotoZoom(data.getData());
        }
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap photo = bundle.getParcelable("data");
                if (new FileUtils().write2SDFromBitmap("nupter/background", System.currentTimeMillis() + ".jpg", photo)) {
                    ScheduleCustomSetting.this.recreate();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 3);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 600);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int flag;
        private ArrayList<Integer> background = new ArrayList<Integer>();
        private ArrayList<String> fileNameList;

        public MyAdapter(Context context, ArrayList<Integer> background, int flag) {
            this.inflater = LayoutInflater.from(context);
            this.background = background;
            this.flag = flag;
            this.fileNameList = new FileUtils().readFileName("nupter/background");
        }

        @Override
        public int getCount() {
            if (flag == 1 && (!fileNameList.isEmpty())) {
                return background.size() + fileNameList.size();
            } else
                return background.size();
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
            if (flag == 1) {
                if (i < (background.size() - 1) || i == (background.size() + fileNameList.size() - 1)) {
                    view = inflater.inflate(R.layout.view_schedule_customsetting_adapter, null);
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) gridViewSmall.getLayoutParams();
                    view.setLayoutParams(new GridView.LayoutParams(linearParams.width, 300));
                    ImageView imageView = (ImageView) view.findViewById(R.id.schedule_background_big);
/*                Bitmap unscaledBitmap = BitmapFactory.decodeResource(getResources(), background.get(i));
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(unscaledBitmap, 280, 420, true);
                imageView.setBackgroundDrawable(new BitmapDrawable(scaledBitmap));*/
                    imageView.setImageResource(background.get(i >= (background.size() - 1) ? i - fileNameList.size() : i));
                    return view;
                } else {
                    view = inflater.inflate(R.layout.view_schedule_customsetting_adapter, null);
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) gridViewSmall.getLayoutParams();
                    view.setLayoutParams(new GridView.LayoutParams(linearParams.width, 300));
                    Bitmap bitmap = BitmapFactory.decodeFile(fileNameList.get(i - background.size() + 1));
                    ImageView imageView = (ImageView) view.findViewById(R.id.schedule_background_big);
                    imageView.setImageBitmap(bitmap);
                    return view;
                }
            } else {
                view = inflater.inflate(R.layout.view_schedule_customsetting_adapter, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.schedule_background_big);
                imageView.setImageResource(background.get(i));
                return view;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.rbBig) {
            mViewPager.setCurrentItem(0);
        } else if (i == R.id.rbSmall) {
            mViewPager.setCurrentItem(1);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        private View view1;
        private View view2;

        public MyPagerAdapter(View view1, View view2) {
            this.view1 = view1;
            this.view2 = view2;
        }

        @Override
        public void destroyItem(View v, int position, Object obj) {
            if (position == 0)
                ((ViewPager) v).removeView(view1);
            else
                ((ViewPager) v).removeView(view2);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == 0) {
                ((ViewPager) container).addView(view1);
                return view1;
            } else {
                ((ViewPager) container).addView(view2, 0);
                return view2;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    private class MyPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (arg2 != 0) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView1.getLayoutParams();
                params.setMargins(arg2 / 2, 0, 0, 0);
                imageView1.setLayoutParams(params);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                rbBig.performClick();
            } else if (position == 1) {
                rbSmall.performClick();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_custombackground, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.custom_background:
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("skin", 5);
                editor.commit();
                Intent intent1 = new Intent("org.nupter.widget.refresh");
                this.sendBroadcast(intent1);
                if (j >= 6) {
                    editor.putInt("color_1", select_smallBackground[0]);
                    editor.putInt("color_2", select_smallBackground[1]);
                    editor.putInt("color_3", select_smallBackground[2]);
                    editor.putInt("color_4", select_smallBackground[3]);
                    editor.putInt("color_5", select_smallBackground[4]);
                    editor.putInt("color_6", select_smallBackground[5]);
                    editor.commit();
                    Intent intent = new Intent(ScheduleCustomSetting.this, ScheduleActivity.class);
                    startActivity(intent);
                    this.finish();
                } else if (j < 6 && j > 0) {
                    Toast.makeText(ScheduleCustomSetting.this, "请选择6个小格子", Toast.LENGTH_SHORT).show();
                } else if (j == 0) {
                    for (int i = 0; i < 5; i++) {
                        ArrayList<Integer> arrayList = new ArrayList<Integer>();
                        for (int j = 0; j <= 5; j++) {
                            arrayList.add(color[i][j]);
                        }
                        colors.add(arrayList);
                    }
                    ArrayList<Integer> arrayList = new ArrayList<Integer>();
                    if (preferences.getInt("color_1", 0) != 0) {
                        arrayList.add(preferences.getInt("color_1", 0));
                        arrayList.add(preferences.getInt("color_2", 0));
                        arrayList.add(preferences.getInt("color_3", 0));
                        arrayList.add(preferences.getInt("color_4", 0));
                        arrayList.add(preferences.getInt("color_5", 0));
                        arrayList.add(preferences.getInt("color_6", 0));
                        colors.add(arrayList);
                    }
                    int n = getIntent().getIntExtra("skin", 0);
                    editor.putInt("color_1", colors.get(n).get(0));
                    editor.putInt("color_2", colors.get(n).get(1));
                    editor.putInt("color_3", colors.get(n).get(2));
                    editor.putInt("color_4", colors.get(n).get(3));
                    editor.putInt("color_5", colors.get(n).get(4));
                    editor.putInt("color_6", colors.get(n).get(5));
                    editor.commit();
                    Intent intent = new Intent(ScheduleCustomSetting.this, ScheduleActivity.class);
                    startActivity(intent);
                    this.finish();
                }
                break;
        }
        return true;
    }
}
