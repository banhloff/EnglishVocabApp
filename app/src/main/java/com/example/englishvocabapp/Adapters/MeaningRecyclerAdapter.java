package com.example.englishvocabapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishvocabapp.Entities.Dictionary.Meanings;
import com.example.englishvocabapp.Entities.Note;
import com.example.englishvocabapp.R;

import java.util.ArrayList;
import java.util.List;

public class MeaningRecyclerAdapter  extends RecyclerView.Adapter<MeaningRecyclerAdapter.MeaningViewHolder> {

    private List<Meanings> data;
    private LayoutInflater inflater;
    private MeaningRecyclerAdapter.MeaningItemClickListener clickListener;
    private Context context;

    // data is passed into the constructor
    public MeaningRecyclerAdapter(Context context, List<Meanings> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public MeaningRecyclerAdapter.MeaningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_item_meaning, parent, false);
        return new MeaningRecyclerAdapter.MeaningViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MeaningRecyclerAdapter.MeaningViewHolder holder, int position) {
        Meanings meanings = data.get(position);
        holder.txtPartOfSpeech.setText("Parts of Speech: " + meanings.getPartOfSpeech());
        holder.recyclerDefinitions.setHasFixedSize(true);
        holder.recyclerDefinitions.setLayoutManager(new GridLayoutManager(context, 1));
        DefinitionRecyclerAdapter definitionAdapter =
                new DefinitionRecyclerAdapter(context, meanings.getDefinitions());
        holder.recyclerDefinitions.setAdapter(definitionAdapter);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class MeaningViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtPartOfSpeech;
        RecyclerView recyclerDefinitions;
        MeaningViewHolder(View itemView) {
            super(itemView);
            txtPartOfSpeech = itemView.findViewById(R.id.txt_partOfSpeech);
            recyclerDefinitions = itemView.findViewById(R.id.recycler_definitions);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Meanings getItem(int id) {
        return data.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(MeaningRecyclerAdapter.MeaningItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    //update all data
    public void updateAll(ArrayList<Meanings> list) {
        data = list;
    }

    // parent activity will implement this method to respond to click events
    public interface MeaningItemClickListener {
        void onItemClick(View view, int position);
    }
}
