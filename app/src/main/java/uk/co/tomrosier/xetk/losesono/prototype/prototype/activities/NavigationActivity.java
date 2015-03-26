package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.HandleGPS;

public class NavigationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapsfragment);

        mapFragment.getMapAsync(
            new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Toast.makeText(getApplicationContext(),"Map Ready", Toast.LENGTH_LONG).show();

                    //googleMap.getUiSettings().setScrollGesturesEnabled(false);
                    //googleMap.getUiSettings().setZoomGesturesEnabled(false);

                    googleMap.getUiSettings().setMapToolbarEnabled(false);
                    googleMap.getUiSettings().setCompassEnabled(false);
                    googleMap.getUiSettings().setRotateGesturesEnabled(false);

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
            mMap.addMarker(mo);
        }

        MessageRestClient mRC = new MessageRestClient(this);
        mRC.getMessages(
            new AjaxCompleteHandler() {
                @Override
                public void handleAction(Object someData) {
                    Message msg = (Message) someData;

                    MarkerOptions mo = new MarkerOptions();
                    mo.position(new LatLng(msg.getLatitude(),msg.getLongitude()));
                    mo.title(msg.getContent());
                    mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mMap.addMarker(mo);
                }
            }
        );

    }
}
