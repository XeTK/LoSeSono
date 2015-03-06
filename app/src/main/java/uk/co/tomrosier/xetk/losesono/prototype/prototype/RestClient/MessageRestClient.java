package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient;

import android.app.Activity;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestModel.MessageModel;

/**
 * Created by xetk on 05/03/15.
 */
public class MessageRestClient {

    public MessageRestClient() {}

    // TODO:
    public static Object getMessageByID(Integer id) {
        return null;
    }

    // TODO:
    public static void getMessages(final Activity activity) {

        RestClient.get(
            "messages",
            null,
            new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if (statusCode == 200) {
                        MessageModel.processMessages(response, activity);
                    } else {
                        System.err.println("Getting Messages failed with status code of " + statusCode);
                    }
                }
            }
        );
    }

    // TODO:
    public static void addMessage(final Activity activity) {

        RequestParams params = new RequestParams();

        params.put("user_id", "1");
        params.put("private", "false");
        params.put("content", "hello world  1");
        params.put("longitude", "54.1");
        params.put("latitude", "45.1");
        params.put("range", "10");

        RestClient.post(
                "message/add",
                params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (statusCode == 200) {
                            MessageModel.addMessage(response, activity);
                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }
                }
        );
    }

}
