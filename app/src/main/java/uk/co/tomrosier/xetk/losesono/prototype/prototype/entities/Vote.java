package uk.co.tomrosier.xetk.losesono.prototype.prototype.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This represents a vote object from the server in the application. When it has been returned from the server.
 */
public class Vote {

    // Stored the number of positive and negative votes we have for either the comment or message.
    private int positive;
    private int negative;

    // Convert the object that we get in from the server.
    public Vote(JSONObject jsonObj) throws JSONException {
        this.positive = jsonObj.getInt("positive");
        this.negative = jsonObj.getInt("negative");
    }

    // If we are not building from the server then we manually provide the number of votes for a given object.
    public Vote(int positive, int negative) {
        this.positive = positive;
        this.negative = negative;
    }

    // Getters.

    public int getPositive() {
        return positive;
    }

    public int getNegative() {
        return negative;
    }
}
