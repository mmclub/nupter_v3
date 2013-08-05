package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.nupter.nupter.R;

/**
 * 从网络加载图片演示
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 *
 * 参见 https://github.com/nostra13/Android-Universal-Image-Loader
 */
public class ImageLoaderActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageloader);

        String url = "http://mobilenupt.sinaapp.com/html/Tpl/Public/images/logo.png";

        ImageView image = (ImageView)findViewById(R.id.imageView);
        ImageLoader.getInstance().displayImage(url, image);
    }
}