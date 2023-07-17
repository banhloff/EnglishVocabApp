package com.example.englishvocabapp.Adapters;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.englishvocabapp.Entities.Dictionary.Phonetics;
import com.example.englishvocabapp.Entities.Note;
import com.example.englishvocabapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhoneticRecyclerAdapter extends RecyclerView.Adapter<PhoneticRecyclerAdapter.PhoneticViewHolder> {

    private List<Phonetics> data;
    private LayoutInflater inflater;
    private PhoneticRecyclerAdapter.PhoneticItemClickListener clickListener;
    private Context context;

    // data is passed into the constructor
    public PhoneticRecyclerAdapter(Context context, List<Phonetics> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public PhoneticRecyclerAdapter.PhoneticViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_item_phonetic, parent, false);
        return new PhoneticRecyclerAdapter.PhoneticViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(PhoneticRecyclerAdapter.PhoneticViewHolder holder, int position) {
        Phonetics phonetics = data.get(position);
        holder.txtPhonetic.setText(phonetics.getText());
        holder.btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer player = new MediaPlayer();
                player.setAudioAttributes(
                        new AudioAttributes
                                .Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build());
                try {
//                    player.setDataSource("https:" + phonetics.getAudio());
                    player.setDataSource(phonetics.getAudio());
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Couldn't play Audio!", Toast.LENGTH_SHORT).show();
                }
                player.start();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class PhoneticViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtPhonetic;
        ImageButton btnAudio;

        PhoneticViewHolder(View itemView) {
            super(itemView);
            txtPhonetic = itemView.findViewById(R.id.txt_phonetic);
            btnAudio = itemView.findViewById(R.id.btn_audio);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Phonetics getItem(int id) {
        return data.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(PhoneticRecyclerAdapter.PhoneticItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    //update all data
    public void updateAll(ArrayList<Phonetics> list) {
        data = list;
    }

    // parent activity will implement this method to respond to click events
    public interface PhoneticItemClickListener {
        void onItemClick(View view, int position);
    }
}
