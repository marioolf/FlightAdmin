package com.example.flightadmin.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flightadmin.R;
import com.example.flightadmin.core.AddFlightActivity;
import com.example.flightadmin.core.DatabaseMng;

public class MenuActivity
        extends AppCompatActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        ListView lvFlights = this.findViewById(R.id.lvFlights);
        Button btnAdd = this.findViewById(R.id.btnAdd);
        Spinner spnAirline = this.findViewById(R.id.spnAirline);

        View header = getLayoutInflater().inflate(R.layout.header, null);

        final Intent sendData = this.getIntent();
        login = sendData.getExtras().getString("login", "ERROR");

        lvFlights.addHeaderView(header);

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent addFlight = new Intent(MenuActivity.this, AddFlightActivity.class);
                activityResultLauncher.launch(addFlight);
}
        });

        ActivityResultContract<Intent, ActivityResult> contract =
                new ActivityResultContracts.StartActivityForResult();
        ActivityResultCallback<ActivityResult> callback =
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();

                            String destination = data.getExtras().getString("destination", "ERROR");
                            String airline = data.getExtras().getString("airline", "ERROR");
                            String date = data.getExtras().getString("date", "ERROR");
                            String time = data.getExtras().getString("time", "ERROR");
                            String origin = data.getExtras().getString("origin", "ERROR");

                            if(MenuActivity.this.gestorDB.addFlight(destination, airline, date, time, origin, login)){
                                Toast t = Toast.makeText(MenuActivity.this, "The flight has been added",
                                        Toast.LENGTH_SHORT);
                                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                t.show();
                            }else{
                                Toast t = Toast.makeText(MenuActivity.this, "There has been a problem adding the flight",
                                        Toast.LENGTH_SHORT);
                                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                t.show();
                            }
                            MenuActivity.this.adapterDB.changeCursor(MenuActivity.this.gestorDB.getFlights(login));
                        }
                    }
                };
        this.activityResultLauncher = this.registerForActivityResult(contract, callback);

        lvFlights.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Cursor cursor = MenuActivity.this.adapterDB.getCursor();

                if(cursor.moveToPosition(pos-1)){
                    final String destination = cursor.getString(0);
                    final String airline = cursor.getString(1);
                    final String date = cursor.getString(2);
                    final String time = cursor.getString(3);
                    final String origin = cursor.getString(4);

                    Intent addFlightActivity = new Intent(MenuActivity.this, MoreInfoActivity.class);

                    addFlightActivity.putExtra("destination", destination);
                    addFlightActivity.putExtra("airline", airline);
                    addFlightActivity.putExtra("date", date);
                    addFlightActivity.putExtra("time", time);
                    addFlightActivity.putExtra("origin", origin);

                    activityResultLauncher.launch(addFlightActivity);
                }else{
                    Log.e("onContext_edit", "wrong position");
                    Toast t = Toast.makeText(MenuActivity.this, "Wrong position",
                            Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }
            }
        });

        spnAirline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    String airline = spnAirline.getItemAtPosition(position).toString();
                    Cursor flightcursor = MenuActivity.this.gestorDB.getFlightsAirline(airline, login);
                    MenuActivity.this.adapterDB.changeCursor(flightcursor);
                }else{
                    MenuActivity.this.adapterDB.changeCursor(MenuActivity.this.gestorDB.getFlights(login));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        this.registerForContextMenu(lvFlights);
        this.gestorDB = new DatabaseMng(this.getApplicationContext());
    }

    @Override
    public void onStart()
    {
        super.onStart();

        final ListView lvFlights = this.findViewById( R.id.lvFlights );

        this.adapterDB = new SimpleCursorAdapter(
                this,
                R.layout.lvflightsitems,
                null,
                new String[] { DatabaseMng.FLIGHT_COL_DESTINATION, DatabaseMng.FLIGHT_COL_AIRLINE, DatabaseMng.FLIGHT_COL_DATE},
                new int[] { R.id.lv_Item_Destination, R.id.lv_Item_Airline, R.id.lv_Item_Date},
                0
        );

        lvFlights.setAdapter( this.adapterDB );
        this.adapterDB.changeCursor(this.gestorDB.getFlights(MenuActivity.this.login));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        this.gestorDB.close();
        this.adapterDB.getCursor().close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.actmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        boolean toret = false;
        switch(menuItem.getItemId()){
            case R.id.opInfo:
                Intent infoActivity = new Intent(MenuActivity.this, InfoActivity.class);
                activityResultLauncher.launch(infoActivity);
                toret = true;
                break;
        }
        return toret;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo cmi){
        super.onCreateContextMenu(contextMenu, view, cmi);

        if(view.getId() == R.id.lvFlights){
            this.getMenuInflater().inflate(R.menu.contmenu, contextMenu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        boolean toret = super.onContextItemSelected(item);
        int pos = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        Cursor cursor = this.adapterDB.getCursor();

        switch(item.getItemId()){
            case R.id.context_op_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setTitle("You are going to delete a flight");
                builder.setMessage("Do you want to delete your reservation?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(cursor.moveToPosition(pos-1)){
                            final String id = cursor.getString(0);
                            if(MenuActivity.this.gestorDB.deleteFlight(id, MenuActivity.this.login)){
                                Toast t = Toast.makeText(MenuActivity.this, "Flight has been deleted",
                                        Toast.LENGTH_SHORT);
                                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                t.show();

                            }else{
                                Toast t = Toast.makeText(MenuActivity.this, "Flight could not be removed",
                                        Toast.LENGTH_SHORT);
                                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                t.show();
                            }
                            MenuActivity.this.adapterDB.changeCursor(MenuActivity.this.gestorDB.getFlights(login));
                        }else{
                            Log.e("onContext_delete", "wrong position");
                            Toast t = Toast.makeText(MenuActivity.this, "Wrong position",
                                    Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                            t.show();
                        }
                    }
                });
                builder.create().show();
                break;
            case R.id.context_op_edit:
                if(cursor.moveToPosition(pos-1)){
                    final String destination = cursor.getString(0);
                    final String airline = cursor.getString(1);
                    final String date = cursor.getString(2);
                    final String time = cursor.getString(3);
                    final String origin = cursor.getString(4);

                    Intent addFlightActivity = new Intent(MenuActivity.this, AddFlightActivity.class);

                    addFlightActivity.putExtra("destination", destination);
                    addFlightActivity.putExtra("airline", airline);
                    addFlightActivity.putExtra("date", date);
                    addFlightActivity.putExtra("time", time);
                    addFlightActivity.putExtra("origin", origin);

                    activityResultLauncher.launch(addFlightActivity);
                    toret = true;
                }else{
                    Log.e("onContext_edit", "wrong position");
                    Toast t = Toast.makeText(MenuActivity.this, "Wrong position",
                            Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }
                break;
        }
        return toret;
    }

    private DatabaseMng gestorDB;
    private SimpleCursorAdapter adapterDB;
}