package uk.co.tomrosier.xetk.losesono.prototype.prototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;


public class MainActivity extends ActionBarActivity {

    Button btnGetGPS;
    Button btnSend;
    Button btnLogin;
    Button btnAddLocation;
    Button btnRegister;

    TextView lblGPSLoc;

    EditText txtUserName;

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetGPS      = (Button)   findViewById(R.id.btnGetGPS);
        btnSend        = (Button)   findViewById(R.id.btnSend);
        btnLogin       = (Button)   findViewById(R.id.btnLogin);
        btnAddLocation = (Button)   findViewById(R.id.btnAddLocation);
        btnRegister    = (Button)   findViewById(R.id.btnRegister);

        lblGPSLoc      = (TextView) findViewById(R.id.lblGPSLoc);

        txtUserName    = (EditText) findViewById(R.id.txtUserName);

        final Context context = getApplicationContext();

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


        btnSend.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                //String username = txtUserName.getText().toString();

                MessageRestClient mRC = new MessageRestClient(getApplicationContext());

                mRC.getMessages(MainActivity.this);
                //mRC.addMessage(MainActivity.this);
                }
            }
        );

        btnLogin.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                Login login = new Login(getApplicationContext());

                login.loginUser();

                }
            }
        );

        btnAddLocation.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                MessageRestClient mRC = new MessageRestClient(getApplicationContext());

                //mRC.getMessages(MainActivity.this);
                mRC.addMessage(MainActivity.this);

                }
            }
        );

        btnRegister.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(myIntent);
                }
            }
        );

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
