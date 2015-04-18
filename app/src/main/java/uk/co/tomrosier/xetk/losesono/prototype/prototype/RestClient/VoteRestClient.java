package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.VoteType;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Vote;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * Created by xetk on 05/03/15.
 */
public class VoteRestClient  {

    private RestClient restClient;

    public VoteRestClient(Context context) {
        restClient = new RestClient(context);
    }

    // TODO:
    public void getVoteByID(Integer voteID, VoteType voteType, final AjaxCompleteHandler handler) {

        String url = "vote/" + voteType(voteType) + "/" + voteID;

        restClient.get(
                url,
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (statusCode == 200) {
                            try {
                                Vote vote = new Vote(response);

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

    // TODO:
    public static Object addVote() {
        return null;
    }


    private String voteType(VoteType vt) {
        if (vt == VoteType.comment) {
            return "comment";
        } else if (vt == VoteType.message) {
            return "message";
        }
        return null;
    }
}
