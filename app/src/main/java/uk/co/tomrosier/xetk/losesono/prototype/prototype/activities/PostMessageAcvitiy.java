package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.HandleGPS;

public class PostMessageAcvitiy extends ActionBarActivity {

    Button btnPostTag;

    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message_acvitiy);

        btnPostTag = (Button) findViewById(R.id.btnPostTag);

        btnPostTag.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    EditText content = (EditText) findViewById(R.id.txtMessage);

                    String contStr = content.getText().toString();

                    Message message = new Message(-1, -1, false, contStr, lon, lat, 1000);

                    MessageRestClient mRC = new MessageRestClient(getApplicationContext());

                    mRC.addMessage(PostMessageAcvitiy.this, message);
                }
            }
        );


        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {


        // Try to obtain the map from the SupportMapFragment.

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.messageMapFragment);

        mapFragment.getMapAsync(
            new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Toast.makeText(getApplicationContext(), "Map Ready", Toast.LENGTH_LONG).show();

                    setUpMap(googleMap);
                }
            }
        );
    }

    private void setUpMap(GoogleMap mMap) {

        HandleGPS gps = new HandleGPS(this);

        lat = gps.getLatitude();
        lon = gps.getLongitude();

        boolean validGPSFix = gps.isValidGPS();

        if (validGPSFix) {
            LatLng location = new LatLng(lat, lon);


            MarkerOptions markerOpts = new MarkerOptions();
            markerOpts.position(location);
            markerOpts.title("Add a tag to this location.");
            markerOpts.visible(true);


            Marker marker = mMap.addMarker(markerOpts);
            marker.showInfoWindow();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));

            mMap.getUiSettings().setScrollGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);
        }
    }
}
