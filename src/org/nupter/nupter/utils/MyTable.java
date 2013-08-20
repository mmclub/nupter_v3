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
    public float offset;
    private String[] time = {"8:00-", "8:45", "8:50-", "9:35", "9:50-", "10:35", "10:40-", "11:25", "11:30-", "12:15", "13:45-", "14:30", "14:35-", "15:20", "15:35-", "16:20", "16:25-", "17:10", "18:30-", "19:15", "19:25-", "20:10", "20:20-", "21:05"};
    private String[] daily={"一","二","三","四","五"};
    public MyTable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint paint = new Paint();
        width = canvas.getWidth();
        offset=dip2px(MyApplication.getAppContext(),20);
        height = canvas.getHeight()-offset;

        paint.setColor(Color.CYAN);
        canvas.drawRect(0,0,width/18,height+offset,paint);
        canvas.drawRect(0,0,width,offset,paint);
        paint.setColor(Color.parseColor("#8ACEEF"));
        canvas.drawRect(width/18,offset,width/6,height+offset,paint);
        for (int i = 1; i <= 12; i++) {
            paint.setColor(Color.WHITE);
            canvas.drawLine(0, height / 12 * (i-1)+offset, width, height / 12 * (i-1)+offset, paint);
            canvas.drawLine(width / 6 * i, 0, width / 6 * i, height+offset, paint);

        }
        canvas.drawLine(width/18,0+offset,width/18,height+offset,paint);
        for (int i = 1; i <= 24; i++) {
//            paint.setColor(Color.parseColor("#8ACEEF"));
            paint.setTextSize(20);
            if(i<=5){
                canvas.drawText(time[i - 1], width/15, height / 24*i-height/48+height/96+offset, paint);
            } else {
            canvas.drawText(time[i - 1], width/6/3, height / 24*i-height/48+height/96+offset, paint);
            }
        }
        for(int i=1;i<=12;i++){
            paint.setColor(Color.BLACK);
            if(i<=9){
            canvas.drawText(i+"",width/64,height/12*i-height/36+offset,paint);
            }else {
                canvas.drawText(i+"",width/240,height/12*i-height/36+offset,paint);
            }
        }
        for(int i=1;i<6;i++){
            canvas.drawText(daily[i-1],width/6*i+width/15,offset/4*3,paint);
        }
    }
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
