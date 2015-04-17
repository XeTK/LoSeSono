package uk.co.tomrosier.xetk.losesono.prototype.prototype.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * This is the service that runs every so often to check if the user is close by to a specific message.
 */
public class DiscoverService extends Service {

    // Set the delay in secounds between checking again for new notifications.
    private final static int delay = 30;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Load up the service when the services are deployed.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("Service has been run");

        // Get the current GPS location and check for changes.
        GPSService gPSS = new GPSService(getApplicationContext());

        gPSS.checkForChange();

        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Needed to debug services.
        // android.os.Debug.waitForDebugger();

    }

    @Override
    public void onDestroy() {
        // Setup alarm to run services again in x amount of time.
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);

        Intent resumeService = new Intent(this, DiscoverService.class);

        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + (delay * 1000),
                PendingIntent.getService(this, 0, resumeService, 0)
        );
    }

}
