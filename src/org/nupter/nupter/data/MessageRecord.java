package org.nupter.nupter.data;

import android.content.Context;
import com.orm.SugarRecord;

/**
 * Created with IntelliJ IDEA.
 * User: linxiangyu
 * Date: 13-8-5
 * Time: 下午12:56
 * To change this template use File | Settings | File Templates.
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
