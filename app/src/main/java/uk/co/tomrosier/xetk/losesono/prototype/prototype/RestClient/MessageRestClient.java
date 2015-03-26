package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient;

import android.app.Activity;
import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestModel.MessageModel;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * Created by xetk on 05/03/15.
 */
public class MessageRestClient {

    RestClient restClient;

    public MessageRestClient(Context context) {
        restClient = new RestClient(context);
    }

    public void getMessageByID(Integer id, final AjaxCompleteHandler handler) {

        restClient.get(
                "message/" + id,
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (statusCode == 200) {
                            try {
                                Message msg = new Message(response);

                                handler.handleAction(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                        System.err.println("Getting Messages failed with status code of " + statusCode);
                    }

                }
        );
    }

    public void getMessages(final AjaxCompleteHandler handler) {

        restClient.get(
            "messages",
            null,
            new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if (statusCode == 200) {
                        MessageModel.processMessages(response, handler);
                    } else {
                        System.err.println("Getting Messages failed with status code of " + statusCode);
                    }
                }
            }
        );
    }

    public void addMessage(final Activity activity, Message message) {

        RequestParams params = new RequestParams();

        params.put("private", message.isPrivate());
        params.put("content", message.getContent());
        params.put("longitude", message.getLongitude());
        params.put("latitude", message.getLatitude());
        params.put("range", message.getRange());

        restClient.post(
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
