package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.FriendRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.UserRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

public class AddFriendActivity extends ActionBarActivity {

    Button btnAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        btnAddFriend = (Button) findViewById(R.id.btnAddFriend);

        btnAddFriend.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        EditText txtFriendsUserName = (EditText) findViewById(R.id.txtFriendsUserName);

                        if (txtFriendsUserName != null) {

                            String friendsUsername = txtFriendsUserName.getText().toString();

                            friendsUsername = friendsUsername.trim();

                            final UserRestClient uRC = new UserRestClient(getApplicationContext());

                            System.out.println("Adding new friend");

                            uRC.getUserByName(
                                friendsUsername,
                                new AjaxCompleteHandler() {
                                    @Override
                                    public void handleAction(Object someData) {
                                        final User friend = (User) someData;

                                        FriendRestClient fRC = new FriendRestClient(getApplicationContext());

                                        fRC.addFriend(AddFriendActivity.this, friend);
                                    }
                                }
                            );

                        }

                    }
                }
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
