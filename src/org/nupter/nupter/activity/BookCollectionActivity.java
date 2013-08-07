package org.nupter.nupter.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import org.nupter.nupter.R;
import org.nupter.nupter.data.BookCollection;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Foci
 * Date: 13-8-6
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
public class BookCollectionActivity extends ListActivity {
    private List<Map<String,String>> bookCollectionMap = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<BookCollection> bookCollections = BookCollection.listAll(BookCollection.class);

        BookCollection.deleteAll(BookCollection.class);

        bookCollectionMap = BookDataActivity.getMaps();
        SimpleAdapter adapter = new SimpleAdapter(BookCollectionActivity.this, bookCollectionMap,
                R.layout.item_activity_book, new String[]{"bookName",
                "bookAuthor", "bookNum"},
                new int[]{R.id.bookName, R.id.bookAuthor,
                        R.id.bookNum});
        setListAdapter(adapter);
    }
}