package org.nupter.nupter.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONObject;

/**
 * 天气辅助类
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 *
 * 中国天气预报网站API（南京地区）
 *
 * - http://www.weather.com.cn/data/sk/101190101.html 实时天气
 * - http://m.weather.com.cn/data/101190101.html 未来天气预报
 * - http://www.weather.com.cn/data/cityinfo/101190101.html 实时天气
 *
 */
public class Weather {

    public static final String NOW_WEATHER_URL_1 = "http://www.weather.com.cn/data/sk/101190101.html";
    public static final String FUTURE_WEATHER_URL = "http://m.weather.com.cn/data/101190101.html";
    public static final String NOW_WEATHER_URL_2 = "http://www.weather.com.cn/data/cityinfo/101190101.html";


    public String getNanjingWeather(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_WEATHER_URL_1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Log.d(response);


            }

            @Override
            public void onFailure(Throwable throwable, String s) {

            }
        });

        return "";
    }
}
