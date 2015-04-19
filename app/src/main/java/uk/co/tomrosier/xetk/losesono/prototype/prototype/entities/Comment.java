package uk.co.tomrosier.xetk.losesono.prototype.prototype.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This is a comment within the application and is shown on the tags.
 */
public class Comment {

    // This is the ID's to link everything together correctly.
    private int commentID;
    private int messageID;
    private int userID;

    // This is the actual comment string.
    private String content;

    // User object so we can refrence it later.
    private User user;

    // This is the date the comment was created.
    private Date createdDate;

    // This is the rating of the comment, we keep this to make it easy to access later.
    private Vote vote;

    // Create the object from the info passed in from the server to make a Java representation of the server object.
    public Comment(JSONObject obj, Vote vote) throws JSONException {
        this.commentID = obj.getInt("comment_id");
        this.messageID = obj.getInt("message_id");
        this.userID    = obj.getInt("user_id");
        this.content   = obj.getString("content");

        // Convert the date into something we can actually use.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

        try {
            this.createdDate = df.parse(obj.getString("created_date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.vote = vote;
    }

    // This is if we don't create the object from the server object.
    public Comment(int messageID, User user, String content) {
        this.messageID = messageID;
        this.user      = user;
        this.content   = content;
        this.userID    = user.getUserID();
    }

    // Getter's and setter's.

    public int getCommentID() {
        return commentID;
    }

    public int getMessageID() {
        return messageID;
    }

    public int getUserID() {
        return userID;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public Vote getVote() {
        return vote;
    }
}
