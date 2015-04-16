package uk.co.tomrosier.xetk.losesono.prototype.prototype.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This is a message object within the application converted from the server side, to the client side.
 */

// JSONObject that the class is based off.
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

    // This is the id's that are needed to reference the other things within the application.
    private int messageID;
    private int userID;

    // Range on the message, the proximity the message is valid for.
    private int range;

    // Is it a global message or is it only available to the users friends.
    private boolean isPrivate;

    // This is the message's content e.g the actual message.
    private String content;

    // This is the location on the map where there tag has been placed.
    private double longitude;
    private double latitude;

    // This is the date that the message was created.
    private Date createdDate;

    // Generate the message from a JSONObejct that is passed in so we can do rawr processing on the objects direct from the restclient.
    public Message(JSONObject obj) throws JSONException {

        this.messageID = obj.getInt("message_id");
        this.userID    = obj.getInt("user_id");
        this.isPrivate = obj.getBoolean("private");
        this.content   = obj.getString("content");
        this.longitude = obj.getDouble("longitude");
        this.latitude  = obj.getDouble("latitude");
        this.range     = obj.getInt("range");

        // Convert the date into something we can actually use.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

        try {
            this.createdDate = df.parse(obj.getString("created_date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Generate a message by giving all of the parameters.
    public Message(int messageID, int userID, Boolean isPrivate, String content, double longitude, double latitude, int range) {
        this.messageID = messageID;
        this.userID    = userID;
        this.isPrivate = isPrivate;
        this.content   = content;
        this.longitude = longitude;
        this.latitude  = latitude;
        this.range     = range;
    }

    // Getters for the variables.

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
