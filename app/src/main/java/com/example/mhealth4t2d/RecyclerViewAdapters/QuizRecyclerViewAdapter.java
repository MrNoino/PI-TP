package com.example.mhealth4t2d.RecyclerViewAdapters;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.mhealth4t2d.Models.Question;
import com.example.mhealth4t2d.R;
import com.example.mhealth4t2d.Responses.GetGendersResponse;
import com.example.mhealth4t2d.Utils.Requests;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class QuizRecyclerViewAdapter extends Section {
    private ArrayList<Question> questions;

    private String sectionText;

    public QuizRecyclerViewAdapter(ArrayList<Question> itemList, String sectionText, int sectionLayout, int itemLayout) {

        super(SectionParameters.builder()
                .itemResourceId(itemLayout)
                .headerResourceId(sectionLayout)
                .build());

        this.questions = itemList;

        this.sectionText = sectionText;

    }

    @Override
    public int getContentItemsTotal() {

        return questions.size();

    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {

        return new ItemViewHolder(view);

    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        itemHolder.tvItem.setText(questions.get(position).getQuestion());

        if(itemHolder.spinner != null){

            if(questions.get(0).getCategory().equals("Resposta do Tipo de Passo")){

                initSpinner(itemHolder.spinner, R.layout.spinner_item, R.array.steps);

            }else if(questions.get(0).getCategory().equals("Resposta na Escala Borg")){

                initSpinner(itemHolder.spinner, R.layout.spinner_item, R.array.borg);

            }

        }


    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {

        final SectionViewHolder headerHolder = (SectionViewHolder) holder;

        headerHolder.tvSection.setText(sectionText);

    }


    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {

        return new SectionViewHolder(view);

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvItem;

        private final Spinner spinner;

        public ItemViewHolder(View itemView) {
            super(itemView);

            tvItem = (TextView) itemView.findViewById(R.id.txtview_question);

            spinner = itemView.findViewById(R.id.spinner);

        }

    }

    class SectionViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvSection;

        public SectionViewHolder(View itemView) {
            super(itemView);

            tvSection = itemView.findViewById(R.id.txtview_section);

        }

    }

    private void initSpinner(Spinner spinner, int spinner_layout, int array){

        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(spinner.getContext(), array, spinner_layout);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinner_adapter);

    }

}