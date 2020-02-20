package com.bawei.dimensionbusiness.base;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutResID());

    }
    //加载布局资源ID
    protected abstract int LayoutResID();
}
