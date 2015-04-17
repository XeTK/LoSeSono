package uk.co.tomrosier.xetk.losesono.prototype.prototype.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class detects when the application is started.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // When the application is installed or the phone has finished booting then run this code.
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ||
                intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {

            // Start the service to detect when a message is near.
            Intent service = new Intent(context, DiscoverService.class);
            context.startService(service);
        }
    }



}
