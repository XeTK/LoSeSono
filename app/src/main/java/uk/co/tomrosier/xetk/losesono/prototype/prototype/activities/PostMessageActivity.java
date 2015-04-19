package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.HandleGPS;

/**
 * This is the activity that handles the posting of messages. It contains the map and the fields to add a message to a location.
 */

public class PostMessageActivity extends ActionBarActivity {

    // Make the button easy to access within the class.
    private Button btnPostTag;

    // Get the current latitude and longitude. So they are easy to access.
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message_acvitiy);

        // Get the button element so that is is easy to access.
        btnPostTag = (Button) findViewById(R.id.btnPostTag);

        // This is the onclick handler for the button to make it do things.
        btnPostTag.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    // Get the content of the message.
                    EditText content = (EditText) findViewById(R.id.txtMessage);

                    // Convert the content from the text field into a string.
                    String contStr = content.getText().toString();

                    // Get the value that is selected from the spinner holding the ranges.
                    Spinner lv = (Spinner) findViewById(R.id.PMRangeList);

                    String rangeStr = lv.getSelectedItem().toString();

                    // Very scetchy conversion from string to int.
                    int range = Integer.valueOf(rangeStr);

                    // Create a temporary message that we can use with the rest client.
                    Message message = new Message(-1, -1, false, contStr, lon, lat, range);

                    // Create a instance of the Rest Client needed for messages.
                    MessageRestClient mRC = new MessageRestClient(getApplicationContext());

                    // Call the Rest Service passing the message to be added.
                    mRC.addMessage(PostMessageActivity.this, message);
                }
            }
        );

        // Populate the list of ranges.
        Spinner lv = (Spinner) findViewById(R.id.PMRangeList);

        // Keep a list of ranges.
        ArrayList<String> ranges = new ArrayList<String>();
        ranges.add("10");
        ranges.add("50");
        ranges.add("100");
        ranges.add("250");
        ranges.add("500");
        ranges.add("1000");


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                ranges );

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lv.setAdapter(arrayAdapter);

        // Run the setup for the maps.
        setUpMapIfNeeded();
    }

    // This sets up map ready.
    private void setUpMapIfNeeded() {

        /* Side note, all Google's API documentation is out of date for this... */

        // Try to obtain the map from the SupportMapFragment.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.messageMapFragment);

        // Setup the map asynchronously,
        mapFragment.getMapAsync(
            new OnMapReadyCallback() {

                // When the map has loaded then we do stuff thats important.
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    // Setup the map for the needs we need.
                    setUpMap(googleMap);
                }
            }
        );
    }

    // Setup the map with the things important to us.
    private void setUpMap(GoogleMap mMap) {

        // Get the current location so we can tag it on the map.
        HandleGPS gps = new HandleGPS(this);

        // Set the current lat and long, so we can access them again later.
        lat = gps.getLatitude();
        lon = gps.getLongitude();

        // Check we have a valid GPS fix before we populate the maps.
        boolean validGPSFix = gps.isValidGPS();

        if (validGPSFix) {

            // Gen a new lat and long ready to make the marker.
            LatLng location = new LatLng(lat, lon);

            // The specific options for the tag.
            MarkerOptions markerOpts = new MarkerOptions();
            markerOpts.position(location);
            markerOpts.title("Add a tag to this location.");
            markerOpts.visible(true);

            // Get a marker from the options.
            Marker marker = mMap.addMarker(markerOpts);
            marker.showInfoWindow(); // Show the information handle.

            // Setup the view point for the map.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));

            // Disable the things we don't need.
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);
        }
    }
}
