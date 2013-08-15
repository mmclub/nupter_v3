package org.nupter.nupter.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.*;
import android.view.View;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;
import org.nupter.nupter.activity.ScheduleActivity;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 * Date: 13-8-14
 * Time: 上午10:42
 * To change this template use File | Settings | File Templates.
 */
public class MyTable extends View {
    public static int width;
    public static float height;
    private String[] time = {"8:00-", "8:45", "8:50-", "9:35", "9:50-", "10:35", "10:40-", "11:25", "11:30-", "12:15", "13:45-", "14:30", "14:35-", "15:20", "15:35-", "16:20", "16:25-", "17:10", "18:30-", "19:15", "19:25-", "20:10", "20:20-", "21:05"};

    public MyTable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint paint = new Paint();
        width = canvas.getWidth();
        height = canvas.getHeight();
        for (int i = 1; i <= 12; i++) {
            paint.setColor(Color.GRAY);
            canvas.drawLine(0, height / 12 * i, width, height / 12 * i, paint);
            canvas.drawLine(width / 6 * i, 0, width / 6 * i, height, paint);

        }
        for (int i = 1; i <= 24; i++) {
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            canvas.drawText(time[i - 1], width/6/5, height / 24*i-height/48+height/96, paint);
        }
    }
}
