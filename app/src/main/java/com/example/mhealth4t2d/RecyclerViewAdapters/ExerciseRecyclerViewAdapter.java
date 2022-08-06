package com.example.mhealth4t2d.RecyclerViewAdapters;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mhealth4t2d.Activities.ExerciseDetailsActivity;
import com.example.mhealth4t2d.Activities.UserDetailsActivity;
import com.example.mhealth4t2d.Models.Exercise;
import com.example.mhealth4t2d.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class ExerciseRecyclerViewAdapter extends Section {

    private ArrayList<Exercise> exercises;

    private String sectionText;

    public ExerciseRecyclerViewAdapter(ArrayList<Exercise> itemList, String sectionText, int sectionLayout, int itemLayout) {

        super(SectionParameters.builder()
                .itemResourceId(itemLayout)
                .headerResourceId(sectionLayout)
                .build());

        this.exercises = itemList;

        this.sectionText = sectionText;

    }

    @Override
    public int getContentItemsTotal() {

        return exercises.size();

    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {

        return new ExerciseRecyclerViewAdapter.ItemViewHolder(view);

    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        ExerciseRecyclerViewAdapter.ItemViewHolder itemHolder = (ExerciseRecyclerViewAdapter.ItemViewHolder) holder;

        itemHolder.tvItem.setText(this.exercises.get(position).getTitle());

        if(itemHolder.tvTime != null){

            try {

                SimpleDateFormat full_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                SimpleDateFormat time_format = new SimpleDateFormat("HH:mm", Locale.getDefault());

                Date datetime = full_format.parse(this.exercises.get(position).getDatetime());

                itemHolder.tvTime.setText(time_format.format(datetime));

            } catch (ParseException e) {

                e.printStackTrace();

            }

        }

        itemHolder.btn_details.setOnClickListener(l->{

            Intent intent = new Intent(holder.itemView.getContext(), ExerciseDetailsActivity.class);
            intent.putExtra("id", exercises.get(position).getId());
            holder.itemView.getContext().startActivity(intent);

        });

    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {

        final ExerciseRecyclerViewAdapter.SectionViewHolder headerHolder = (ExerciseRecyclerViewAdapter.SectionViewHolder) holder;

        headerHolder.tvSection.setText(sectionText);

    }


    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {

        return new ExerciseRecyclerViewAdapter.SectionViewHolder(view);

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvItem, tvTime;

        private Button btn_details;

        public ItemViewHolder(View itemView) {
            super(itemView);

            tvItem = (TextView) itemView.findViewById(R.id.txtview_title);

            btn_details = itemView.findViewById(R.id.btn_details);

            tvTime = itemView.findViewById(R.id.txtview_time);

        }

    }

    class SectionViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvSection;

        public SectionViewHolder(View itemView) {
            super(itemView);

            tvSection = itemView.findViewById(R.id.txtview_section);

        }

    }


}
