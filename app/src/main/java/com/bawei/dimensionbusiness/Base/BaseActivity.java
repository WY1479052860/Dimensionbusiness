package com.bawei.dimensionbusiness.Base;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bawei.dimensionbusiness.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        //加载控件
        initView();
        //加载数据
        initData();
    }
    //加载布局资源id
    protected abstract int getLayoutResID();
    //加载控件
    protected abstract void initView();
    //加载数据
    protected abstract void initData();


}
