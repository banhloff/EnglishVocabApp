package com.example.englishvocabapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.englishvocabapp.Entities.Note;
import com.example.englishvocabapp.R;
import com.example.englishvocabapp.Utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder> implements Filterable {

    private List<Note> data;
    private List<Note> filteredData;
    private LayoutInflater inflater;
    private NoteItemClickListener clickListener;

    // data is passed into the constructor
    public NoteRecyclerAdapter(Context context, List<Note> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.filteredData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_item_note, parent, false);
        return new NoteViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = filteredData.get(position);
        StringBuilder str = new StringBuilder(note.getTitle());
        if(str.length() >= Constants.NOTE_TITLE_LENGTH) {
            str.delete(Constants.NOTE_TITLE_LENGTH, str.length());
            str.append("...");
        }
        holder.txtTitle.setText(str);
        DateFormat dateFormat = new SimpleDateFormat(Constants.FORMAT_DATE);
        String strDate = dateFormat.format(note.getCreatedDate());
        holder.txtDate.setText(strDate);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredData = data;
                } else {
                    List<Note> filteredList = new ArrayList<>();
                    for (Note row : data) {

                        //filter by title
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredData = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredData = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    // stores and recycles views as they are scrolled off screen
    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle;
        TextView txtDate;

        NoteViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDate = itemView.findViewById(R.id.txt_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Note getItem(int id) {
        return filteredData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(NoteItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    //update all data
    public void updateAll(ArrayList<Note> list) {
        data = list;
    }

    // parent activity will implement this method to respond to click events
    public interface NoteItemClickListener {
        void onItemClick(View view, int position);
    }
}
