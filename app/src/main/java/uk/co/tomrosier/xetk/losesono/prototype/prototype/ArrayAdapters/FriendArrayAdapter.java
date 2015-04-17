package uk.co.tomrosier.xetk.losesono.prototype.prototype.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Friend;

/**
 * This is for the listview on the adding friends to a message page.
 */
public class FriendArrayAdapter extends ArrayAdapter<Friend> {

    // Keep track of the friends that our shown on the page.
    private ArrayList<Friend> friendList = new ArrayList<Friend>();

    // When we instantiate the listview we want to get everything ready, for action.
    public FriendArrayAdapter(Context context, ArrayList<Friend> friends) {
        super(context, 0, friends);
        this.friendList = new ArrayList<Friend>();
        this.friendList.addAll(friendList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Friend user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_list_item, parent, false);
        }

        // Lookup view for data population
        TextView friendName = (TextView) convertView.findViewById(R.id.friendName);
        CheckBox friendCB   = (CheckBox) convertView.findViewById(R.id.friendCB);

        // String that is displayed.
        String name = "(" + user.getUser().getUserNmae() + ") " + user.getUser().getFirstName() + " " + user.getUser().getLastName();


        // Populate the data into the template view using the data object
        friendName.setText(name);
        friendCB.setChecked(false);

        // Register the onClick. To check the box.
        friendCB.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.setChecked(!user.isChecked());
                }
            }
        );

        // Return the completed view to render on screen
        return convertView;
    }

    // This gets the id for a given item.
    @Override
    public long getItemId(int position) {
        Friend user = getItem(position);
        return user.getUser().getUserID();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
