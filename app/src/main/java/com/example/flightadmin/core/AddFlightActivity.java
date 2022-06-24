package com.example.flightadmin.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flightadmin.R;

public class AddFlightActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addflight);


        EditText edDestination = (EditText) this.findViewById(R.id.edDestination);
        Spinner spnAirline = (Spinner) this.findViewById(R.id.spnAirline);
        EditText edDate = (EditText) this.findViewById(R.id.edDate);
        EditText edTime = (EditText) this.findViewById(R.id.edTime);
        EditText edOrigin = (EditText) this.findViewById(R.id.edOrigin);

        String spnvalue = spnAirline.getSelectedItem().toString();

        Button btnCancel = (Button) this.findViewById(R.id.btnCancel);
        Button btnAdd = (Button) this.findViewById(R.id.btnAdd);

        final Intent sendData = this.getIntent();
        if(sendData.getExtras() != null){
            final String destination = sendData.getExtras().getString("destination", "");
            final String airline = sendData.getExtras().getString("airline", "");
            final String date = sendData.getExtras().getString("date", "");
            final String time = sendData.getExtras().getString("time", "");
            final String origin = sendData.getExtras().getString("origin", "");

            edDestination.setText(destination);
            Adapter adapter = spnAirline.getAdapter();
            int n = adapter.getCount();
            int i = 0;
            while(i<n && !spnAirline.getItemAtPosition(i).equals(airline)){
                i++;
            }

            spnAirline.setSelection(i);
            edDate.setText(date);
            edTime.setText(time);
            edOrigin.setText(origin);

            spnvalue = spnAirline.getSelectedItem().toString();
        }

        btnAdd.setEnabled(false);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFlightActivity.this.setResult(Activity.RESULT_CANCELED);
                AddFlightActivity.this.finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String destination = edDestination.getText().toString();
                final String airline = spnAirline.getSelectedItem().toString();
                final String date = edDate.getText().toString();
                final String time = edTime.getText().toString();
                final String origin = edOrigin.getText().toString();

                final Intent retData = new Intent();

                retData.putExtra("destination", destination);
                retData.putExtra("airline", airline);
                retData.putExtra("date", date);
                retData.putExtra("time", time);
                retData.putExtra("origin", origin);

                AddFlightActivity.this.setResult(Activity.RESULT_OK, retData);
                AddFlightActivity.this.finish();
            }
        });

        edDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edDestination.getText().toString().trim().length() > 0){
                    btnAdd.setEnabled(true);
                }else{
                    btnAdd.setEnabled(false);
                }
            }
        });

        String finalSpnvalue = spnvalue;
        spnAirline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(finalSpnvalue == spnAirline.getSelectedItem().toString()){
                    btnAdd.setEnabled(true);
                }else{
                    btnAdd.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        edDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edDate.getText().toString().trim().length() > 0){
                    btnAdd.setEnabled(true);
                }else{
                    btnAdd.setEnabled(false);
                }
            }
        });

        edTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edTime.getText().toString().trim().length() > 0){
                    btnAdd.setEnabled(true);
                }else{
                    btnAdd.setEnabled(false);
                }
            }
        });

        edOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edOrigin.getText().toString().trim().length() > 0){
                    btnAdd.setEnabled(true);
                }else{
                    btnAdd.setEnabled(false);
                }
            }
        });

    }
}