package uk.co.tomrosier.xetk.losesono.prototype.prototype.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by xetk on 25/03/15.
 */
public class DiscoverService extends Service {

    private final static int delay = 30;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("Service has been run");

        GPSService gPSS = new GPSService(getApplicationContext());

        gPSS.checkForChange();

        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // android.os.Debug.waitForDebugger();

    }

    @Override
    public void onDestroy() {
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);

        Intent resumeService = new Intent(this, DiscoverService.class);

        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + (delay * 1000),
                PendingIntent.getService(this, 0, resumeService, 0)
        );
    }

}
