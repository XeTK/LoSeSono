package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * This handles the dealing with notifications, as we decide if we want to either clear the notification or we go to the tag on the map.
 */
public class NotificationActivity extends Activity {

    // The id of the notification.
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This gets the list of notifications that have already been created.
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Get the id of the message that we are working with that is the notification.
        int msgID = getIntent().getIntExtra(NOTIFICATION_ID, -1);

        // Clear the notification from the list that shows the notifications.
        manager.cancel(msgID);

        // Show that we are clearing all of the notificiations.
        Toast.makeText(getApplicationContext(), "Clearing Notification: " + msgID, Toast.LENGTH_LONG).show();

        // Close this activity when we are done.
        finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
    }

    // This clears the notification and marks it as already read.
    public static PendingIntent getDismissIntent(int notificationId, Context context) {

        // Get the activity ready which is this activity.
        Intent intent = new Intent(context, NotificationActivity.class);

        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Put the id's of the notification we want to cancel.
        intent.putExtra(NOTIFICATION_ID, notificationId);
        intent.setAction("Notification: " + notificationId);

        // Create the pending intent ready for us to clear it.
        PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Return the pending intent.
        return dismissIntent;
    }

    // This loads the activity to display the notification.
    public static PendingIntent getRealIntent(int messageID, Context context) {

        // Create a intent to the activity that displays the messages.
        Intent resultIntent = new Intent(context, ViewMessage.class);

        // Pass the message_id to the activity to view the message that has been left.
        resultIntent.putExtra("MsgObj", messageID);
        resultIntent.setAction("Notification: " + messageID);

        // Create a stack that holds the other activityies that need to be in the stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Put a activity to fall back once we have finished with the message.
        stackBuilder.addParentStack(NavigationActivity.class);

        // Add the pending intent.
        stackBuilder.addNextIntent(resultIntent);

        // return the pending intent.
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
    }

}
