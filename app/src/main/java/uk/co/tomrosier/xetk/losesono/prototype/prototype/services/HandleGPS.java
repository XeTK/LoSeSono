package uk.co.tomrosier.xetk.losesono.prototype.prototype.services;

import android.content.Context;

/**
 * Created by Tom Rosier on 16/02/15.
 * This is a wrapper class for the GPS tracker.
 */
public class HandleGPS {

    // GPSTracker class
    GPSTracker gps;

    // Keep the current context we are using.
    Context context;

    // Hold the GPS location.
    private double latitude  = 0;
    private double longitude = 0;

    // Default constructor, call the update GPS method.
    public HandleGPS(Context context) {
        gpsUpdate(context);
    }

    // Resets the context and updates the GPS location.
    public void gpsUpdate(Context context) {
        this.context = context;
        HandleGPS();
    }

    // This collects the latest GPS location.
    private void HandleGPS() {
        // Create gps tracker. object.
        gps = new GPSTracker(context);

        // Check if GPS enabled
        if(gps.canGetLocation()) {

            // Set the most current GPS locations got from the tracker.
            this.latitude  = gps.getLatitude();
            this.longitude = gps.getLongitude();

        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
    }

    // Check if we have a bad gps fix.
    public boolean isValidGPS() {
        return !(latitude == 0 || longitude == 0);
    }

    // Getters.
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
