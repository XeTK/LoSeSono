package uk.co.tomrosier.xetk.losesono.prototype.prototype.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by xetk on 25/03/15.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ||
                intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {

            Intent service = new Intent(context, DiscoverService.class);
            context.startService(service);
        }
    }



}
