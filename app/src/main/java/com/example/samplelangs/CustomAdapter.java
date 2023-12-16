package com.example.samplelangs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Service> {

    public CustomAdapter(Context context, List<Service> services) {
        super(context, 0, services);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Service service = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

//        TextView textView = convertView.findViewById(android.R.id.text1);
//        assert service != null;
//        textView.setText("Service: " + service.getServiceName() + ", Cost: " + service.getCost());

        return convertView;
    }
}
