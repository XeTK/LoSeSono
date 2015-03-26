package uk.co.tomrosier.xetk.losesono.prototype.prototype.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xetk on 13/03/15.
 */

/*
{
"message_id": 34,
"user_id": 2,
"private": false,
"content": "Cats",
"longitude": -1.75585,
"latitude": 51.5444,
"range": 10,
"created_date": "2015-03-23T14:46:20.924Z",
"modified_date": "2015-03-23T14:46:20.924Z",
"created_by": "application",
"modified_by": "application"
},
 */

public class Message {

    private int messageID;
    private int userID;
    private int range;

    private boolean isPrivate;

    private String content;

    private double longitude;
    private double latitude;

    private Date createdDate;

    public Message(JSONObject obj) throws JSONException {

        this.messageID = obj.getInt("message_id");
        this.userID    = obj.getInt("user_id");
        this.isPrivate = obj.getBoolean("private");
        this.content   = obj.getString("content");
        this.longitude = obj.getDouble("longitude");
        this.latitude  = obj.getDouble("latitude");
        this.range     = obj.getInt("range");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

        try {
            this.createdDate = df.parse(obj.getString("created_date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Message(int messageID, int userID, Boolean isPrivate, String content, double longitude, double latitude, int range) {
        this.messageID = messageID;
        this.userID    = userID;
        this.isPrivate = isPrivate;
        this.content   = content;
        this.longitude = longitude;
        this.latitude  = latitude;
        this.range     = range;
    }



    public boolean isPrivate() {
        return isPrivate;
    }

    public String getContent() {
        return content;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getRange() {
        return range;
    }

    public int getMessageID() {
        return messageID;
    }

    public int getUserID() {
        return userID;
    }
}
