package com.example.friendsapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.friendsapp.BE.BEFriend;

import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<BEFriend> {
    private ArrayList<BEFriend> friends;
    private final int[] colours = {
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#EEEEEE")
    };


    public FriendAdapter(Context context, int textViewResourceId,
                         ArrayList<BEFriend> friends) {
        super(context, textViewResourceId, friends);
        this.friends = friends;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        if (v == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.friendlistcell, null);
        }
        else
        v.setBackgroundColor(colours[position % colours.length]);


        BEFriend f = friends.get(position);

        TextView name = (TextView) v.findViewById(R.id.txtName);
        //ImageView favorite = (ImageView) v.findViewById(R.id.favorite);

        name.setText(f.getName());

        return v;
    }
}
