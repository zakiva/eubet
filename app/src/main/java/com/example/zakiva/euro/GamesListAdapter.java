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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zakiva on 5/26/16.
 */
public class GamesListAdapter extends ArrayAdapter<String[]> {

   // private List<Game> myGames;


    public GamesListAdapter(Context context, ArrayList<String[]> items) {
        super(context, R.layout.singal_game, items);
    }


    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.singal_game, viewGroup, false);

        RelativeLayout relativeLayout = (RelativeLayout) customView.findViewById(R.id.relativeSingleGame);
        Button team1 = (Button) customView.findViewById(R.id.buttonFirstTeam);
        Button team2 = (Button) customView.findViewById(R.id.buttonSecondTeam);
        Button tie = (Button) customView.findViewById(R.id.buttonTie);
        TextView header = (TextView) customView.findViewById(R.id.gameHeader);

        final String[] item = getItem(i);
        team1.setText(item[0]);
        team2.setText(item[1]);
        tie.setText(item[2]);
        header.setText(item[3]);

       int bet = Integer.parseInt(item[4]);

        if (bet >= 0) {
            colorList(i, viewGroup, bet);
        }


        team1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                colorList(i, viewGroup, 0);
            }
        });

        team2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                colorList(i, viewGroup, 1);
            }
        });

        tie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                colorList(i, viewGroup, 2);
            }
        });

        return customView;
    }

    public void colorList(int i, ViewGroup listView, int button) {
        View v;


        v = getViewByPosition(i, (ListView) listView);

        Button team1 = (Button) v.findViewById(R.id.buttonFirstTeam);
        Button team2 = (Button) v.findViewById(R.id.buttonSecondTeam);
        Button tie = (Button) v.findViewById(R.id.buttonTie);

        team1.setBackgroundColor(Color.parseColor("#d0dddc"));
        team2.setBackgroundColor(Color.parseColor("#d0dddc"));
        tie.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

        if (button == 0){
            team1.setBackgroundColor(Color.parseColor("#FFFFFF02"));
        }
        else if (button == 1) {
            team2.setBackgroundColor(Color.parseColor("#FFFFFF02"));
        }
        else {
            tie.setBackgroundColor(Color.parseColor("#FFFFFF02"));
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


}
