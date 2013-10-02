package org.nupter.nupter.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.umeng.analytics.MobclickAgent;
import org.nupter.nupter.AppConstants;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.Log;



/**
 * 百度地图Activity
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 *         <p/>
 *         由百度地图Android SDK Demo修改而来
 */

public class MapBaiduActivity extends Activity {

    final static String TAG = "MainActivity";
    private MapView mMapView = null;
    private MapController mMapController = null;
    MKMapViewListener mMapListener = null;
    Point[] points = {new Point (118.936514,32.124308
            ,"主体育场:阅兵仪式，运动会都是在这里",0),
            new Point (118.937345,32.124358
                    ,"体育馆:有室内篮球场和羽毛球场，体能测试也在这边",0),
            new Point (118.937695,32.123303
                    ,"大学生活动中心:部分社团活动室所在地。各种文艺表演，各院晚会都在这演出",0),
            new Point (118.932346,32.122926
                    ,"青教楼:这是一个神奇的地方，据说可以出租。",0),
            new Point (118.939227,32.125556
                    ,"桂苑:男生宿舍",0),
            new Point (118.940341,32.124287
                    ,"柳苑:男生宿舍",0),
            new Point (118.94106,32.123293
                    ,"李苑:女生宿舍",0),
            new Point (118.938509,32.124746
                    ,"荷苑:男生宿舍",0),
            new Point (118.939623,32.123737
                    ,"南三食堂",0),
            new Point (118.93983,32.123496
                    ,"教育超市",0),
            new Point (118.939856,32.122819
                    ,"桃苑:男生宿舍",0),
            new Point (118.938514,32.122682
                    ,"学生事务中心（含门诊部）:新生在这领书，生病一定要来看看~",0),
            new Point (118.938141,32.118083
                    ,"图书馆:学霸集中营，复习好去处，空调、无线妥妥的。",0),
            new Point (118.94009,32.11924
                    ,"菊苑:男生宿舍",0),
            new Point (118.940495,32.118014
                    ,"竹苑:女生宿舍",0),
            new Point (118.939839,32.1169
                    ,"兰苑:男生宿舍",0),
            new Point (118.939928,32.115784
                    ,"梅苑:女生宿舍",0),
            new Point (118.939382,32.118014
                    ,"南二食堂",0),
            new Point (118.939946,32.114668
                    ,"南一食堂",0),
            new Point (118.937575,32.115494
                    ,"教学楼:南邮学子们学习，自习的地方，大教室有空调~",0),
            new Point (118.936066,32.113307
                    ,"行政楼",0),
            new Point (118.938106,32.112561
                               ,"南邮正门（南门）",0)
    };

    public class Point {
        public double longtitude;
        public double latitude;
        public int imageid;
        public String text;

        public Point(double longtitude, double latitude, String text, int imageid) {
            this.longtitude = longtitude;
            this.latitude = latitude;
            this.text = text;
            this.imageid = imageid;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // 初始化BMapManager
        MyApplication app = (MyApplication) this.getApplication();
        if (app.baiduMapManager == null) {
            app.baiduMapManager = new BMapManager(this);
            app.baiduMapManager.init(AppConstants.BaiduMapKey, new MyApplication.MyGeneralListener());
        }

        // 初始化布局
        setContentView(R.layout.activity_baidumap);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.setSatellite(true);

        mMapController = mMapView.getController();
        mMapController.enableClick(true);
        mMapController.setZoom(16);


        // 将聚焦点移动到南邮仙林校区
        GeoPoint p;
        double cLat = 32.120688;
        double cLon = 118.93697;
        p = new GeoPoint((int) (cLat * 1E6), (int) (cLon * 1E6));
        mMapController.setCenter(p);

        /**
         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        mMapListener = new MKMapViewListener() {
            @Override
            public void onMapMoveFinish() {

            }

            @Override
            public void onClickMapPoi(MapPoi mapPoiInfo) {

                String title = "";
                if (mapPoiInfo != null) {
                    title = mapPoiInfo.strText;
                    Toast.makeText(MapBaiduActivity.this, title, Toast.LENGTH_SHORT).show();
                    mMapController.animateTo(mapPoiInfo.geoPt);
                }
            }

            @Override
            public void onGetCurrentMap(Bitmap b) {

            }

            @Override
            public void onMapAnimationFinish() {
            }
        };


        mMapView.getOverlays().clear();
        Drawable mark = getResources().getDrawable(R.drawable.point_mark_bule);

        OverlayPoint itemOverlay = new OverlayPoint(mark, mMapView, points);
        mMapView.getOverlays().add(itemOverlay);


        for (Point point : points) {
            GeoPoint p1 = new GeoPoint((int) (point.latitude * 1E6), (int) (point.longtitude * 1E6));
            OverlayItem item1 = new OverlayItem(p1, point.text, "x");

            item1.setTitle(point.text);
            item1.setSnippet("南邮的图书馆");

            itemOverlay.addItem(item1);

        }


        mMapView.refresh();


        mMapView.regMapViewListener(MyApplication.getInstance().baiduMapManager, mMapListener);
    }


    class OverlayPoint extends ItemizedOverlay<OverlayItem> {
        //用MapView构造ItemizedOverlay
        PopupOverlay pop;
        Point[] points;

        public OverlayPoint(Drawable mark, MapView mapView, Point[] point) {
            super(mark, mapView);
            this.points = point;
        }

        protected boolean onTap(int index) {
           // Log.d("item onTap: " + index);
            Toast.makeText(MapBaiduActivity.this, this.getItem(index).getTitle(), 500).show();

// 显示图片
//            if (points[index].imageid != 0) {
//                ImageView img = new ImageView(MapBaiduActivity.this);
//                img.setImageResource(points[index].imageid);
//                new AlertDialog.Builder(MapBaiduActivity.this)
//                        .setView(img)
//                        .show();
//            }
            return true;
        }
    }


    @Override
    protected void onPause() {
        /**
         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        mMapView.onPause();
        MobclickAgent.onPause(this);

        super.onPause();
    }

    @Override
    protected void onResume() {
        /**
         *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
         */
        mMapView.onResume();
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        /**
         *  MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
         */
        mMapView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }





}
