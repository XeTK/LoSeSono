package uk.co.tomrosier.xetk.losesono.prototype.prototype.entities;

/**
 * This is a friend object within the application. Converted from a server side to client side.
 */
public class Friend {

    // This is the id of the link between two users.
    private int linkID;

    // This this is the user object for the friend.
    private User user;

    // This is if the checkbox is checked when this class is used in the list view.
    private boolean isChecked = false;

    // Setup the class with the information we need.
    public Friend(int linkID, User user) {
        this.linkID = linkID;
        this.user   = user;
    }

    // Getters for the class.

    public int getLinkID() {
        return linkID;
    }

    public User getUser() {
        return user;
    }

    // Setting of the checkbox when used in a listview.

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
