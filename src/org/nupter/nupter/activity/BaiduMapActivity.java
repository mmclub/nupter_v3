package org.nupter.nupter.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.Log;

import java.io.IOException;

public class BaiduMapActivity extends Activity {

    final static String TAG = "MainActivity";
    private MapView mMapView = null;
    private MapController mMapController = null;
    MKMapViewListener mMapListener = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 使用地图sdk前需先初始化BMapManager.
         * BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
         * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
         */
        MyApplication app = (MyApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            /**
             * 如果BMapManager没有初始化则初始化BMapManager
             */
            app.mBMapManager.init(MyApplication.strKey,new MyApplication.MyGeneralListener());
        }
        /**
         * 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
         */
        setContentView(R.layout.activity_baidumap);
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapController = mMapView.getController();
        mMapController.enableClick(true);
        mMapController.setZoom(15);

        /**
         * 将地图移动至指定点
         * 使用百度经纬度坐标，可以通过http://api.map.baidu.com/lbsapi/getpoint/index.html查询地理坐标
         *
         */
        GeoPoint p ;
        double cLat = 32.120688 ;
        double cLon = 118.93697 ;
        p = new GeoPoint((int)(cLat * 1E6), (int)(cLon * 1E6));
        mMapController.setCenter(p);

        /**
         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        mMapListener = new MKMapViewListener() {
            @Override
            public void onMapMoveFinish() {
                /**
                 * 在此处理地图移动完成回调
                 * 缩放，平移等操作完成后，此回调被触发
                 */
            }

            @Override
            public void onClickMapPoi(MapPoi mapPoiInfo) {
                /**
                 * 在此处理底图poi点击事件
                 * 显示底图poi名称并移动至该点
                 * 设置过： mMapController.enableClick(true); 时，此回调才能被触发
                 *
                 */
                String title = "";
                if (mapPoiInfo != null){
                    title = mapPoiInfo.strText;
                    Toast.makeText(BaiduMapActivity.this, title, Toast.LENGTH_SHORT).show();
                    mMapController.animateTo(mapPoiInfo.geoPt);
                }
            }

            @Override
            public void onGetCurrentMap(Bitmap b) {
                /**
                 *  当调用过 mMapView.getCurrentMap()后，此回调会被触发
                 *  可在此保存截图至存储设备
                 */
            }

            @Override
            public void onMapAnimationFinish() {
                /**
                 *  地图完成带动画的操作（如: animationTo()）后，此回调被触发
                 */
            }
        };

        /**
         *  在想要添加Overlay的地方使用以下代码，
         *  比如Activity的onCreate()中
         */
        //准备要添加的Overlay

        double mLat1 = 32.117966;
        double mLon1 = 118.93837;




        // 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
        GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));

        //准备overlay图像数据，根据实情情况修复
        Drawable mark= getResources().getDrawable(R.drawable.icon_marka);
        //用OverlayItem准备Overlay数据
        OverlayItem item1 = new OverlayItem(p1,"图书馆","南邮的图书馆");
        //使用setMarker()方法设置overlay图片,如果不设置则使用构建ItemizedOverlay时的默认设置

        item1.setTitle("图书馆");
        item1.setSnippet("南邮的图书馆");

        //创建IteminizedOverlay
        OverlayTest itemOverlay = new OverlayTest(mark, mMapView);
        //将IteminizedOverlay添加到MapView中

        mMapView.getOverlays().clear();
        mMapView.getOverlays().add(itemOverlay);

        //现在所有准备工作已准备好，使用以下方法管理overlay.
        //添加overlay, 当批量添加Overlay时使用addItem(List<OverlayItem>)效率更高
        itemOverlay.addItem(item1);

        mMapView.refresh();
        //删除overlay .
        //itemOverlay.removeItem(itemOverlay.getItem(0));
        //mMapView.refresh();
        //清除overlay
        // itemOverlay.removeAll();
        // mMapView.refresh();

        mMapView.regMapViewListener(MyApplication.getInstance().mBMapManager, mMapListener);
    }

    @Override
    protected void onPause() {
        /**
         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        /**
         *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
         */
        mMapView.onResume();
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



    /*
     * 要处理overlay点击事件时需要继承ItemizedOverlay
     * 不处理点击事件时可直接生成ItemizedOverlay.
     */
    class OverlayTest extends ItemizedOverlay<OverlayItem> {
        //用MapView构造ItemizedOverlay
        PopupOverlay pop;
        public OverlayTest(Drawable mark,MapView mapView){
            super(mark,mapView);
        }
        protected boolean onTap(int index) {
            //在此处理item点击事件
            Log.d("item onTap: " + index);
            Toast.makeText(BaiduMapActivity.this, this.getItem(index).getTitle(), Toast.LENGTH_SHORT).show();

            //pop demo
            //创建pop对象，注册点击事件监听接口
           pop = new PopupOverlay(mMapView,new PopupClickListener() {
                @Override
                public void onClickedPopup(int index) {
                    //在此处理pop点击事件，index为点击区域索引,点击区域最多可有三个
                }
            });

            Bitmap[] bmps = new Bitmap[3];
            try {
                bmps[0] = BitmapFactory.decodeStream(getAssets().open("ic_launcher.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //弹窗弹出位置
            GeoPoint ptTAM = new GeoPoint((int)(this.getItem(index).getPoint().getLatitudeE6() ), (int) (this.getItem(index).getPoint().getLongitudeE6()));
            //弹出pop,隐藏pop
            pop.showPopup(bmps, ptTAM, 32);
            //隐藏弹窗
            //


            return true;
        }
        public boolean onTap(GeoPoint pt, MapView mapView){
            //在此处理MapView的点击事件，当返回 true时
            super.onTap(pt,mapView);
            if (pop != null)
                pop.hidePop();

            return false;
        }

    }


}
