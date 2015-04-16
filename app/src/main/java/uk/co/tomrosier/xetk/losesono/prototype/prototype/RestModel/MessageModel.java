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
 * Created by xetk on 06/03/15.
 */
public class MessageModel {


    public static void processMessages(JSONArray response, final AjaxCompleteHandler handler) {

        try {

            for (int i = 0; i < response.length(); i++) {

                JSONObject obj = response.getJSONObject(i);

                Message msg = new Message(obj);

                handler.handleAction(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void addMessage(JSONObject response, Activity activity) {

        try {
            activity.finish();
            Intent myIntent = new Intent(activity, MessageFriendsActivity.class);
            myIntent.putExtra("Message_ID", response.getInt("message_id"));
            activity.startActivity(myIntent);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Toast.makeText(activity.getApplicationContext(), response.toString(),Toast.LENGTH_LONG).show();
    }

}
