package com.bawei.dimensionbusiness.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bawei.dimensionbusiness.Base.BaseActivity;
import com.bawei.dimensionbusiness.R;
import com.bawei.dimensionbusiness.bean.Register;
import com.bawei.dimensionbusiness.utils.NetUtils;
import com.google.gson.Gson;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity {

    private EditText et_name;
    private EditText et_pwd;
    private Button bt_reg;
    String path="http://mobile.bwstudent.com/small/user/v1/register";

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        //找控件
        et_name = findViewById(R.id.et_name);
        et_pwd = findViewById(R.id.et_pwd);
        bt_reg = findViewById(R.id.bt_reg);
    }

    @Override
    protected void initData() {

        //点击事件
        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = et_name.getText();
                Editable pwd = et_pwd.getText();

                String name = text.toString();
                String password = pwd.toString();
                //创建map集合
                HashMap<String, String> map = new HashMap<>();
                map.put("phone",name);
                map.put("pwd",password);
                NetUtils.getNetutls().getregister(path, map, new NetUtils.Contiontper() {
                    @Override
                    public void onCtion(String json) {
                        Gson gson = new Gson();
                        Register register = gson.fromJson(json,Register.class);
                        String message = register.getMessage();
                        Toast.makeText(RegisterActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String msg) {

                    }
                });


            }
        });
    }
}
