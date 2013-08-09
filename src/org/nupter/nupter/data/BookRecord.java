package org.nupter.nupter.data;

import android.content.Context;
import com.orm.SugarRecord;

/**
 *
 * 图书Model
 *
 * @author WangTao
 *
 */
public class BookRecord extends SugarRecord<BookRecord> {
    public String name;
    public String author;
    public String bookNum;
    public String bookInfo;

    public BookRecord(Context ctx){
        super(ctx);
    }
    public BookRecord(Context ctx, String name, String author, String bookNum, String bookInfo){
        super(ctx);
        this.name = name;
        this.author = author;
        this.bookNum = bookNum;
        this.bookInfo = bookInfo;
    }

}
