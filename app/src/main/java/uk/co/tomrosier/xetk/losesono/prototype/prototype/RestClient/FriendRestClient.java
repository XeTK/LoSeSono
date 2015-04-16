package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestModel.FriendModel;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * This is the class for dealing with processing information from the friend REST services from
 * the server side of the application.
 */
public class FriendRestClient  {

    // This is the global class for doing HTTP requests for REST.
    private RestClient restClient;

    // We need to store the context as it holds the cookies that are needed for the authentication to work within the application.
    private Context context;

    // Setup the context using the default constructor to ensure that we have the cookies needed to make the application works.
    public FriendRestClient(Context context) {
        this.context = context;
        // Make the HTTP Rest Client class ready for carrying out the actions we need to do.
        restClient = new RestClient(context);
    }

    // This gets a list of all the friends tied to the user.
    public void getFriends(final AjaxCompleteHandler handler) {

        // Call the parent HTTP Rest Client class to do the dirty work.
        restClient.get(
                "friends",
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                        // If we successfully got the data then we can pass it to the Friend Model to be processed.
                        if (statusCode == 200) {
                            new FriendModel(context).processFriends(response, handler);
                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }
                }
        );
    }

    // This is a method that calls the backend to add a new friend to the current user.
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
                            new FriendModel(context).addNewFriend(user, activity);
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

    // TODO: Implement the removing of users.
    public static Object removeFriend() {
        return null;
    }

}
