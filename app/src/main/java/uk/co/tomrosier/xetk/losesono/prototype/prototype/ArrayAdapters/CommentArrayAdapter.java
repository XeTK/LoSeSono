package uk.co.tomrosier.xetk.losesono.prototype.prototype.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.ActionEffect;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.VoteRestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.VoteType;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Comment;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.Vote;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;

/**
 * This is the container and handler for the listview for comments on each message.
 */
public class CommentArrayAdapter extends ArrayAdapter<Comment> {

    // Keep a list of the comment objects that are currently on the activity.
    private ArrayList<Comment> commentList = new ArrayList<Comment>();

    // When we instantiate the listview we want to get everything ready, for action.
    public CommentArrayAdapter(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
        // Create a new list of comments, when we instantiate the class.
        this.commentList = new ArrayList<Comment>();
        // And copy all of the comments into the new comment list we are working with.
        this.commentList.addAll(commentList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the comment we are working with from the list.
        final Comment comment = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        // Set the type of view we have for each element.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_item, parent, false);
        }

        // Get all of the elements on the page that we want to edit.
        TextView cName      = (TextView) convertView.findViewById(R.id.lblCommenterName);
        TextView cComment   = (TextView) convertView.findViewById(R.id.lblCommentorComment);
        TextView cTime      = (TextView) convertView.findViewById(R.id.LBLCommentTime);
        TextView cUpVotes   = (TextView) convertView.findViewById(R.id.lblUpVotes);
        TextView cDownVotes = (TextView) convertView.findViewById(R.id.lblDownVotes);

        // Grab the user from the comment so we can strip the important information out.
        User user = comment.getUser();

        // Generate the string to describe the user that posted the comment.
        String name = user.getFirstName() + " " + user.getLastName() + " (" + user.getUserNmae() + ")";

        // Get the date that the comment was originally posted.
        Date createdDate = comment.getCreatedDate();

        // Make the format that we want to display the time the comment was created.
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yy");

        // Get the string with the date in to add to the comment.
        String dateStr = dateFormat.format(createdDate);

        // Set all labels on the list view item to the values we want.
        cName.setText(name);
        cComment.setText(comment.getContent());
        cTime.setText(dateStr);

        // Grab the vote information for the comment so we can process it.
        Vote vote = comment.getVote();

        // Get the strings that we want to put next to the icons.
        String lblPos = String.valueOf(vote.getPositive());
        String lblNeg = String.valueOf(vote.getNegative());

        // Set the labels to the values we want.
        cUpVotes.setText(lblPos);
        cDownVotes.setText(lblNeg);

        // Get the buttons so we can set there action handlers.
        ImageButton upVote   = (ImageButton) convertView.findViewById(R.id.btnUpVote);
        ImageButton downVote = (ImageButton) convertView.findViewById(R.id.btnDownVote);

        // Bind the buttons to change the vote characteristics.
        upVote.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vote(ActionEffect.positive, comment);
                    }
                }
        );

        downVote.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vote(ActionEffect.negative, comment);
                    }
                }
        );

        // Return the completed view to render on screen
        return convertView;
    }


    // This is the action that is carried out when the buttons are pressed to give votes.
    private void vote(ActionEffect ae, Comment comment) {

        // Get the vote rest client ready for us to pass the information we need to the server.
        VoteRestClient vrc = new VoteRestClient(getContext());

        // TODO: fix this dirty big hack, this just shows the values in the UI but dosn't actually reflect whats going on in the server.

        // Get the current stats for the comment.
        Vote vote = comment.getVote();

        // Get the values out of the vote object.
        int pos = vote.getPositive();
        int neg = vote.getNegative();

        // Increment them depending on the effect.
        if (ae == ActionEffect.positive) {
            pos++;
        } else {
            neg++;
        }

        // Gen a new vote object with are apended values.
        Vote newVote = new Vote(pos,neg);

        // Bodge that onto the comment object like nothing ever happened seriously no one will ever know...
        comment.setVote(newVote);

        // Pass the effect of the vote back to the server.
        vrc.addVote(
                comment.getCommentID(),
                VoteType.comment,
                ae,
                new AjaxCompleteHandler() {
                    @Override
                    public void handleAction(Object someData) {
                        // Tell the user they have updated the the vote for the given object they are working on.
                        Toast.makeText(getContext(), "Vote Added", Toast.LENGTH_LONG).show();
                        // Invalidate the dataset so it draws again.
                        notifyDataSetInvalidated();
                    }
                }
        );
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
