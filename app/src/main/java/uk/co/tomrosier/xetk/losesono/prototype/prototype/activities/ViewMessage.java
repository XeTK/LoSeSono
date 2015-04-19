package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Comparator;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.ActionEffect;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.ArrayAdapters.CommentArrayAdapter;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.CommentRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.UserRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.VoteRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.VoteType;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Comment;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Vote;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.Login;

/**
 * This is the activity for viewing the messages and showing where they are on the map. It also has commenting and vote potential.
 */
public class ViewMessage extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    // Keep a list of the comments currently viewable on the page.
    private ArrayList<Comment> listItems = new ArrayList<Comment>();

    // This is the view adapter that holds all of the comments.
    private CommentArrayAdapter adapter;

    // We need this for detecting if the user has swiped down on the list view to refresh.
    private SwipeRefreshLayout swipeLayout;

    // Hold the message ready for it to be used when we need it.
    private Message message;

    // Keep the menu ready to access as we might modify it.
    private Menu menu;

    // This disabled the ability to edit the the post.
    private boolean isEditable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);

        // Get the extras that have been passed to the activity.
        Bundle extras = getIntent().getExtras();

        // Grab the message_id ready for us to use to retrive the message that we need.
        final int msgID = extras.getInt("MsgObj");


        // Get the notification manager.
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Cancel the notification for the message if there is a notification for it.
        manager.cancel(msgID);



        // Get the message restclient ready to sent our requests.
        final MessageRestClient mRC = new MessageRestClient(this);

        // Get a message by the message_id.
        mRC.getMessageByID(
            msgID,
            new AjaxCompleteHandler() {
                @Override
                public void handleAction(Object someData) {

                    // Grab the object that was returned from the JSON request.
                    Message msg = (Message) someData;
                    // Grab the user that is currently logged in.
                    User user   = Login.user;

                    // Check that the objects we are working with are not null.
                    if (msg != null && user != null) {

                        message = msg;

                        // Populate the list of comments on the page.
                        populateComments();

                        // Set it up for viewing someone elses message.
                        if (msg.getUserID() != user.getUserID()) {
                            // Disable editing.
                            isEditable = false;
                            // Disable the menu.
                            menu.findItem(R.id.audience).setVisible(isEditable);
                            // Invalidate what we have.
                            invalidateOptionsMenu();

                            // Get the user rest client ready for us to get data about the users.
                            UserRestClient uRC = new UserRestClient(getApplicationContext());

                            // Get the user info for the given user that has posted the message.
                            uRC.getUserByID(
                                msg.getUserID(),
                                new AjaxCompleteHandler() {
                                    @Override
                                    public void handleAction(Object someData) {
                                        User user = (User)someData;
                                        // Set the title to include the users full name at the top.
                                        setTitle(user.getFirstName() + " " + user.getLastName() + " has tagged");

                                    }
                                }
                            );

                            // This marks the message as read.
                            mRC.addMessageRead(
                                    msgID,
                                    new AjaxCompleteHandler() {
                                        @Override
                                        public void handleAction(Object someData) {}
                                    }
                            );
                        } else {
                            // Set the title for the activity.
                            setTitle("I have tagged");
                        }

                        // Setup the map ready.
                        setUpMapIfNeeded(msg);
                    }
                }
            }
        );

        // This is the button that posts the comment to the server.
        Button postComment = (Button) findViewById(R.id.VMBtnComment);

        // This is the on handler for that comment button.
        postComment.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Get the element holding the comment text.
                    final EditText commentText = (EditText)findViewById(R.id.VMCommentText);

                    // Check that it isnt null.. as that sometimes happens.
                    if (commentText != null) {

                        // Get the string from the element.
                        String strCommentText = commentText.getText().toString();

                        // If the user actually has entered something then we continue.
                        if (strCommentText != null && strCommentText.length() > 0) {

                            // Set the comment list to be refreshing as a dialog to show something is happening.
                            swipeLayout.setRefreshing(true);

                            // Generate a new comment from the information we have.
                            Comment newComment = new Comment(msgID, Login.user, strCommentText);

                            // Get the rest client ready for sending our request to the server.
                            CommentRestClient crc = new CommentRestClient(getApplicationContext());

                            // Send the new comment to the server. And let the server add it to the right place.
                            crc.addComment(
                                    newComment,
                                    new AjaxCompleteHandler() {
                                        @Override
                                        public void handleAction(Object someData) {
                                            // Get the status of the request that we are working with.
                                            Boolean status = (Boolean) someData;

                                            //if (status == true) {

                                            // Get the listview that holds all the comments we are working with.
                                            ListView lw = (ListView) findViewById(R.id.VMCommentList);

                                            // Invalidate the contents of the listview as the data has changed.
                                            lw.invalidateViews();

                                            // Say the comment has been posted.
                                            Toast.makeText(getApplicationContext(), "Comment Posted", Toast.LENGTH_LONG).show();

                                            // Repopulate the listview with the new comments.
                                            populateComments();

                                            // Clear the box for adding a new comment.
                                            commentText.setText("");
                                            //}
                                        }
                                    }
                            );

                        }
                    }


                }
            }
        );

        // Get the voting buttons for the message and make them easily accessible to change the vote levels on the message.
        ImageButton upVote   = (ImageButton) findViewById(R.id.btnVMUpVote);
        ImageButton downVote = (ImageButton) findViewById(R.id.btnVMDownVote);

        // Register the click events on pressing the upvote and downvote buttons.
        upVote.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // This upvotes the message.
                    vote(ActionEffect.positive);
                }
            }
        );

        // Downvote the message.
        downVote.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vote(ActionEffect.negative);
                }
            }
        );
    }

    // This is the method for dealing with voting.
    private void vote(ActionEffect ae) {

        // Get the rest client ready for dealing with the request.
        VoteRestClient vrc = new VoteRestClient(getApplicationContext());

        // Call the server and add the vote to the message.
        vrc.addVote(
            message.getMessageID(),
            VoteType.message,
            ae,
            new AjaxCompleteHandler() {
                @Override
                public void handleAction(Object someData) {
                    // Notify the user there vote has actually been added to the message.
                    Toast.makeText(getApplicationContext(), "Vote Added", Toast.LENGTH_LONG).show();

                    // Refresh the votes.
                    refreshVotes();
                }
            }
        );
    }

    // This populates the comment list on the page for the message.
    private void populateComments() {

        // Tell the UI we are refreshing the box.
        swipeLayout.setRefreshing(true);

        // Create a fresh list to hold the comments in.
        listItems = new ArrayList<Comment>();

        // Get the list view ready to repopilate.
        ListView lw = (ListView) findViewById(R.id.VMCommentList);

        // Reset the display adapter ready with the new list of items.
        adapter = new CommentArrayAdapter(this, listItems);

        // Re assign the list view to the new adapter.
        lw.setAdapter(adapter);

        // Get the comment rest client ready to get the list of comments from the server.
        CommentRestClient crc = new CommentRestClient(getApplicationContext());

        // Get the list of comments by the message id.
        crc.getCommentsByID(
                message.getMessageID(),
                new AjaxCompleteHandler() {
                    @Override
                    public void handleAction(Object someData) {

                        // Get the comment that we are working with.
                        final Comment comment = (Comment) someData;

                        // If the comment is null then its no good... or there is no comments for this message.
                        if (comment == null) {
                            // Disable the refreshing prompt.
                            swipeLayout.setRefreshing(false);

                            // TODO: make this less sketchy.
                            // Exit!
                            return;
                        }

                        System.out.println("Comment_id: " + comment.getCommentID());

                        // This is the rest client for getting information on the user.
                        UserRestClient uRC = new UserRestClient(getApplicationContext());

                        // Get the user by id.
                        uRC.getUserByID(
                                comment.getUserID(),
                                new AjaxCompleteHandler() {
                                    @Override
                                    public void handleAction(Object someData) {
                                        // Get a Java user object that we can then use within the Android app.
                                        User user = (User) someData;

                                        // Get the comment ready to manipulate.
                                        Comment userComment = comment;

                                        // Set the user to the the one we just got.
                                        userComment.setUser(user);

                                        // Add the comment to the list of comments we are displaying.
                                        listItems.add(userComment);

                                        // Sort the listview adapter in time.
                                        adapter.sort(
                                                new Comparator<Comment>() {
                                                    @Override
                                                    public int compare(Comment arg1, Comment arg0) {

                                                        // Minus one time from the other to get the differnece.
                                                        int diff = (int) (arg1.getCreatedDate().getTime() - arg0.getCreatedDate().getTime());

                                                        return diff;
                                                    }
                                                }
                                        );

                                        // Notify the adapter the items have changed so redraw.
                                        adapter.notifyDataSetChanged();
                                        // We are done so we are not refreshing any more.
                                        swipeLayout.setRefreshing(false);
                                    }
                                }
                        );
                    }
                }
        );
        refreshVotes();
    }

    // This refreshes the bar below the map that holds the vote information for the users.
    private void refreshVotes(){

        // Get the vote rest client ready to get the information that we need.
        VoteRestClient vrc = new VoteRestClient(getApplicationContext());

        // This gets the votes by the id of the message.
        vrc.getVoteByID(
                message.getMessageID(),
                VoteType.message,
                new AjaxCompleteHandler() {
                    @Override
                    public void handleAction(Object someData) {

                        // Get the vote object from the server.
                        Vote vote = (Vote) someData;

                        // Get the ui elements we want to change the value for, theses are labels holding the count of the votes.
                        TextView upVote   = (TextView) findViewById(R.id.VMUpVote);
                        TextView downVote = (TextView) findViewById(R.id.VMDownVote);

                        // Convert the int values we have for the votes into strings that we can append to the labels. Other wise we get exceptions.
                        String lblPos = String.valueOf(vote.getPositive());
                        String lblNeg = String.valueOf(vote.getNegative());

                        // Set the label text.
                        upVote.setText(lblPos);
                        downVote.setText(lblNeg);

                    }
                }
        );
    }

    // Setup the map with the things we need.
    private void setUpMapIfNeeded(final Message msg) {

        // Try to obtain the map from the SupportMapFragment.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.vMsgMapFragment);

        // Setup the map when the map has loaded after doing things asynchronously.
        mapFragment.getMapAsync(
                new OnMapReadyCallback() {

                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        setUpMap(googleMap, msg);
                    }
                }
        );
    }

    // Setup the map with the details about the message that has been left.
    private void setUpMap(GoogleMap mMap, Message msg) {

        // Generate the long and lat for the location of the tag.
        LatLng location = new LatLng(msg.getLatitude(), msg.getLongitude());

        // Configure the options that are needed for the tag, giving the location along with the content.
        MarkerOptions markerOpts = new MarkerOptions();
        markerOpts.position(location);
        markerOpts.title(msg.getContent());
        markerOpts.visible(true);
        // Set the colour of the tag.
        markerOpts.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        // Add the marker to the map.
        Marker marker = mMap.addMarker(markerOpts);
        marker.showInfoWindow();

        // Move the camera to the desired location.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));

        // Disable stuff we don't need for viewing the marker on the map.
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
    }

    @Override
    public void onBackPressed() {
        // When exiting the activity.
        finish();

        // Create a intent to go back to the home screen of the application.
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Make the menu easy to access.
        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_view_message, menu);
        menu.findItem(R.id.audience).setVisible(isEditable);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // This is the button to edit the audience for a given message.
        if (id == R.id.audience) {

            // Create a intent to edit the friends tied to a message.
            Intent myIntent = new Intent(this, MessageFriendsActivity.class);

            // Pass the message_id to the activity that deals with viewing the message.
            myIntent.putExtra("Message_ID", message.getMessageID());

            // Start the edit audience activity.
            startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // This is what is called when the pull down on the list view is triggered.
    @Override
    public void onRefresh() {
        populateComments();
    }
}
