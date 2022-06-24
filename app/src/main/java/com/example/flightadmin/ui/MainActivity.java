package com.example.flightadmin.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flightadmin.R;
import com.example.flightadmin.core.DatabaseMng;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edLogin = this.findViewById(R.id.edLogin);
        EditText edPasswd = this.findViewById(R.id.edPasswd);

        Button btnAccept = this.findViewById(R.id.btnAccept);
        Button btnRegister = this.findViewById(R.id.btnRegister);

        btnAccept.setEnabled(false);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(MainActivity.this, RegActivity.class);
                activityResultLauncher.launch(registerActivity);
            }
        });

        ActivityResultContract<Intent, ActivityResult> contract =
                new ActivityResultContracts.StartActivityForResult();
        ActivityResultCallback<ActivityResult> callback =
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent retData = result.getData();
                            String login = retData.getExtras().getString("login", "ERROR");
                            String passwd = retData.getExtras().getString("passwd", "ERROR");

                            if(MainActivity.this.gestorDB.addUser(login, passwd)){
                                Toast.makeText(MainActivity.this, "The user was registered",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "There was a problem registering the user",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                };

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.this.gestorDB.checkLogin(edLogin.getText().toString(), edPasswd.getText().toString())){
                    Intent registerActivity = new Intent(MainActivity.this, MenuActivity.class);
                    registerActivity.putExtra("login", edLogin.getText().toString());
                    activityResultLauncher.launch(registerActivity);

                }else{
                    Toast.makeText(MainActivity.this, "Wrong login or password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.activityResultLauncher = this.registerForActivityResult(contract, callback);
        this.gestorDB = new DatabaseMng(this.getApplicationContext());

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
                    btnAccept.setEnabled(true);
                }else{
                    btnAccept.setEnabled(false);
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
                    btnAccept.setEnabled(true);
                }else{
                    btnAccept.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        this.gestorDB.close();
    }

    private DatabaseMng gestorDB;

}