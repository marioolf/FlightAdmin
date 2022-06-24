package com.example.flightadmin.ui;

import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.example.flightadmin.R;

public class MoreInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);

        TextView edDestination = this.findViewById(R.id.edDestination);
        TextView spnAirline = this.findViewById(R.id.spnAirline);
        TextView edDate = this.findViewById(R.id.edDate);
        TextView edTime = this.findViewById(R.id.edTime);
        TextView edOrigin = this.findViewById(R.id.edOrigin);

        Button btnBack = (Button) this.findViewById(R.id.btnBack);

        final Intent sendData = this.getIntent();
        if (sendData.getExtras() != null) {

            final String destination = sendData.getExtras().getString("destination", "");
            final String airline = sendData.getExtras().getString("airline", "");
            final String date = sendData.getExtras().getString("date", "");
            final String time = sendData.getExtras().getString("time", "");
            final String origin = sendData.getExtras().getString("origin", "");

            edDestination.setText(destination);
            spnAirline.setText(airline);
            edDate.setText(date);
            edTime.setText(time);
            edOrigin.setText(origin);

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoreInfoActivity.this.setResult(Activity.RESULT_CANCELED);
                    MoreInfoActivity.this.finish();
                }
            });
        }
    }
}