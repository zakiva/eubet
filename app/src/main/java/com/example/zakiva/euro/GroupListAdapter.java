package com.example.zakiva.euro;

/**
 * Created by zakiva on 5/12/16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zakiva.euro.FirebaseListAdapter;
import com.example.zakiva.euro.GroupInfo;
import com.example.zakiva.euro.GroupName;
import com.example.zakiva.euro.R;
import com.firebase.client.Query;

public class GroupListAdapter extends FirebaseListAdapter<GroupName> {

    int i = 0;
    private Context context;

    // The mUsername for this client. We use this to indicate which messages originated from this user

    public GroupListAdapter(Query ref, Activity activity, int layout) {
        super(ref, GroupName.class, layout, activity);
        this.context = activity;
    }

    /**
     * Bind an instance of the <code>Post</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Post</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param groupname An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, GroupName groupname) {
        final String groupName = groupname.getName();
        Button groupNameButton = (Button) view.findViewById(R.id.groupNameList);
        groupNameButton.setText(groupName);
        if (i == 0) {
            groupNameButton.setBackgroundColor(Color.parseColor("#d0dddc"));
            i = 1;
        } else {
            groupNameButton.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            i = 0;
        }
        groupNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent gi = new Intent(context, GroupInfo.class);
                gi.putExtra("groupName", groupName);
                context.startActivity(gi);
            }
        });

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.groupElement);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        /*
        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
            authorText.setVisibility(View.GONE);
            relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            params.setMargins(40, 3, 0, 3);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            relativeLayout.setLayoutParams(params);
            //  authorText.setTextColor(Color.RED);
            //   linearLayout.setGravity(Gravity.RIGHT); // no
        } else {
            authorText.setVisibility(View.VISIBLE);
            relativeLayout.setBackgroundColor(Color.parseColor("#9a48f84b"));
            params.setMargins(0, 3, 40, 3);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            relativeLayout.setLayoutParams(params);
            // authorText.setTextColor(Color.BLUE);
        }
        ((TextView) view.findViewById(R.id.message)).setText(" " + post.getMessage() + "   ");
        */
    }
}