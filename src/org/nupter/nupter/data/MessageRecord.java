package org.nupter.nupter.data;

import android.content.Context;
import com.orm.SugarRecord;

/**
 * 推送消息的Model类
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */
public class MessageRecord extends SugarRecord<MessageRecord> {


    public  String title;
    public  String content;

    public MessageRecord(Context context) {
        super(context);
    }


    public MessageRecord(Context context, String title, String content) {
        this(context);
        this.title = title;
        this.content = content;
    }

    public String toString(){
         return "Title:  " + title + "\n" + "Content: " + content;
    }
}
