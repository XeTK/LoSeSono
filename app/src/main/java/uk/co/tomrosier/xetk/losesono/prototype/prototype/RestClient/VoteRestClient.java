package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.ActionEffect;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.VoteType;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Vote;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * This is dealing with all vote related requests to and from the server.
 */
public class VoteRestClient  {

    // Parent Rest Client ready for dealing with HTTP requests.
    private RestClient restClient;

    // Setup the class ready for dealing with requests.
    public VoteRestClient(Context context) {
        restClient = new RestClient(context);
    }

    // This gets vote objects from the server from by there id's.
    public void getVoteByID(Integer voteID, VoteType voteType, final AjaxCompleteHandler handler) {

        // Build the url for getting the votes.
        String url = "vote/" + voteType(voteType) + "/" + voteID;

        // Get the vote objects from the server.
        restClient.get(
                url,
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If we were successful in getting the objects then we continue.
                        if (statusCode == 200) {
                            try {
                                // Generate the vote object.
                                Vote vote = new Vote(response);

                                // Pass it back to the method that called the request to get the vote.
                                handler.handleAction(vote);
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

    // This adds a new vote to the either the comment or message. Pass some info to the server to process it.
    public void addVote(int id, VoteType voteType, ActionEffect actionEffect, final AjaxCompleteHandler handler) {

        // Build the url for the destination of the request.
        String url = "vote/" + voteType(voteType) + "/add";

        String type = new String();

        // TODO: convert to enums that return there own values.
        // This converts the enums into values.
        if (actionEffect == ActionEffect.positive) {
            type = "positive";
        } else if (actionEffect == ActionEffect.negative) {
            type = "negative";
        }

        // Build the perimeter list.
        RequestParams params = new RequestParams();

        // Attach the parameters we need.
        params.put("id",  id);
        params.put("type", type);

        // Send the request to the server.
        restClient.post(
                url,
                params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If we succeded in posting the data to the server we tell the method that tried to add the vote.
                        if (statusCode == 200) {
                            handler.handleAction(true);
                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                        }
                    }

                    // If all else fails tell the method we failed.
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        handler.handleAction(false);
                    }
                }
        );
    }

    // TODO: convert to enums that return there own values.
    // This converts the enums into values.
    private String voteType(VoteType vt) {
        if (vt == VoteType.comment) {
            return "comment";
        } else if (vt == VoteType.message) {
            return "message";
        }
        return null;
    }
}
