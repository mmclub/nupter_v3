package org.nupter.nupter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.*;
import android.util.Log;
import android.view.View;
import org.nupter.nupter.MyApplication;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 * Date: 13-8-14
 * Time: 上午10:42
 * To change this template use File | Settings | File Templates.
 */
public class MyTable extends View {
    private int width;
    private float height;
    private float offset;
    private int weekday;
    private int hour;
    private int minute;
    private int whichClass = 13;
    private int skin;
    private String[] time = {"8:00-", "8:45", "8:50-", "9:35", "9:50-", "10:35", "10:40-", "11:25", "11:30-", "12:15", "13:45-", "14:30", "14:35-", "15:20", "15:35-", "16:20", "16:25-", "17:10", "18:30-", "19:15", "19:25-", "20:10", "20:20-", "21:05"};
    private String[] daily = {"一", "二", "三", "四", "五"};
    private String[][] color = new String[][]{{"#D3D3D3", "#DDD2D2", "#94A0A4"}, {"#FB98ED", "#BDB4CD", "#A86BB2"}, {"#57DE54", "#9FE4B5", "#18AD15"}, {"#93DEFD", "#ABD9F2", "#5CA7BA"}, {"#94E1FF", "#AFD8EE", "#5CA7BA"}};

    public MyTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        long time = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        switch (hour) {
            case 7:
                whichClass = 1;
                break;
            case 8:
                if (minute <= 45) {
                    whichClass = 1;
                } else {
                    whichClass = 2;
                }
                break;
            case 9:
                if (minute <= 35) {
                    whichClass = 2;
                } else {
                    whichClass = 3;
                }
                break;
            case 10:
                if (minute <= 35) {
                    whichClass = 3;
                } else {
                    whichClass = 4;
                }
                break;
            case 11:
                if (minute <= 25) {
                    whichClass = 4;
                } else {
                    whichClass = 5;
                }
                break;
            case 12:
                if (minute <= 15) {
                    whichClass = 5;
                } else {
                    whichClass = 6;
                }
                break;
            case 13:
                whichClass = 6;
                break;
            case 14:
                if (minute <= 30) {
                    whichClass = 6;
                } else {
                    whichClass = 7;
                }
                break;
            case 15:
                if (minute <= 20) {
                    whichClass = 7;
                } else {
                    whichClass = 8;
                }
                break;
            case 16:
                if (minute <= 20) {
                    whichClass = 8;
                } else {
                    whichClass = 9;
                }
                break;
            case 17:
                if (minute <= 10) {
                    whichClass = 9;
                } else {
                    whichClass = 10;
                }
                break;
            case 18:
                whichClass = 10;
                break;
            case 19:
                if (minute <= 15) {
                    whichClass = 10;
                } else {
                    whichClass = 11;
                }
                break;
            case 20:
                if (minute <= 10) {
                    whichClass = 11;
                } else {
                    whichClass = 12;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        width = canvas.getWidth();
        offset = dip2px(MyApplication.getAppContext(), 20);
        height = canvas.getHeight() - offset;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        skin = preferences.getInt("skin", 0);
        //设置边框栏的背景色
        paint.setColor(Color.parseColor(color[skin][0]));
        canvas.drawRect(0, 0, width / 18, height + offset, paint);
        canvas.drawRect(0, 0, width, offset, paint);
        paint.setColor(Color.parseColor(color[skin][1]));
        canvas.drawRect(width / 18, offset, width / 6, height + offset, paint);
        //当天背景
        paint.setColor(Color.parseColor(color[skin][2]));
        if (weekday != 0) {
            canvas.drawRect(width / 6 * weekday, 0, width / 6 * (weekday + 1), offset, paint);
        }
        canvas.drawRect(0, offset + height / 12 * (whichClass - 1), width / 18, height / 12 * whichClass + offset, paint);
        //画表格线
        for (int i = 1; i <= 12; i++) {
            paint.setColor(Color.WHITE);
            canvas.drawLine(0, height / 12 * (i - 1) + offset, width, height / 12 * (i - 1) + offset, paint);
            canvas.drawLine(width / 6 * i, 0, width / 6 * i, height + offset, paint);
        }
        canvas.drawLine(width / 18, 0 + offset, width / 18, height + offset, paint);
        //时间
        for (int i = 1; i <= 24; i++) {
            paint.setColor(Color.parseColor("#725334"));
            paint.setTextSize(20);
            if (i <= 5) {
                canvas.drawText(time[i - 1], width / 15, height / 24 * i - height / 48 + height / 96 + offset, paint);
            } else {
                canvas.drawText(time[i - 1], width / 6 / 3, height / 24 * i - height / 48 + height / 96 + offset, paint);
            }
        }
        //序号
        for (int i = 1; i <= 12; i++) {
            paint.setColor(Color.BLACK);
            if (i <= 9) {
                canvas.drawText(i + "", width / 64, height / 12 * i - height / 36 + offset, paint);
            } else {
                canvas.drawText(i + "", width / 240, height / 12 * i - height / 36 + offset, paint);
            }
        }
        //星期几
        for (int i = 1; i < 6; i++) {
            canvas.drawText(daily[i - 1], width / 6 * i + width / 15, offset / 4 * 3, paint);
        }
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
