package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestModel;

import android.app.Activity;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.activities.MessageFriendsActivity;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * This handles the processing of messages and converting them into a usable format.
 */
public class MessageModel {

    // This processes the messages and converting them into something useable in the ui.
    public static void processMessages(JSONArray response, final AjaxCompleteHandler handler) {

        try {

            // Loop through all of the message objects that have been returned.
            for (int i = 0; i < response.length(); i++) {

                // Get the JSONObject from the array.
                JSONObject obj = response.getJSONObject(i);

                // Convert a JSON object into a Java Message object that we can then use.
                Message msg = new Message(obj);

                // Pass the object back so we can use it.
                handler.handleAction(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // This handles adding a message for a given user.
    public static void addMessage(JSONObject response, Activity activity) {

        try {
            // Close the activity once we are done processing the response.
            activity.finish();

            // Open the activity to enable adding people that can view the message.
            Intent myIntent = new Intent(activity, MessageFriendsActivity.class);
            myIntent.putExtra("Message_ID", response.getInt("message_id"));
            activity.startActivity(myIntent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
