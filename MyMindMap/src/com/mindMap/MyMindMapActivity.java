package com.mindMap;

import com.loading.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class MyMindMapActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    WindowManager.LayoutParams.FLAG_FULLSCREEN);//…Ë÷√»´∆¡  
        setContentView(R.layout.main);
    }
}