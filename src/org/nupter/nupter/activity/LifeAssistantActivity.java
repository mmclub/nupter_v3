package org.nupter.nupter.activity;

import org.nupter.nupter.R;

import android.app.Activity;
import android.app.TabActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


/**
 *  生活小秘书板块主界面

 *  @author panlei
*/

public class LifeAssistantActivity extends TabActivity {
    
    private TabHost LifeassistantTabhost;
    private int[] layoutTab = new int [] {
            R.id.widget_layout_schoolguide,
            R.id.widget_layout_busroute,
            R.id.widget_layout_localnews,
            R.id.widget_layout_resttable
            };
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        
        this.LifeassistantTabhost = super.getTabHost();//取得tabhost对象
        LayoutInflater.from(this).inflate(R.layout.activity_lifeassistant, 
                this.LifeassistantTabhost.getTabContentView(), 
                true);//true表示实例化布局文件中的组件
        for(int x = 0; x < this.layoutTab.length; x++){
            TabSpec lifeassTab = this.LifeassistantTabhost.newTabSpec("tab" + x);
            lifeassTab.setIndicator("标签"+x);
            lifeassTab.setContent(layoutTab[x]);
            this.LifeassistantTabhost.addTab(lifeassTab);
            
       
            
        }
       
    }
}