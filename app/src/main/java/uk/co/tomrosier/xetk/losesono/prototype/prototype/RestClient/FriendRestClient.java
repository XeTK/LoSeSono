package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestModel.FriendModel;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;

/**
 * Created by xetk on 05/03/15.
 */
public class FriendRestClient  {

    RestClient restClient;

    public FriendRestClient(Context context) {
        restClient = new RestClient(context);
    }

    // TODO:
    public static Object getFriends() {
        return null;
    }


    public void addFriend(final Activity activity, final User user) {

        RequestParams params = new RequestParams();

        params.put("friend_id", user.getUserID());

        restClient.post(
                "friend/add",
                params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (statusCode == 200) {
                            FriendModel.addNewFriend(user, activity);
                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(activity.getApplicationContext(), "Status Code: " + statusCode + ", Response: " + responseString, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    // TODO:
    public static Object removeFriend() {
        return null;
    }

}
