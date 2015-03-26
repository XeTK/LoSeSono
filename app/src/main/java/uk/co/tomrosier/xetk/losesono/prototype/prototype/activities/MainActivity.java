package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.Constants;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.services.DiscoverService;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.GPSTracker;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.Login;


public class MainActivity extends ActionBarActivity {

    Button btnGetGPS;
    Button btnLoadMap;
    Button btnAddLocation;

    TextView lblGPSLoc;
    TextView lblURL;

    GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetGPS      = (Button)   findViewById(R.id.btnGetGPS);
        btnLoadMap     = (Button)   findViewById(R.id.btnLoadMap);
        btnAddLocation = (Button)   findViewById(R.id.btnAddLocation);

        lblGPSLoc      = (TextView) findViewById(R.id.lblGPSLoc);
        lblURL         = (TextView) findViewById(R.id.lblURL);

        lblURL.setText(Constants.BASE_URL);

        Intent service = new Intent(this, DiscoverService.class);
        startService(service);

        // Show location button click event
        btnGetGPS.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                // Create class object
                gps = new GPSTracker(MainActivity.this);

                // Check if GPS enabled
                if(gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    if (latitude != 0 && longitude != 0) {
                        lblGPSLoc.setText("Your Location is - \nLat: " + latitude + "\nLong: " + longitude);
                    } else {
                        lblGPSLoc.setText("No fix yet");
                    }
                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.
                    gps.showSettingsAlert();
                }
                }
            }
        );


        btnLoadMap.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent myIntent = new Intent(MainActivity.this, NavigationActivity.class);
                        MainActivity.this.startActivity(myIntent);
                    }
                }
        );

        //MessageRestClient mRC = new MessageRestClient(getApplicationContext());
        //mRC.getMessages(MainActivity.this);

        btnAddLocation.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent myIntent = new Intent(MainActivity.this, PostMessageAcvitiy.class);
                    MainActivity.this.startActivity(myIntent);
                }
            }
        );

        new Login(this).autoLogin();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_login) {
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
