package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.services.DiscoverService;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.HandleGPS;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.Login;

public class NavigationActivity extends ActionBarActivity {

    private HashMap<Marker, Message> allMarkersMap = new HashMap<Marker, Message>();

    private Marker myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Intent service = new Intent(this, DiscoverService.class);
        startService(service);

        new Login(this).autoLogin();

        setUpMapIfNeeded();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        //allMarkersMap = new HashMap<Marker, Message>();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapsfragment);

        mapFragment.newInstance();

        User user = Login.user;

        if (user != null)
            setTitle(user.getFirstName() + " " + user.getLastName() + "'s Tags");

        mapFragment.getMapAsync(
            new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {

                    googleMap.getUiSettings().setMapToolbarEnabled(false);
                    googleMap.getUiSettings().setCompassEnabled(false);
                    googleMap.getUiSettings().setRotateGesturesEnabled(false);

                    googleMap.setOnMarkerClickListener(
                        new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                marker.showInfoWindow();

                                if (marker == myLocation) {
                                    Intent intent = new Intent(getApplicationContext(), PostMessageAcvitiy.class);

                                    startActivity(intent);
                                } else {
                                    Message msg = allMarkersMap.get(marker);

                                    Intent intent = new Intent(getApplicationContext(), ViewMessage.class);

                                    if (msg != null) {
                                        intent.putExtra("MsgObj", msg.getMessageID());

                                        startActivity(intent);
                                    }

                                }

                                return true;
                            }
                        }
                    );

                    setUpMap(googleMap);
                }
            }
        );
    }

    private void setUpMap(final GoogleMap mMap) {

        HandleGPS gps = new HandleGPS(NavigationActivity.this);


        double lat = gps.getLatitude();
        double lon = gps.getLongitude();

        boolean validGPSFix = gps.isValidGPS();

        if (validGPSFix) {

            LatLng curLoc = new LatLng(lat,lon);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 14));

            MarkerOptions mo = new MarkerOptions();
            mo.position(curLoc);
            mo.title("Me");
            mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            myLocation = mMap.addMarker(mo);

        }

        MessageRestClient mRC = new MessageRestClient(this);
        mRC.getMessagesForUser(
                new AjaxCompleteHandler() {
                    @Override
                    public void handleAction(Object someData) {
                        Message msg = (Message) someData;

                        MarkerOptions mo = new MarkerOptions();
                        mo.position(new LatLng(msg.getLatitude(), msg.getLongitude()));
                        mo.title(msg.getContent());
                        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                        Marker marker = mMap.addMarker(mo);

                        allMarkersMap.put(marker, msg);

                    }
                }
        );

        mRC.getMessagesForFriends(
                new AjaxCompleteHandler() {
                    @Override
                    public void handleAction(Object someData) {
                        Message msg = (Message) someData;

                        MarkerOptions mo = new MarkerOptions();
                        mo.position(new LatLng(msg.getLatitude(), msg.getLongitude()));
                        mo.title(msg.getContent());
                        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                        Marker marker = mMap.addMarker(mo);

                        allMarkersMap.put(marker, msg);

                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_login) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_post_message) {
            Intent intent = new Intent(this, PostMessageAcvitiy.class);
            startActivity(intent);
        }

        if (id == R.id.add_friend) {

            Intent myIntent = new Intent(this, AddFriendActivity.class);
            startActivity(myIntent);
        }

        if (id == R.id.action_refresh) {
            setUpMapIfNeeded();
        }

        return super.onOptionsItemSelected(item);
    }

}
