package com.example.fast_reading_app;

import android.content.Context;

import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter implements Filterable {

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<ListEntry> files;
    private List<ListEntry> filteredFiles;
    private ItemFilter mFilter;
    private SparseBooleanArray itemStateArray;
    private Boolean isCheckBoxVisible;

    public ListAdapter(Context context, int resource, List<ListEntry> files) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.files = files;
        this.filteredFiles = files;
        this.mFilter = new ItemFilter();
        this.itemStateArray = new SparseBooleanArray();
        this.isCheckBoxVisible = false;
    }

    public void setCheckBoxVisible(Boolean checkBoxVisible) {
        isCheckBoxVisible = checkBoxVisible;
    }

    @Override
    public int getCount() {
        return filteredFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredFiles.get(position);
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

        ListEntry currentFile = filteredFiles.get(position);
        viewHolder.name.setText(currentFile.getName());

        viewHolder.check.setTag(position);
        viewHolder.check.setOnClickListener(onStateChangedListener(position));


        if (!isCheckBoxVisible) {
            viewHolder.check.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.check.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private View.OnClickListener onStateChangedListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemStateArray.get(position, false)) {
                    itemStateArray.put(position, true);
                } else  {
                    itemStateArray.put(position, false);
                }

                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        final TextView name;
        final CheckBox check;

        ViewHolder(View v) {
            this.name = v.findViewById(R.id.name);
            this.check = v.findViewById(R.id.check);
        }

    }

    public SparseBooleanArray getItemStateArray() {
        return  itemStateArray;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            int count = files.size();
            final ArrayList<ListEntry> nlist = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                if (files.get(i).getName().toLowerCase().contains(filterString)) {
                    nlist.add(files.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            System.err.println(results.values.toString());

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                filteredFiles = (ArrayList<ListEntry>) results.values;
                notifyDataSetChanged();
            }
        }

    }

}
