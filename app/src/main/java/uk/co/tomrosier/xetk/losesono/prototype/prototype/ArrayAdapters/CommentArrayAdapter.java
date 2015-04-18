package uk.co.tomrosier.xetk.losesono.prototype.prototype.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Comment;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;

/**
 * This is for the listview on the adding friends to a message page.
 */
public class CommentArrayAdapter extends ArrayAdapter<Comment> {


    private ArrayList<Comment> commentList = new ArrayList<Comment>();

    // When we instantiate the listview we want to get everything ready, for action.
    public CommentArrayAdapter(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
        this.commentList = new ArrayList<Comment>();
        this.commentList.addAll(commentList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Comment comment = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_item, parent, false);
        }

        TextView cName    = (TextView) convertView.findViewById(R.id.lblCommenterName);
        TextView cComment = (TextView) convertView.findViewById(R.id.lblCommentorComment);
        TextView cTime    = (TextView) convertView.findViewById(R.id.LBLCommentTime);

        User user = comment.getUser();

        String name = user.getFirstName() + " " + user.getLastName() + " (" + user.getUserNmae() + ")";

        Date createdDate = comment.getCreatedDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yy");


        String dateStr = dateFormat.format(createdDate);

        cName.setText(name);
        cComment.setText(comment.getContent());
        cTime.setText(dateStr);

        // Return the completed view to render on screen
        return convertView;
    }

    // This gets the id for a given item.
    @Override
    public long getItemId(int position) {
        Comment comment = getItem(position);
        return comment.getCommentID();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
