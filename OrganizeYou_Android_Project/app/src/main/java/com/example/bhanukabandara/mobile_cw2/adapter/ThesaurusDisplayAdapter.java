package com.example.bhanukabandara.mobile_cw2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.bhanukabandara.mobile_cw2.R;

import java.util.ArrayList;
import java.util.List;

public class ThesaurusDisplayAdapter extends  RecyclerView.Adapter<ThesaurusDisplayAdapter.ThesaurusViewHolder>{

    private ArrayList<String> thesaurusArray;

    public class ThesaurusViewHolder extends RecyclerView.ViewHolder {
        TextView thesaurusTextView;

    public ThesaurusViewHolder(View view) {
        super(view);
        thesaurusTextView = view.findViewById(R.id.thesaurus_text_view);
    }
}

    public ThesaurusDisplayAdapter(ArrayList<String> synonymsList) {
        this.thesaurusArray = synonymsList;
    }

    @Override
    public ThesaurusDisplayAdapter.ThesaurusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.thesaurus_list_item, parent, false);
        return new ThesaurusDisplayAdapter.ThesaurusViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ThesaurusDisplayAdapter.ThesaurusViewHolder holder, int position) {
        String thesaurusWord = thesaurusArray.get(position);
        holder.thesaurusTextView.setText(thesaurusWord);

    }

    @Override
    public int getItemCount() {
        return thesaurusArray.size();
    }
}
