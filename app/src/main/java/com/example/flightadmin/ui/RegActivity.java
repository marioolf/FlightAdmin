package com.example.flightadmin.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import com.example.flightadmin.*;
import com.example.flightadmin.R;

public class RegActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        EditText edLogin = (EditText) this.findViewById(R.id.edLogin);
        EditText edPasswd = (EditText) this.findViewById(R.id.edPasswd);
        Button btnBack = (Button) this.findViewById(R.id.btnBack);
        Button btnRegister = (Button) this.findViewById(R.id.btnRegister);

        btnRegister.setEnabled(false);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegActivity.this.setResult(Activity.RESULT_CANCELED);
                RegActivity.this.finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String login = edLogin.getText().toString();
                final String passwd = edPasswd.getText().toString();
                final Intent retData = new Intent();

                retData.putExtra("login", login);
                retData.putExtra("passwd", passwd);

                RegActivity.this.setResult(Activity.RESULT_OK, retData);
                RegActivity.this.finish();
            }
        });

        edLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edLogin.getText().toString().trim().length() > 0 &&
                        edPasswd.getText().toString().trim().length() > 0){
                    btnRegister.setEnabled(true);
                }else{
                    btnRegister.setEnabled(false);
                }
            }
        });

        edPasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edLogin.getText().toString().trim().length() > 0 &&
                        edPasswd.getText().toString().trim().length() > 0){
                    btnRegister.setEnabled(true);
                }else{
                    btnRegister.setEnabled(false);
                }
            }
        });
    }
}