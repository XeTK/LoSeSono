package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.ArrayAdapters.CommentArrayAdapter;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.CommentRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.MessageRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.UserRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Comment;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Message;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.Login;

/**
 * This is the activity for viewing the messages and showing where they are on the map.
 */
public class ViewMessage extends ActionBarActivity {

    private ArrayList<Comment> listItems = new ArrayList<Comment>();

    private CommentArrayAdapter adapter;

    // Hold the message ready for it to be used when we need it.
    private Message message;

    // Keep the menu ready to access as we might modify it.
    private Menu menu;

    // This disabled the abiliy to edit the the post.
    private boolean isEditable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        // Get the extras that have been passed to the activity.
        Bundle extras = getIntent().getExtras();

        // Grab the message_id ready for us to use to retrive the message that we need.
        final int msgID = extras.getInt("MsgObj");


        // Get the notification manager.
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Cancel the notification for the message if there is a notification for it.
        manager.cancel(msgID);


        ListView lw = (ListView) findViewById(R.id.VMCommentList);

        adapter = new CommentArrayAdapter(this, listItems);

        lw.setAdapter(adapter);

        // Get the user rest client ready for us to get data about the users.
        final UserRestClient uRC = new UserRestClient(getApplicationContext());

        CommentRestClient crc = new CommentRestClient(getApplicationContext());

        crc.getCommentsByID(
            msgID,
            new AjaxCompleteHandler() {
                @Override
                public void handleAction(Object someData) {

                    final Comment comment = (Comment)someData;

                    System.out.println("Comment_id: " + comment.getCommentID());

                    uRC.getUserByID(
                        comment.getUserID(),
                        new AjaxCompleteHandler() {
                            @Override
                            public void handleAction(Object someData) {
                                User user = (User)someData;

                                Comment userComment = comment;

                                userComment.setUser(user);

                                listItems.add(userComment);

                                adapter.notifyDataSetChanged();
                            }
                        }
                    );
                }
            }
        );


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
                        // Set it up for viewing someone elses message.
                        if (msg.getUserID() != user.getUserID()) {
                            // Disable editing.
                            isEditable = false;
                            // Disable the menu.
                            menu.findItem(R.id.audience).setVisible(isEditable);
                            // Invalidate what we have.
                            invalidateOptionsMenu();

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




    }

    // Setup the map with the things we need.
    private void setUpMapIfNeeded(final Message msg) {

        // Save the message to make it easy to access.
        this.message = msg;

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
}
