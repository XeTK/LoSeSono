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
 * Created by xetk on 23/03/15.
 */
public class FriendModel {

    private Context context;

    public FriendModel(Context context) {
        this.context = context;
    }

    public void processFriends(JSONArray friends, final AjaxCompleteHandler handler) {
        try {

            for (int i = 0; i < friends.length(); i++) {

                final JSONObject friendObj = friends.getJSONObject(i);

                UserRestClient uRC = new UserRestClient(context);

                uRC.getUserByID(
                    friendObj.getInt("friend_user_id"),
                    new AjaxCompleteHandler() {
                        @Override
                        public void handleAction(Object someData) {
                            try {
                                User user = (User) someData;

                                Friend friend = new Friend(friendObj.getInt("friends_id"), user);
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

    public void addNewFriend(User user, Activity activity) {
        Toast.makeText(activity.getApplicationContext(), user.getUserNmae() + " Has been added.", Toast.LENGTH_LONG).show();
        activity.finish();
    }
}
