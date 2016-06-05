package com.example.zakiva.euro;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zakiva on 5/26/16.
 */
public class ScorerAdapter extends ArrayAdapter<String[]> {

    int size;
    public Context mContext;
    LayoutInflater mInflater;
    public String userName;
    long start;


    public ScorerAdapter(Context context, ArrayList<String[]> items, Activity activity, long start) {
        super(context, R.layout.team, items);
        size = items.size();
        this.mContext = context;
        mInflater = activity.getLayoutInflater();
        userName = ((Euro) mContext.getApplicationContext()).getGlobalUsername();
        this.start = start;
    }


    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {

        if (view == null) {
            view = mInflater.inflate(R.layout.team, viewGroup, false);
        }

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeWinningTeamBar);
        final Button name = (Button) view.findViewById(R.id.buttonTeamName);
        final String[] item = getItem(i);
        name.setText(" " + item[0]  + ". " + item[1]);
        if (i % 2 == 0){
            relativeLayout.setBackgroundColor(Color.parseColor("#fbf5f6bb"));
            name.setBackgroundColor(Color.parseColor("#fbf5f6bb"));
        }
        else {
            relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            name.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }

        if (start == 0) {

            name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    writeBet(new Team(0, item[0], Integer.parseInt(item[1])));
                }
            });
        }

        return view;
    }

    public void writeBet (Team player) {
        System.out.println("writing bet...");

        final Firebase firebase = new Firebase(mContext.getApplicationContext().getString(R.string.firebase));
        firebase.child("Users").child(userName).child("scorer").setValue(player);
    }
}
