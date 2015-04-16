package uk.co.tomrosier.xetk.losesono.prototype.prototype.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.UserRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.activities.NotificationActivity;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.GPSTracker;

/**
 * Created by xetk on 25/03/15.
 */
public class GPSService {

    Context context;

    NotificationManager notifyMgr;


    public GPSService(Context context) {
        this.context = context;

        notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void checkForChange() {
        GPSTracker gps = new GPSTracker(context);

        // Check if GPS enabled
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            if (latitude != 0 && longitude != 0) {
                checkForNearBy(latitude,longitude);
            } else {
                Toast.makeText(context, "No fix yet", Toast.LENGTH_LONG).show();
            }
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
    }

    private void checkForNearBy(final Double latitude, final Double longitude) {

        MessageRestClient mRC = new MessageRestClient(context);

        mRC.getMessagesForNotifications(
                new AjaxCompleteHandler() {
                    @Override
                    public void handleAction(Object someData) {
                        final Message msg = (Message) someData;

                        Double val = GPSService.calculateDistance(latitude, longitude, msg.getLatitude(), msg.getLongitude());

                        System.out.println("Distance is: " + val);
                        System.out.println("Range: " + msg.getRange());

                        if (val < msg.getRange()) {

                            UserRestClient uRC = new UserRestClient(context);

                            uRC.getUserByID(
                                msg.getUserID(),
                                new AjaxCompleteHandler() {
                                    @Override
                                    public void handleAction(Object someData) {
                                        final User user = (User) someData;
                                        createNotification(msg, user);
                                    }
                                }
                            );


                            System.out.println("Alert peoples: " + val);
                        }
                    }
                }
        );
    }

    private static double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {

        long radius = 6371000;

        double la1 = Math.toRadians(lat1);
        double la2 = Math.toRadians(lat2);

        double lo1 = Math.toRadians(lon1);
        double lo2 = Math.toRadians(lon2);

        double lat = (la2 - la1);
        double lon = (lo2 - lo1);

        double a = Math.sin(lat / 2) * Math.sin(lat / 2) +
                   Math.cos(la1)     * Math.cos(la2) *
                   Math.sin(lon / 2) * Math.sin(lon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double distant = radius * c;

        return distant;
    }

    private void createNotification(Message msg, User user) {

        System.out.println("Creating notification");


        int msgID = msg.getMessageID();

        PendingIntent dismissIntent = NotificationActivity.getDismissIntent(msgID, context);
        PendingIntent realIntent    = NotificationActivity.getRealIntent(msgID,    context);

        String msgStr = msg.getContent() + " - " + user.getFirstName() + " " + user.getLastName();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder .setDefaults(Notification.DEFAULT_ALL) // also requires VIBRATE permission
                .setSmallIcon(R.drawable.icon) // Required!
                .setContentTitle("Tag nearby")
                .setContentText(msgStr)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(0, "View Tag", realIntent)
                .addAction(0, "Dismiss Tag", dismissIntent);

        // Builds the notification and issues it.
        notifyMgr.notify(msgID, builder.build());


    }
}
