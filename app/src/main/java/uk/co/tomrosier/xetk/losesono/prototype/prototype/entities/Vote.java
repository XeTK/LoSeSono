package uk.co.tomrosier.xetk.losesono.prototype.prototype.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xetk on 18/04/15.
 */
public class Vote {

    private int positive;
    private int negative;

    public Vote(JSONObject jsonObj) throws JSONException {
        this.positive = jsonObj.getInt("positive");
        this.negative = jsonObj.getInt("negative");
    }

    public Vote(int positive, int negative) {
        this.positive = positive;
        this.negative = negative;
    }


    public int getPositive() {
        return positive;
    }

    public int getNegative() {
        return negative;
    }
}
