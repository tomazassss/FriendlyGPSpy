package com.crazy.specialists.friendlygpspy;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crazy.specialists.friendlygpspy.communication.CommsManager;
import com.crazy.specialists.friendlygpspy.location.LocationFinder;
import com.crazy.specialists.friendlygpspy.utils.Utilities;

import static com.crazy.specialists.friendlygpspy.utils.Parameters.DEFAULT_IP;
import static com.crazy.specialists.friendlygpspy.utils.Parameters.PAIRED_IP_PROPERTY;

public class FriendlyGPSpy extends AppCompatActivity {

    private CommsManager commsManager;
    private FloatingActionButton requestLocationBtn;


    private LocationFinder locationFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendly_gpspy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestLocationBtn = (FloatingActionButton) findViewById(R.id.fab);
        requestLocationBtn.setOnClickListener(view -> Snackbar.make(view, "Replace with your own sudai! brudaifgfgfgfgfg!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        locationFinder = new LocationFinder(FriendlyGPSpy.this);

        Button locationButton = (Button) findViewById(R.id.locationButton);
        locationButton.setOnClickListener(v -> getCurrentLocation());

        Button myTestButton = (Button) findViewById(R.id.myTestButton);
        myTestButton.setOnClickListener(v -> {
            Log.w("myApp" , "Sending...");
            commsManager.clientSendData("Test kazkas ");
            Log.w("myApp" , "Sent...");
        });
    }

    private void getCurrentLocation() {
        Location location = locationFinder.findLocation();
        // check if GPS enabled
        if(locationFinder.canGetLocation() && location != null){
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + location.getLatitude() + "\nLong: " + location.getLongitude(), Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            locationFinder.showSettingsAlert();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        init();
        View.OnClickListener l = v -> {

        };
        requestLocationBtn.setOnClickListener(l);
    }

    private void init()
    {
        SharedPreferences sharedPref = getSharedPreferences();
        pairToDeviceIfNeeded(sharedPref);
        startCommsManager(sharedPref);
    }

    private void startCommsManager(final SharedPreferences sharedPref)
    {
        String endpointIp = sharedPref.getString(PAIRED_IP_PROPERTY, DEFAULT_IP);
        commsManager = new CommsManager(endpointIp, (message) -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show());
        commsManager.start();
    }

    private void pairToDeviceIfNeeded(final SharedPreferences sharedPref)
    {
        if(!sharedPref.contains(PAIRED_IP_PROPERTY))
        {
            pairToAnotherDevice();
        }
        else
        {
            String endpointIp = sharedPref.getString(PAIRED_IP_PROPERTY, DEFAULT_IP);
            if(!Utilities.isIpValid(endpointIp))
            {
                pairToAnotherDevice();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friendly_gpspy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_show_my_ip) {
            showMyIp();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void pairToAnotherDevice() {
        final EditText text = new EditText(this);
        text.setSingleLine();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pair to device")
                .setMessage("Enter IP of device you want to pair with")
                .setView(text)
                .setPositiveButton("Save", (dialog, which) -> {
                    saveToSharedPreferences(PAIRED_IP_PROPERTY, text.getText().toString());
                }).setNegativeButton("Cancel", (dialog, which) -> {
                    //User canceled dialog
                });

        builder.create();
        builder.show();
    }

    private void saveToSharedPreferences(final String key, final String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void showMyIp() {
        String myIpV4 = Utilities.getIPAddress(true);
        String myIpV6 = Utilities.getIPAddress(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("My IP")
                .setMessage("IPv4: " + myIpV4  + "\n" + "IPv6: " + myIpV6)
                .setNeutralButton("Ok", (dialog, which) -> {
                    //Done
                });

        builder.create();
        builder.show();
    }

    private SharedPreferences getSharedPreferences() {
        return this.getPreferences(Context.MODE_PRIVATE);
    }
}
