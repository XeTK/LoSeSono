package uk.co.tomrosier.xetk.losesono.prototype.prototype.entities;

/**
 * Created by xetk on 26/03/15.
 */
public class Friend {
    private int linkID;
    private User user;
    private boolean isChecked = false;

    public Friend(int linkID, User user) {
        this.linkID = linkID;
        this.user   = user;
    }

    public int getLinkID() {
        return linkID;
    }

    public User getUser() {
        return user;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
