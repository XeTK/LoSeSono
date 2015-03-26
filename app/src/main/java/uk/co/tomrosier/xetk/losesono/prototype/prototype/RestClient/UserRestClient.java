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
 * Created by xetk on 05/03/15.
 */
public class UserRestClient  {

    RestClient restClient;

    public UserRestClient(Context context) {
        restClient = new RestClient(context);
    }


    public void getUserByID(Integer id, final AjaxCompleteHandler handler) {
        userRequest("user/id/" + id, handler);
    }

    public void getUserByName(String userID, final AjaxCompleteHandler handler) {
        userRequest("user/username/" + userID, handler);
    }

    public void getUser(final AjaxCompleteHandler handler) {
        userRequest("user", handler);
    }

    private void userRequest(String url, final AjaxCompleteHandler handler) {
        restClient.get(
                url,
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        if (statusCode == 200) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);

                                    User userObj = new User(obj);

                                    handler.handleAction(userObj);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }
                }
        );
    }

}
