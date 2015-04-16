package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * This is the Rest Client for dealing with interaction with user objects on the server.
 */
public class UserRestClient  {

    // Declare the global Rest Client we interact with.
    private RestClient restClient;

    // Declare the Rest client and pass the context so we have cookies to work with.
    public UserRestClient(Context context) {
        restClient = new RestClient(context);
    }

    // Build the URL needed to get the user by id, then call the mater method to process the information.
    public void getUserByID(Integer id, final AjaxCompleteHandler handler) {
        userRequest("user/id/" + id, handler);
    }

    // Build the URL needed to get the user by username, then call the mater method to process the information.
    public void getUserByName(String userID, final AjaxCompleteHandler handler) {
        userRequest("user/username/" + userID, handler);
    }

    // This is getting the user object for the current user that is logged in.
    public void getUser(final AjaxCompleteHandler handler) {
        userRequest("user", handler);
    }

    // This handles the requests with the user rest clients, dealing with the AJAX handling and objects.
    private void userRequest(String url, final AjaxCompleteHandler handler) {

        // Get the information from the server.
        restClient.get(
                url,
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        // If we got the data successfully.
                        if (statusCode == 200) {
                            try {
                                // For each of the users we get back, we call the ajax handler, to process the responce.
                                for (int i = 0; i < response.length(); i++) {
                                    // Get the JSON object from the JSON array to manipulate
                                    JSONObject obj = response.getJSONObject(i);

                                    // Convert the JSONObject into a actual user object that then can be used.
                                    User userObj = new User(obj);

                                    // Pass that user object back to the AJAX handler.
                                    handler.handleAction(userObj);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            // Sometimes things break.
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }
                }
        );
    }

}
