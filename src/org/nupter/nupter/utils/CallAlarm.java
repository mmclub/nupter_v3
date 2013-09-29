package org.nupter.nupter.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.nupter.nupter.activity.ClassAlarmActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-15
 * Time: 上午12:58
 * To change this template use File | Settings | File Templates.
 */
public class CallAlarm extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent i = new Intent(context, ClassAlarmActivity.class);

        Bundle bundleRet = new Bundle();
        bundleRet.putString("STR_CALLER", "");
        i.putExtras(bundleRet);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);


    }
}
