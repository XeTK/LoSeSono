package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.services.HandleGPS;

public class PostMessageAcvitiy extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message_acvitiy);
        setUpMapIfNeeded();
    }
//messageMapFragment

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_message_acvitiy, menu);
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

    private void setUpMapIfNeeded() {


        // Try to obtain the map from the SupportMapFragment.

//            SupportMapFragment gmap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragementHolder));

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

        HandleGPS gps = new HandleGPS(PostMessageAcvitiy.this);

        double lat = gps.getLatitude();
        double lon = gps.getLongitude();

        boolean validGPSFix = gps.isValidGPS();

        if (validGPSFix) {
            LatLng location = new LatLng(lat, lon);


            MarkerOptions markerOpts = new MarkerOptions();
            markerOpts.position(location);
            markerOpts.title("Add a tag to this location.");
            markerOpts.visible(true);


            Marker marker = mMap.addMarker(markerOpts);
            marker.showInfoWindow();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));

            mMap.getUiSettings().setScrollGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }
    }
}
