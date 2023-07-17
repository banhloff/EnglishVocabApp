package com.example.englishvocabapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.englishvocabapp.Entities.Dictionary.Definitions;
import com.example.englishvocabapp.R;

import java.util.ArrayList;
import java.util.List;

public class DefinitionRecyclerAdapter extends RecyclerView.Adapter<DefinitionRecyclerAdapter.DefinitionViewHolder> {

    private List<Definitions> data;
    private LayoutInflater inflater;
    private DefinitionRecyclerAdapter.DefinitionItemClickListener clickListener;
    private Context context;

    // data is passed into the constructor
    public DefinitionRecyclerAdapter(Context context, List<Definitions> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public DefinitionRecyclerAdapter.DefinitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_item_definition, parent, false);
        return new DefinitionRecyclerAdapter.DefinitionViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(DefinitionRecyclerAdapter.DefinitionViewHolder holder, int position) {
        Definitions definitions = data.get(position);
        holder.txtDefinition.setText("Definition: " + definitions.getDefinition());
        holder.txtExample.setText("Example: " + definitions.getExample());
        if(definitions.getSynonyms() != null && definitions.getSynonyms().size() > 0){
            StringBuilder synonyms = new StringBuilder();
            synonyms.append(definitions.getSynonyms());
            holder.txtSynonyms.setText(synonyms);
        } else {
            holder.txtSynonyms.setText("");
        }
        if(definitions.getAntonyms() != null && definitions.getAntonyms().size() > 0){
            StringBuilder antonyms = new StringBuilder();
            antonyms.append(definitions.getAntonyms());
            holder.txtSynonyms.setText(antonyms);
        } else {
            holder.txtAntonyms.setText("");
        }

        holder.txtSynonyms.setSelected(true);
        holder.txtAntonyms.setSelected(true);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class DefinitionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtDefinition, txtExample, txtSynonyms, txtAntonyms;
        DefinitionViewHolder(View itemView) {
            super(itemView);
            txtDefinition = itemView.findViewById(R.id.txt_definition);
            txtExample = itemView.findViewById(R.id.txt_example);
            txtSynonyms = itemView.findViewById(R.id.txt_synonyms);
            txtAntonyms = itemView.findViewById(R.id.txt_antonyms);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Definitions getItem(int id) {
        return data.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(DefinitionItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    //update all data
    public void updateAll(ArrayList<Definitions> list) {
        data = list;
    }

    // parent activity will implement this method to respond to click events
    public interface DefinitionItemClickListener {
        void onItemClick(View view, int position);
    }
}
