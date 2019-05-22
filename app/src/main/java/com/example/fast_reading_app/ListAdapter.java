package com.example.fast_reading_app;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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

    private ItemFilter filter;
    private SparseBooleanArray itemStateArray;
    private Boolean isCheckBoxVisible;

    public ListAdapter(Context context, int resource, List<ListEntry> files) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.files = files;
        this.filteredFiles = files;

        this.filter = new ItemFilter();
        this.itemStateArray = new SparseBooleanArray();
        this.isCheckBoxVisible = false;
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

    public void setCheckBoxVisible(Boolean checkBoxVisible) {
        isCheckBoxVisible = checkBoxVisible;
    }

    public Filter getFilter() {
        return filter;
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

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            int count = files.size();
            final ArrayList<ListEntry> newList = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                if (files.get(i).getName().toLowerCase().contains(filterString)) {
                    newList.add(files.get(i));
                }
            }

            results.values = newList;
            results.count = newList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                filteredFiles = (ArrayList<ListEntry>) results.values;
                notifyDataSetChanged();
            }
        }

    }
}
