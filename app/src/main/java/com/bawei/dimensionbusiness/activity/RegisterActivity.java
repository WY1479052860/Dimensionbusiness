package com.bawei.dimensionbusiness.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bawei.dimensionbusiness.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_name;
    private EditText et_pwd;
    private Button bt_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //找控件
        et_name = findViewById(R.id.et_name);
        et_pwd = findViewById(R.id.et_pwd);
        bt_reg = findViewById(R.id.bt_reg);
    }
}
