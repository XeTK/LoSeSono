package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.VoteType;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Comment;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Vote;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * This is for handling receiving and adding comments from the server.
 */
public class CommentRestClient {

    // Keep the rest client open so we can manipulate it later if need be.
    private RestClient restClient;

    // Keep the context easy to access as we may need for getting other objects from the server.
    private Context context;

    // Get everything ready when we create an instance of the Comment Rest Client.
    public CommentRestClient(Context context) {
        restClient = new RestClient(context);
        this.context = context;
    }

    // This gets a list of the comments tied to a specific message, then handles each object adding them to the correct places.
    public void getCommentsByID(Integer msgId, final AjaxCompleteHandler handler) {

        // This gets a list of comments from the server.
        restClient.get(
                "comments/" + msgId,
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                        // If we get a success response then we proceed.
                        if (statusCode == 200) {
                            try {

                                // If we have no comments then we kill it here.
                                if (response.length() == 0) {
                                    handler.handleAction(null);
                                } else {
                                    // Process all the comments we get back.
                                    for (int i = 0; i < response.length(); i++) {

                                        // Grab the current JSON object we are working with.
                                        final JSONObject commentObj = response.getJSONObject(i);

                                        // Get the vote rest client so we can process contents.
                                        VoteRestClient vrc = new VoteRestClient(context);

                                        // Get the votes for the given comment we are working with.
                                        vrc.getVoteByID(
                                                commentObj.getInt("comment_id"),
                                                VoteType.comment,
                                                new AjaxCompleteHandler() {
                                                    @Override
                                                    public void handleAction(Object someData) {
                                                        // Process the vote and add it to the comment, so its easily accessible.
                                                        Vote vote = (Vote) someData;
                                                        try {
                                                            // Create a new comment object to use within the application.
                                                            Comment comment = new Comment(commentObj, vote);
                                                            // Pass the comment back to the ajax handler to process it.
                                                            handler.handleAction(comment);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                        );
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            System.err.println("Getting Comments failed with status code of " + statusCode);
                        }
                    }
                }
        );
    }

    // This calls the backend to add a comment to a message.
    public void addComment(Comment comment, final AjaxCompleteHandler handler) {

        // Setup the parameters to pass back to the server.
        RequestParams params = new RequestParams();

        // Add the variables onto the header for the HTTP request.
        params.put("message_id", comment.getMessageID());
        params.put("comment", comment.getContent());

        // Post the information back to the server to add the comment to the message.
        restClient.post(
            "comments/add",
                params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Tell the application we have succeeded.
                        if (statusCode == 200) {
                            handler.handleAction(true);
                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }

                    // If all fails then notify the application.
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        handler.handleAction(false);
                    }
                }
        );
    }
}
