package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.FriendRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.UserRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * This activity enables users to add new friends to there account.
 * This helps them distribute there messages to there friends.
 */

public class AddFriendActivity extends ActionBarActivity {

    // Make the button visable to the whole activity.
    private Button btnAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        // Get the button from the view XML.
        btnAddFriend = (Button) findViewById(R.id.btnAddFriend);

        // Bind the button to carry out the adding of a friend.
        btnAddFriend.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        // Grab the edit text field so we can work on it.
                        EditText txtFriendsUserName = (EditText) findViewById(R.id.txtFriendsUserName);

                        // Check that we got the text field correctly and it isn't null.
                        if (txtFriendsUserName != null) {

                            // Grab the string that has been entered into the text box.
                            String friendsUsername = txtFriendsUserName.getText().toString();

                            // Trim the text so there is no space around the string.
                            friendsUsername = friendsUsername.trim();

                            // Prepare the restclient ready for adding a user.
                            final UserRestClient uRC = new UserRestClient(getApplicationContext());

                            System.out.println("Adding new friend");

                            // Call the rest service passing the username to get the correct user_id. for adding them.
                            uRC.getUserByName(
                                friendsUsername,
                                new AjaxCompleteHandler() {
                                    @Override
                                    public void handleAction(Object someData) {
                                        final User friend = (User) someData;

                                        // Call the friends rest service for the application passing the user_id of the person we want to add to the server.
                                        FriendRestClient fRC = new FriendRestClient(getApplicationContext());

                                        // Call the function that carrys out the adding of friends.
                                        fRC.addFriend(AddFriendActivity.this, friend);

                                        // Close the activity once we are done.
                                        finish();
                                    }
                                }
                            );

                        }

                    }
                }
        );

    }
}
