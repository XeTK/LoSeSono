package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.FriendArrayAdapter;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.FriendRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Friend;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

public class MessageFriendsActivity extends ActionBarActivity {

    ArrayList<Friend> listItems=new ArrayList<Friend>();

    FriendArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_friends);

        ListView lw = (ListView) findViewById(R.id.ListViewFriends);

        adapter = new FriendArrayAdapter(this, listItems);

        lw.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            final int messageID = extras.getInt("Message_ID");
            Toast.makeText(getApplicationContext(), "Message_id: " + messageID, Toast.LENGTH_LONG).show();

            FriendRestClient fRC = new FriendRestClient(this);

            fRC.getFriends(
                new AjaxCompleteHandler() {
                    @Override
                    public void handleAction(Object someData) {
                        Friend user = (Friend)someData;
                        listItems.add(user);
                        adapter.notifyDataSetChanged();
                    }
                }
            );

            Button BtnSelectFriends = (Button) findViewById(R.id.BtnSelectFriends);

            BtnSelectFriends.setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            for (int i = 0; i < adapter.getCount(); i++) {
                                Friend fi = adapter.getItem(i);

                                String help = fi.getUser().getUserNmae() + " " + fi.isChecked();

                                Toast.makeText(getApplicationContext(), help, Toast.LENGTH_LONG).show();

                                MessageRestClient mRC = new MessageRestClient(getApplicationContext());

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

                                finish();
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

            Intent myIntent = new Intent(MessageFriendsActivity.this, AddFriendActivity.class);
            MessageFriendsActivity.this.startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
