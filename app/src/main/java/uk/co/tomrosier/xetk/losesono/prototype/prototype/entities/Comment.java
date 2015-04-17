package uk.co.tomrosier.xetk.losesono.prototype.prototype.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xetk on 17/04/15.
 */
public class Comment {

    private int commentID;
    private int messageID;
    private int userID;

    private String content;

    private User user;

    public Comment(JSONObject obj) throws JSONException {
        commentID = obj.getInt("comment_id");
        messageID = obj.getInt("message_id");
        userID    = obj.getInt("user_id");
        content   = obj.getString("content");
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

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
