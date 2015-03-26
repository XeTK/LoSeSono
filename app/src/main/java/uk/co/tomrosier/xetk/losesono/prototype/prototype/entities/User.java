package uk.co.tomrosier.xetk.losesono.prototype.prototype.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xetk on 23/03/15.
 */

// [{"user_id":2,"first_name":"Tom","last_name":"Rosier","username":"XeTK","created_date":"2015-03-09T16:08:03.140Z","modified_date":"2015-03-09T16:08:03.140Z","created_by":"xetk","modified_by":"xetk"}]
public class User {

    private int userID;

    private String firstName;
    private String lastName;
    private String userNmae;

    public User(JSONObject obj) throws JSONException {
        this.userID    = obj.getInt("user_id");
        this.firstName = obj.getString("first_name");
        this.lastName  = obj.getString("last_name");
        this.userNmae  = obj.getString("username");
    }


    public int getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserNmae() {
        return userNmae;
    }
}
