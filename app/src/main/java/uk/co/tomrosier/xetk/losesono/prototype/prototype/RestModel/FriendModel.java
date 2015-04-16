package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestModel;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.UserRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Friend;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * This processes the data returned from the Rest Client, and makes it something that can be used within the front end application.
 */
public class FriendModel {

    // Keep the context for later use.
    private Context context;

    // Setup the class keeping hold of the context that is needed for the operations to work successfully.
    public FriendModel(Context context) {
        this.context = context;
    }

    // This processes a list JSONObjects with the users friends details held within it. and converts it to something more useable.
    public void processFriends(JSONArray friends, final AjaxCompleteHandler handler) {
        try {

            // Get the User rest client ready to get the user object for each friend object.
            UserRestClient uRC = new UserRestClient(context);

            // For each item in the JSON array, we process it dependandly
            for (int i = 0; i < friends.length(); i++) {

                // Get the JSON object from the JSON array.
                final JSONObject friendObj = friends.getJSONObject(i);

                // Get the user by there ID.
                uRC.getUserByID(
                    friendObj.getInt("friend_user_id"),
                    new AjaxCompleteHandler() {
                        @Override
                        public void handleAction(Object someData) {
                            try {
                                // Convert the data into a user object.
                                User user = (User) someData;

                                // Create a friend object pinning the user object to the friend object.
                                Friend friend = new Friend(friendObj.getInt("friends_id"), user);

                                // Pass the friend back to the method that called it.
                                handler.handleAction(friend);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // TODO: what the hell was a I thinking here I really have no idea...!
    // This method seems a bit pointless but im not going to remove it...
    public void addNewFriend(User user, Activity activity) {
        // Show a notification saying that a user has been added correctly...
        Toast.makeText(activity.getApplicationContext(), user.getUserNmae() + " Has been added.", Toast.LENGTH_LONG).show();
        activity.finish();
    }
}
