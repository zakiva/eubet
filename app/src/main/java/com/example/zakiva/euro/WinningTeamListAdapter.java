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

import java.util.ArrayList;

/**
 * Created by zakiva on 5/26/16.
 */
public class WinningTeamListAdapter extends ArrayAdapter<String[]> {

    int size;

    public WinningTeamListAdapter(Context context, ArrayList<String[]> items) {
        super(context, R.layout.team ,items);
        size = items.size();
    }


    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.team, viewGroup, false);
        RelativeLayout relativeLayout = (RelativeLayout) customView.findViewById(R.id.relativeWinningTeamBar);
        Button name = (Button) customView.findViewById(R.id.buttonTeamName);
        final String[] item = getItem(i);
        name.setText(item[0]);
        if (i % 2 == 0){
            relativeLayout.setBackgroundColor(Color.parseColor("#d0dddc"));
            name.setBackgroundColor(Color.parseColor("#d0dddc"));
        }
        else {
            relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            name.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }

        name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                colorList(i, viewGroup);
            }
        });

        return customView;
    }

    public void colorList (int i, ViewGroup listView) {
        View v;

        for (int j = 0; j < size; j++) {

            v = getViewByPosition(j, (ListView) listView);

            RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeWinningTeamBar);
            Button name = (Button) v.findViewById(R.id.buttonTeamName);

            if (i == j) {
                relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF02"));
                name.setBackgroundColor(Color.parseColor("#FFFFFF02"));
            }
            else {

                if (j % 2 == 0) {
                    relativeLayout.setBackgroundColor(Color.parseColor("#d0dddc"));
                    name.setBackgroundColor(Color.parseColor("#d0dddc"));
                } else {
                    relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    name.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                }
            }
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
