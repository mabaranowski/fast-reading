package com.example.fast_reading_app;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter implements Filterable {

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<ListEntry> files;
    private List<ListEntry> filteredFiles;
    private ItemFilter mFilter;

    public ListAdapter(Context context, int resource, List<ListEntry> files) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.files = files;
        this.filteredFiles = files;
        this.mFilter = new ItemFilter();
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

        return convertView;
    }

    private class ViewHolder {
        final TextView name;

        ViewHolder(View v) {
            this.name = v.findViewById(R.id.name);
        }

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
