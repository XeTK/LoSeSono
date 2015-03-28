package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.Login;


public class ViewMessage extends ActionBarActivity {

    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        new Login(this).autoLogin();

        Bundle extras = getIntent().getExtras();

        int msgID = extras.getInt("MsgObj");

        MessageRestClient mRC = new MessageRestClient(this);

        mRC.getMessageByID(
            msgID,
            new AjaxCompleteHandler() {
                @Override
                public void handleAction(Object someData) {
                    Message msg = (Message) someData;
                    User user = Login.user;

                    if (msg != null && user != null) {
                        if (msg.getUserID() != user.getUserID()) {
                            MenuItem item = (MenuItem) findViewById(R.id.audience);
                            item.setVisible(false);
                            invalidateOptionsMenu();
                        }

                        setUpMapIfNeeded(msg);
                    }
                }
            }
        );



    }

    private void setUpMapIfNeeded(final Message msg) {

        this.message = msg;

        // Try to obtain the map from the SupportMapFragment.

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.vMsgMapFragment);

        mapFragment.getMapAsync(
                new OnMapReadyCallback() {

                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Toast.makeText(getApplicationContext(), "Map Ready", Toast.LENGTH_LONG).show();

                        setUpMap(googleMap, msg);
                    }
                }
        );
    }

    private void setUpMap(GoogleMap mMap, Message msg) {

        LatLng location = new LatLng(msg.getLatitude(), msg.getLongitude());

        MarkerOptions markerOpts = new MarkerOptions();
        markerOpts.position(location);
        markerOpts.title(msg.getContent());
        markerOpts.visible(true);
        markerOpts.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));


        Marker marker = mMap.addMarker(markerOpts);
        marker.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));

        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.audience) {

            Intent myIntent = new Intent(this, MessageFriendsActivity.class);

            myIntent.putExtra("Message_ID", message.getMessageID());

            startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
