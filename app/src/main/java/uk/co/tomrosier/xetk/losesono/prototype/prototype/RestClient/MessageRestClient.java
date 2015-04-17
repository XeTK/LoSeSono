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
 * This is the HTTP REST Client it deals with receiving and processing messages within the applications.
 */
public class MessageRestClient {

    // Keep the higher REST Client object ready for us to access it.
    private RestClient restClient;

    // Create a instance of the REST Client when this class is instantiated.
    public MessageRestClient(Context context) {
        restClient = new RestClient(context);
    }

    // This gets a message by the message_id, and converting it to Java message objects.
    public void getMessageByID(Integer id, final AjaxCompleteHandler handler) {

        restClient.get(
                "message/" + id,
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the request returns succesfully then we continue to process the messages.
                        if (statusCode == 200) {
                            try {
                                // Convert the responce into a Java Message object.
                                Message msg = new Message(response);

                                // Pass the message back to the method that called to get messages.
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

    // Get all the messages for the current user that is signed in.
    public void getMessagesForUser(final AjaxCompleteHandler handler) {

        restClient.get(
            "messages/user",
            null,
            new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // If the status is successful, then we continue.
                    if (statusCode == 200) {
                        // Pass the response to the Message Model to process and handle within the UI.
                        MessageModel.processMessages(response, handler);
                    } else {
                        System.err.println("Getting Messages failed with status code of " + statusCode);
                    }
                }
            }
        );
    }

    // Get all the messages that are related to the user that is currently logged in.
    public void getMessagesForFriends(final AjaxCompleteHandler handler) {

        restClient.get(
                "messages/friends",
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        if (statusCode == 200) {
                            // Pass the response to the Message Model to process and handle within the UI.
                            MessageModel.processMessages(response, handler);
                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }
                }
        );
    }

    // This gets the message that are needed for the notificiations within the application.
    public void getMessagesForNotifications(final AjaxCompleteHandler handler) {

        restClient.get(
                "messages/notifications",
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        if (statusCode == 200) {
                            // This processes the responce within the Message Model,
                            MessageModel.processMessages(response, handler);
                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }
                }
        );
    }

    // This calls adding a message within the back end of the application.
    public void addMessage(final Activity activity, Message message) {

        // Define a list of parameters to pass over the POST request.
        RequestParams params = new RequestParams();

        // Adding the parameters for building a message.
        params.put("private", message.isPrivate());
        params.put("content", message.getContent());
        params.put("longitude", message.getLongitude());
        params.put("latitude", message.getLatitude());
        params.put("range", message.getRange());

        // Send the request off to the backend of the application.
        restClient.post(
                "message/add/message",
                params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (statusCode == 200) {
                            // Process the message, and handle the other activities.
                            MessageModel.addMessage(response, activity);
                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }
                }
        );
    }

    // This adds a user to message and makes it viewable to other people.
    public void addUser(final AjaxCompleteHandler handler, int messageID, int friendLinkID) {

        // This is the parameters that need to be passed to the backend application.
        RequestParams params = new RequestParams();

        params.put("friend_id", friendLinkID);
        params.put("message_id", messageID);

        // Send this info to the server.
        restClient.post(
                "message/add/user",
                params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the response returns successfully.
                        if (statusCode == 200) {
                            try {
                                // Pass the group_id back to the method that called adding a user to a message.
                                handler.handleAction(response.getInt("group_id"));
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

    // This handles when a message is read, so we dont get notified multiple times for a message that has been read.
    public void addMessageRead(int messageID, final AjaxCompleteHandler handler) {

        // Pass the message_id to the server so that we can log it has been read.
        RequestParams params = new RequestParams();

        params.put("message_id", messageID);

        // Send the details of the REST request over to the server to log it as read.
        restClient.post(
                "message/read",
                params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the status is returned successfully.
                        if (statusCode == 200) {
                            try {
                                // Pass the group_id even if we dont use it back to the method that called it.
                                handler.handleAction(response.getInt("group_id"));
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
