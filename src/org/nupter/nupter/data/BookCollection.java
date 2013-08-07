package org.nupter.nupter.data;

import android.content.Context;
import com.orm.SugarRecord;

/**
 * Created with IntelliJ IDEA.
 * User: Foci
 * Date: 13-8-6
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
public class BookCollection extends SugarRecord<BookCollection> {
    String name;
    String author;
    String bookNum;
    String bookInfo;

    public BookCollection(Context ctx){
        super(ctx);
    }
    public BookCollection(Context ctx, String name, String author, String bookNum , String bookInfo){
        super(ctx);
        this.name = name;
        this.author = author;
        this.bookNum = bookNum;
        this.bookInfo = bookInfo;
    }
}
