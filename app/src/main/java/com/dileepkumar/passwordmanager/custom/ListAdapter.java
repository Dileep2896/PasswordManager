package com.dileepkumar.passwordmanager.custom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dileepkumar.passwordmanager.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {

    ArrayList<String> userData;
    Context context;

    public ListAdapter(Context context, ArrayList<String> userData) {
        super(context, R.layout.custom_list_view, userData);

        this.context = context;
        this.userData = userData;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String email = userData.get(position);
        Log.i("Emails", email);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_view,
                    parent, false);
        }

        TextView tvListEmail = convertView.findViewById(R.id.tvListEmail);
        tvListEmail.setText(email);

        return convertView;
    }

}
