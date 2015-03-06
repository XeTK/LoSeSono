package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestModel;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;

/**
 * Created by xetk on 06/03/15.
 */
public class MessageModel {


    public static void processMessages(JSONArray response, Activity activity) {

        try {

            for (int i = 0; i < response.length(); i++) {

                JSONObject obj = response.getJSONObject(i);

                String res = obj.toString();

                System.out.println(res);

                TextView lblJSON = (TextView) activity.findViewById(R.id.jsonStr);

                String msg = "Content: " + obj.getString("content")
                        + "\nLongitude: " + obj.getDouble("longitude")
                        + "\nLatitude: " + obj.getDouble("latitude");

                lblJSON.setText(msg);

                Toast.makeText(activity.getApplicationContext(), res, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void addMessage(JSONObject response, Activity activity) {

        Toast.makeText(activity.getApplicationContext(), response.toString(),Toast.LENGTH_LONG).show();
    }

}
