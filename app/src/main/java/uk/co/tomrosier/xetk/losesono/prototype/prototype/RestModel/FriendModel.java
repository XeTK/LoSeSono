package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestModel;

import android.app.Activity;
import android.widget.Toast;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;

/**
 * Created by xetk on 23/03/15.
 */
public class FriendModel {

    public static void addNewFriend(User user, Activity activity) {
        Toast.makeText(activity.getApplicationContext(), user.getUserNmae() + " Has been added.", Toast.LENGTH_LONG).show();
        activity.finish();
    }
}
