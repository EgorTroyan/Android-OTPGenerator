package com.egortroyan.otpgenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Pass> {

    public ListAdapter(Context context, ArrayList<Pass> passList){
        super(context, R.layout.list_item, passList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Pass pass = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            TextView id = convertView.findViewById(R.id.set_id);

            id.setText(pass.getKeyId());
        }
        return convertView;
    }
}
