package com.example.dangnhapdangki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DonHangAdapter extends ArrayAdapter<DonHang> {
    private final Context context;
    private final ArrayList<DonHang> data;
    private final int resource;

    public DonHangAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DonHang> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, parent, false);

        return convertView;
    }
}

