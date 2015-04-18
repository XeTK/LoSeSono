package uk.co.tomrosier.xetk.losesono.prototype.prototype.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xetk on 17/04/15.
 */
public class Comment {

    private int commentID;
    private int messageID;
    private int userID;

    private String content;

    private User user;

    private Date createdDate;


    public Comment(JSONObject obj) throws JSONException {
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
    }

    public Comment(int messageID, User user, String content) {
        this.messageID = messageID;
        this.user      = user;
        this.content   = content;
        this.userID    = user.getUserID();
    }

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
}
