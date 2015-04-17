package uk.co.tomrosier.xetk.losesono.prototype.prototype.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.UserRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.activities.NotificationActivity;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.GPSTracker;

/**
 * This is the service that checks if we need to prompt the user with notifications.
 */
public class GPSService {

    // This is the context that holds the cookies we need to make the application authenticate with the api.
    private Context context;

    // This handles the notifications within Android and keeps a check on what notifications have been put.
    private NotificationManager notifyMgr;

    // Setup the service passing the current application context.
    public GPSService(Context context) {
        this.context = context;

        // Get the the notification manager for Android.
        notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    // TODO: Actually check for changing in location and minimise turning the gps on.
    // Check for change in the location.
    public void checkForChange() {
        GPSTracker gps = new GPSTracker(context);

        // Check if GPS enabled
        if(gps.canGetLocation()) {

            // Get the current locations. and make it easy to access.
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            if (latitude != 0 && longitude != 0) {
                // Check for items near by to the user.
                checkForNearBy(latitude,longitude);
            }
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
    }

    // This checks if there is any messages near by.
    private void checkForNearBy(final Double latitude, final Double longitude) {

        // Get the message RestClient so we can get the nearby messages.
        MessageRestClient mRC = new MessageRestClient(context);

        // Get all the messages that are usable for notifications.
        mRC.getMessagesForNotifications(
                new AjaxCompleteHandler() {
                    @Override
                    public void handleAction(Object someData) {
                        // Get the message and convert it into a message object.
                        final Message msg = (Message) someData;

                        // Calculate the distance between the two points and see if its valid to what we are doing.
                        Double val = GPSService.calculateDistance(latitude, longitude, msg.getLatitude(), msg.getLongitude());

                        System.out.println("Distance is: " + val);
                        System.out.println("Range: " + msg.getRange());

                        // If the distance between the points is less than the proximity on the message then we notify the user.
                        if (val < msg.getRange()) {

                            // Get the user rest client so we can query the users id and tag the user to the notification
                            UserRestClient uRC = new UserRestClient(context);

                            // Get the user by ID.
                            uRC.getUserByID(
                                msg.getUserID(),
                                new AjaxCompleteHandler() {
                                    @Override
                                    public void handleAction(Object someData) {
                                        // Convert the response into a User object into a Java Object that we can use.
                                        final User user = (User) someData;
                                        // Create a notification for the user.
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


    // This calculates the distance between two gps coordinates.
    // Based off http://www.movable-type.co.uk/scripts/latlong.html.
    private static double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {

        // The radius of the earth.
        long radius = 6371000;

        // Seriously do not have a idea whats going on here, Copied it from the movable-type source and just checked the variables had the same values.

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

    // This creates a notification that can be viewable in the Android notification draw.
    private void createNotification(Message msg, User user) {

        System.out.println("Creating notification");

        // Get the message ID from the message to make it easy to access.
        int msgID = msg.getMessageID();

        // Create some pending intents needed for the notification, one thats for if the notification is dismissed and one for if we want to view the notification.
        PendingIntent dismissIntent = NotificationActivity.getDismissIntent(msgID, context);
        PendingIntent realIntent    = NotificationActivity.getRealIntent(msgID,    context);

        // Build the string that is displayed in the notification preview.
        String msgStr = msg.getContent() + " - " + user.getFirstName() + " " + user.getLastName();

        // Build the notification with the details we need.
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
