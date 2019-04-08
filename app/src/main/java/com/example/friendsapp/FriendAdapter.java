package com.example.friendsapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.friendsapp.BE.BEFriend;

import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<BEFriend> {
    private ArrayList<BEFriend> friends;

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

        BEFriend f = friends.get(position);

        TextView name = (TextView) v.findViewById(R.id.txtName);

        ImageView iv = (ImageView) v.findViewById(R.id.iv);

        name.setText(f.getName());
        if (f.getImgPath() != null) {
            iv.setImageURI(Uri.parse(f.getImgPath()));
        } else {
            iv.setImageResource(R.drawable.placeholder);
        }
        return v;
    }
}
