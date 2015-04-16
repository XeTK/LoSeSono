package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.FriendArrayAdapter;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.FriendRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Friend;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * This is the activity for adding the visability to friends for a given message.
 */

public class MessageFriendsActivity extends ActionBarActivity {

    // List of the items shown on the screen.
    private ArrayList<Friend> listItems=new ArrayList<Friend>();

    /// This is the adapter that shows the listview on the screen.
    private FriendArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_friends);

        // Get the list view from the layout so we can add items to it.
        ListView lw = (ListView) findViewById(R.id.ListViewFriends);

        // Setup the display adapter to add the items to.
        adapter = new FriendArrayAdapter(this, listItems);

        // Set the view point for the listview.
        lw.setAdapter(adapter);

        // Get the bundle from the intent.
        Bundle extras = getIntent().getExtras();

        // Check the extras see if we have any.
        if (extras != null) {

            // Get the message_id that has been passed to the activity.
            final int messageID = extras.getInt("Message_ID");

            // Get the restclient for dealing with friends.
            FriendRestClient fRC = new FriendRestClient(this);

            // Get the list of friends from the server.
            fRC.getFriends(
                new AjaxCompleteHandler() {
                    @Override
                    public void handleAction(Object someData) {
                        // What we do with each friends in the list.
                        Friend user = (Friend)someData;

                        // Add each friend to the list.
                        listItems.add(user);
                        // Show the data has been changed once we added.
                        adapter.notifyDataSetChanged();
                    }
                }
            );

            // Get the button for submitting the data.
            Button BtnSelectFriends = (Button) findViewById(R.id.BtnSelectFriends);

            // Process the selected friends on the button press.
            BtnSelectFriends.setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // For each of the friends, in the list view.
                            for (int i = 0; i < adapter.getCount(); i++) {
                                // Get the friend object from the adapter view.
                                Friend fi = adapter.getItem(i);

                                // Get the message rest client ready to append the friend to the message.
                                MessageRestClient mRC = new MessageRestClient(getApplicationContext());

                                // Add the user to the message.
                                mRC.addUser(
                                    new AjaxCompleteHandler() {
                                        @Override
                                        public void handleAction(Object someData) {
                                            int groupID = (int) someData;
                                            System.out.println("GroupID: " + groupID);
                                        }
                                    },
                                    messageID,
                                    fi.getLinkID()
                                );

                                // Close the activity.
                                finish();
                                // Go back to the home activity.
                                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_friends, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_friend) {

            // Show the dialog for adding a new friends.
            Intent myIntent = new Intent(MessageFriendsActivity.this, AddFriendActivity.class);
            MessageFriendsActivity.this.startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
