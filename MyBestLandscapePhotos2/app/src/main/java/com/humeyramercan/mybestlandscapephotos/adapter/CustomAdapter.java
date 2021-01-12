package com.humeyramercan.mybestlandscapephotos.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.humeyramercan.mybestlandscapephotos.R;
import com.humeyramercan.mybestlandscapephotos.modal.Landscape;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Landscape> {
   private ArrayList<Landscape> Landscapes;
    private Activity context;

    public CustomAdapter(Activity context, ArrayList<Landscape> Landscapes) {
        super(context, R.layout.custom_view, Landscapes);
        this.context=context;
        this.Landscapes = Landscapes;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater=context.getLayoutInflater();
        View customView=layoutInflater.inflate(R.layout.custom_view,null,true);
        TextView placeNameTextView=customView.findViewById(R.id.textView);
        placeNameTextView.setText(Landscapes.get(position).getPlaceName());

        return customView;
    }
}
