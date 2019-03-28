package com.example.fast_reading_app;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter {

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<ListEntry> files;

    public ListAdapter(Context context, int resource, List<ListEntry> files) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.files = files;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ListEntry currentFile = files.get(position);

        viewHolder.name.setText(currentFile.getName());

        return convertView;
    }

    private class ViewHolder {
        final TextView name;

        ViewHolder(View v) {
            this.name = v.findViewById(R.id.name);
        }
    }
}
