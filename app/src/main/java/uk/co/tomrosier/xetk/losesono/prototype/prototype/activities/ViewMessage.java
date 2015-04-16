package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.app.NotificationManager;
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
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.UserRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.Login;


public class ViewMessage extends ActionBarActivity {

    private Message message;

    private Menu menu;

    private boolean isEditable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        new Login(this).autoLogin();

        Bundle extras = getIntent().getExtras();

        final int msgID = extras.getInt("MsgObj");

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.cancel(msgID);

        final MessageRestClient mRC = new MessageRestClient(this);

        mRC.getMessageByID(
            msgID,
            new AjaxCompleteHandler() {
                @Override
                public void handleAction(Object someData) {
                    Message msg = (Message) someData;
                    User user = Login.user;

                    if (msg != null && user != null) {
                        if (msg.getUserID() != user.getUserID()) {
                            isEditable = false;
                            menu.findItem(R.id.audience).setVisible(isEditable);
                            invalidateOptionsMenu();

                            UserRestClient uRC = new UserRestClient(getApplicationContext());

                            uRC.getUserByID(
                                msg.getUserID(),
                                new AjaxCompleteHandler() {
                                    @Override
                                    public void handleAction(Object someData) {
                                        User user = (User)someData;

                                        setTitle(user.getFirstName() + " " + user.getLastName() + " has tagged");

                                    }
                                }
                            );

                            mRC.addMessageRead(
                                    msgID,
                                    new AjaxCompleteHandler() {
                                        @Override
                                        public void handleAction(Object someData) {
                                            Toast.makeText(getApplicationContext(), "Marking message " + msgID + " as Read!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );
                        } else {
                            setTitle("I have tagged");
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
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_view_message, menu);
        menu.findItem(R.id.audience).setVisible(isEditable);

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
