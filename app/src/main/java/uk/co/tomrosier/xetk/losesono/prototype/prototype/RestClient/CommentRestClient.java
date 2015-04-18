package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Comment;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * Created by xetk on 05/03/15.
 */
public class CommentRestClient {

    private RestClient restClient;

    public CommentRestClient(Context context) {
        restClient = new RestClient(context);
    }

    public void getCommentsByID(Integer msgId, final AjaxCompleteHandler handler) {

        restClient.get(
                "comments/" + msgId,
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        if (statusCode == 200) {
                            try {

                                if (response.length() == 0) {
                                    handler.handleAction(null);
                                } else {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject commentObj = response.getJSONObject(i);

                                        Comment comment = new Comment(commentObj);
                                        handler.handleAction(comment);

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

    public void addComment(Comment comment, final AjaxCompleteHandler handler) {

        RequestParams params = new RequestParams();

        params.put("message_id", comment.getMessageID());
        params.put("comment", comment.getContent());

        restClient.post(
            "comments/add",
                params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (statusCode == 200) {
                            handler.handleAction(true);
                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        handler.handleAction(false);
                    }
                }
        );
    }
}
