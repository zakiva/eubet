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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zakiva on 5/26/16.
 */
public class GamesListAdapter extends ArrayAdapter<String[]> {

    private Context mContext;
    public String userName;

    public GamesListAdapter(Context context, ArrayList<String[]> items) {
        super(context, R.layout.singal_game, items);
        this.mContext = context;
        userName = ((Euro) mContext.getApplicationContext()).getGlobalUsername();
    }


    @Override
    public View getView(final int i, View customView, final ViewGroup viewGroup) {

        System.out.println("get view start i =" + i);

        LayoutInflater myInflater = LayoutInflater.from(getContext());

        if (customView == null) {
            customView = myInflater.inflate(R.layout.singal_game, viewGroup, false);
        }

        customView.setId(i);

        final View currentView = customView;

        Button team1 = (Button) customView.findViewById(R.id.buttonFirstTeam);
        Button team2 = (Button) customView.findViewById(R.id.buttonSecondTeam);
        Button tie = (Button) customView.findViewById(R.id.buttonTie);
        TextView header = (TextView) customView.findViewById(R.id.gameHeader);
        TextView time = (TextView) customView.findViewById(R.id.time);
        TextView teams = (TextView) customView.findViewById(R.id.teams);



        System.out.println("settext");

        final String[] item = getItem(i);
        team1.setText(" " + item[0] + " "  + item[8]);
        team2.setText(" " +  item[1] + " " + item[9]);
        tie.setText(item[2]);
        time.setText(item[3]);
        header.setText(item[6]);
        teams.setText(item[0] + " vs " + item[1]);

        int bet = Integer.parseInt(item[4]);
        final int result = Integer.parseInt(item[11]);

        System.out.println("colorlistfirst");

        colorCells(i, viewGroup, bet, currentView, result);

        System.out.println("setlisteners");

        team1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                writeBet(i, item[5], 0);
            }
        });

        team2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                writeBet(i, item[5], 1);
            }
        });

        tie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                writeBet(i, item[5], 2);
            }
        });

        System.out.println("onClickListenersdone");


        final Firebase firebase = new Firebase(mContext.getApplicationContext().getString(R.string.firebase));

        //better to change for listener only to the username. but require to initilize username for all games bets asap

        firebase.child("games").child(item[5]).child("bets").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println("on data changed >>BETS<<");

                Map<String, Long> bets = (Map<String, Long>) snapshot.getValue();

                if (bets.get(userName) != null) { // change this too after changing this listener
                    colorCells(i, viewGroup, bets.get(userName), currentView, result);
                } else {
                    colorCells(i, viewGroup, -1, currentView, result);
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });

        System.out.println("returning customView");


        return customView;
    }


    public void writeBet(int i, String key, long bet) {

        System.out.println("writing bet...");

        final Firebase firebase = new Firebase(mContext.getApplicationContext().getString(R.string.firebase));

        //  firebase.child("games").child(key).child("bets").child(((Euro) mContext.getApplicationContext()).getGlobalUsername()).setValue(bet);
        firebase.child("games").child(key).child("bets").child(userName).setValue(bet);
    }

    public void colorCells(int i, ViewGroup listView, long button, View currentView, int result) {

        System.out.println(currentView.getId());

        System.out.println("color cells: i = " + i + " button = " + button + " result = " + result);

        if (i == currentView.getId()) {

            Button team1 = (Button) currentView.findViewById(R.id.buttonFirstTeam);
            Button team2 = (Button) currentView.findViewById(R.id.buttonSecondTeam);
            Button tie = (Button) currentView.findViewById(R.id.buttonTie);

            team1.setBackgroundColor(Color.parseColor("#d0dddc"));
            team2.setBackgroundColor(Color.parseColor("#d0dddc"));
            tie.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

            if (button == 0) {
                team1.setBackgroundColor(Color.parseColor("#C676FBB0"));
            } else if (button == 1) {
                team2.setBackgroundColor(Color.parseColor("#C676FBB0"));
            } else if (button == 2) {
                tie.setBackgroundColor(Color.parseColor("#C676FBB0"));
            }


            team1.setTextColor(Color.parseColor("#FF000000"));
            team2.setTextColor(Color.parseColor("#FF000000"));
            tie.setTextColor(Color.parseColor("#FF000000"));

            if (result == 0) {
                team1.setTextColor(Color.parseColor("#FFFF0000"));
            } else if (result == 1) {
                team2.setTextColor(Color.parseColor("#FFFF0000"));
            } else if (result == 2) {
                tie.setTextColor(Color.parseColor("#FFFF0000"));
            }

        }
    }



    //*********************************************************************************************

    public void colorList(int i, ViewGroup listView, long button) {

        System.out.println("color list: i = " + i + " button = " + button);

        View v;

        v = getViewByPosition(i, (ListView) listView);

        System.out.println("we have the view !!!!!!!!!!!!!!!!!!!!!!!!!! ");

        Button team1 = (Button) v.findViewById(R.id.buttonFirstTeam);
        Button team2 = (Button) v.findViewById(R.id.buttonSecondTeam);
        Button tie = (Button) v.findViewById(R.id.buttonTie);

        team1.setBackgroundColor(Color.parseColor("#d0dddc"));
        team2.setBackgroundColor(Color.parseColor("#d0dddc"));
        tie.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

        if (button == 0) {
            team1.setBackgroundColor(Color.parseColor("#FFFFFF02"));
        } else if (button == 1) {
            team2.setBackgroundColor(Color.parseColor("#FFFFFF02"));
        } else if (button == 2) {
            tie.setBackgroundColor(Color.parseColor("#FFFFFF02"));
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


}
