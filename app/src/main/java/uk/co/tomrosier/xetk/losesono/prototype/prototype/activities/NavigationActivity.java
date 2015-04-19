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

import java.util.HashMap;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.services.DiscoverService;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.HandleGPS;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.Login;

/**
 * This is the main activity for the application and what the application lands on when it is launched.
 * It shows a list of all the tags around the current user and makes them easy to access.
 */

public class NavigationActivity extends ActionBarActivity {

    // Keep a list of all the markers that have been added to the map.
    private HashMap<Marker, Message> allMarkersMap = new HashMap<Marker, Message>();

    // This marker is the location that the current user is in. It is distinct compared to the other markers on the map.
    private Marker myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Load up the background service that checks for the location updates and notifys the user if they are near to a notification.
        Intent service = new Intent(this, DiscoverService.class);
        startService(service);

        // Make sure that the user is logged in to the application and load up the dialog if needed.
        new Login(this).autoLogin(
            new AjaxCompleteHandler() {
                @Override
                public void handleAction(Object someData) {
                    String status = (String) someData;

                    // If the user is logged in correctly then load the dialog correctly.
                    if (status.equals("Success")) {
                        setUpMapIfNeeded();
                    }
                }
            }
        );
    }

    // When the session resumes reload the map if its not right.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setUpMapIfNeeded();
    }

    // When the page is resumed when coming back from other activities then run the code.
    @Override
    protected void onRestart() {
        super.onRestart();
        setUpMapIfNeeded();
    }

    // This sets up the map correctly with the info that is needed.
    private void setUpMapIfNeeded() {

        // Grab the stored authenticated user that we then can work with.
        User user = Login.user;

        // This gets the google maps fragment for us to work with.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapsfragment);

        // Create a new instance of the object with all of the items cleared from it.
        mapFragment.newInstance();

        // Doubly verify that the user is not null before we get there data.
        if (user != null)
            setTitle(user.getFirstName() + " " + user.getLastName() + "'s Tags");

        // This loads the map fragment asynchronously.
        mapFragment.getMapAsync(
            new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {

                    // Disable the parts of the Google maps API that we don't need.
                    googleMap.getUiSettings().setMapToolbarEnabled(false);
                    googleMap.getUiSettings().setCompassEnabled(false);
                    googleMap.getUiSettings().setRotateGesturesEnabled(false);

                    // Overwrite what we do when the markers are clicked on the page.
                    googleMap.setOnMarkerClickListener(
                        new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                // Show the information window first so the user is sure that they know what they have pressed.
                                marker.showInfoWindow();

                                // If the selected marker is the one that is the users current location then we load the message generator page.
                                if (marker == myLocation) {
                                    // Create the intent to swap to that activity.
                                    Intent intent = new Intent(getApplicationContext(), PostMessageActivity.class);

                                    startActivity(intent);
                                } else {
                                    // Get the message for the current tag that we are working with.
                                    Message msg = allMarkersMap.get(marker);

                                    Intent intent = new Intent(getApplicationContext(), ViewMessage.class);

                                    // Pass the message ID to the view message activity so it can view the message.
                                    if (msg != null) {
                                        intent.putExtra("MsgObj", msg.getMessageID());

                                        startActivity(intent);
                                    }

                                }

                                return true;
                            }
                        }
                    );

                    // Load up the map correctly.
                    setUpMap(googleMap);
                }
            }
        );
    }

    // Setup the map with the valid markers we need.
    private void setUpMap(final GoogleMap mMap) {

        // Get the valid GPS fix for the application.
        HandleGPS gps = new HandleGPS(NavigationActivity.this);

        // Get the locations so they are easy to access.
        double lat = gps.getLatitude();
        double lon = gps.getLongitude();

        // Check that we have a valid fix before we continue.
        boolean validGPSFix = gps.isValidGPS();

        // If we got a valid GPS fix then we can add the locations for the current user.
        if (validGPSFix) {

            // Generate the current location long and lat, for passing to the marker.
            LatLng curLoc = new LatLng(lat, lon);

            // Move the camera to the current location that the user is in.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 14));

            // Configure the marker to show the location of the user.
            MarkerOptions mo = new MarkerOptions();
            mo.position(curLoc);
            mo.title("Me");
            // Give it a nice colour to make it stand out.
            mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            // Keep track of the marker so we can reference it later.
            myLocation = mMap.addMarker(mo);

            // Get the message rest client ready to retrieve the information from the backend.
            MessageRestClient mRC = new MessageRestClient(this);

            // Get all the messages for the current user.
            mRC.getMessagesForUser(
                    new AjaxCompleteHandler() {
                        @Override
                        public void handleAction(Object someData) {
                            // Get each message and process it and add to the map.
                            Message msg = (Message) someData;

                            // Generate the tag on the map.
                            MarkerOptions mo = new MarkerOptions();
                            mo.position(new LatLng(msg.getLatitude(), msg.getLongitude()));
                            mo.title(msg.getContent());
                            mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                            // Add it to the map.
                            Marker marker = mMap.addMarker(mo);

                            // Add it to the list of markers we have added to the map.
                            allMarkersMap.put(marker, msg);
                        }
                    }
            );

            // Get all the messages relevant to the user left by friends.
            mRC.getMessagesForFriends(
                    new AjaxCompleteHandler() {
                        @Override
                        public void handleAction(Object someData) {
                            // For each message we process and add to the map.
                            Message msg = (Message) someData;

                            // Generate the tag to add to the map.
                            MarkerOptions mo = new MarkerOptions();
                            mo.position(new LatLng(msg.getLatitude(), msg.getLongitude()));
                            mo.title(msg.getContent());
                            mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                            // Add it to the map.
                            Marker marker = mMap.addMarker(mo);

                            // Add it to the list of markers so we can keep track of it.
                            allMarkersMap.put(marker, msg);
                        }
                    }
            );
        } else {
            // Show the a message if there is no GPS fix.
            Toast.makeText(getApplicationContext(), "There is not a valid GPS fix.", Toast.LENGTH_LONG).show();
        }

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

        // Load the activity for the login.
        if (id == R.id.action_login) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
        }

        // Load the activity that enables the user to post a message.
        if (id == R.id.action_post_message) {
            Intent intent = new Intent(this, PostMessageActivity.class);
            startActivity(intent);
        }

        // Load activity that enables user to add a new friend.
        if (id == R.id.add_friend) {
            Intent myIntent = new Intent(this, AddFriendActivity.class);
            startActivity(myIntent);
        }

        // Refresh the activity giving new information if the user needs it.
        if (id == R.id.action_refresh) {
            setUpMapIfNeeded();
            Toast.makeText(getApplicationContext(), "Refreshing Map", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
