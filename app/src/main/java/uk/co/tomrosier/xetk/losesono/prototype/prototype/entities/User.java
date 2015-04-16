package uk.co.tomrosier.xetk.losesono.prototype.prototype.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a user object within this application. Just a data structure to keep information in.
 */

// JSON object class is based off.
// [{"user_id":2,"first_name":"Tom","last_name":"Rosier","username":"XeTK","created_date":"2015-03-09T16:08:03.140Z","modified_date":"2015-03-09T16:08:03.140Z","created_by":"xetk","modified_by":"xetk"}]
public class User {

    // The user_id for the current user.
    private int userID;

    // The names for the user.
    private String firstName;
    private String lastName;
    private String userNmae;

    // Convert the JSONObject into a Java User Object.
    public User(JSONObject obj) throws JSONException {
        this.userID    = obj.getInt("user_id");
        this.firstName = obj.getString("first_name");
        this.lastName  = obj.getString("last_name");
        this.userNmae  = obj.getString("username");
    }

    // Getter for the fields within the class.

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
