package com.example.mhealth4t2d.RecyclerViewAdapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhealth4t2d.Activities.AssociateExerciseActivity;
import com.example.mhealth4t2d.Activities.UserDetailsActivity;
import com.example.mhealth4t2d.Models.User;
import com.example.mhealth4t2d.R;

import java.util.ArrayList;

public class UserRecyclerViewAdpater extends RecyclerView.Adapter<UserRecyclerViewAdpater.ViewHolder> {

    private ArrayList<User> usersList;

    private RecyclerView recyclerView;

    public UserRecyclerViewAdpater(RecyclerView recyclerView, ArrayList<User> usersList) {

        this.recyclerView = recyclerView;
        this.usersList = usersList;

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(UserRecyclerViewAdpater.ViewHolder holder, int position) {

        holder.txtview_name.setText(usersList.get(position).getName());

        holder.itemView.setOnClickListener(v -> {

            if(holder.sub_items.getVisibility() == View.GONE){

                for(int i = 0; i < this.usersList.size(); i++){

                    View itemView2 = recyclerView.findViewHolderForAdapterPosition(i).itemView;

                    itemView2.findViewById(R.id.sub_items).setVisibility(View.GONE);

                }

                holder.sub_items.setVisibility(View.VISIBLE);


            }else{

                holder.sub_items.setVisibility(View.GONE);

            }

            notifyItemChanged(position);

        });

        holder.btn_details.setOnClickListener(l -> {

            Intent intent = new Intent(holder.itemView.getContext(), UserDetailsActivity.class);
            intent.putExtra("id", usersList.get(position).getId());
            holder.itemView.getContext().startActivity(intent);

        });

        holder.btn_associate_exercise.setOnClickListener(l -> {

            Intent intent = new Intent(holder.itemView.getContext(), AssociateExerciseActivity.class);
            intent.putExtra("id", usersList.get(position).getId());
            holder.itemView.getContext().startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {

        return this.usersList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtview_name;

        Button btn_associate_exercise, btn_details;

        ConstraintLayout sub_items;

        public ViewHolder(View itemView) {
            super(itemView);

            txtview_name = itemView.findViewById(R.id.txtview_name);

            btn_associate_exercise = itemView.findViewById(R.id.btn_associate_exercise);

            btn_details = itemView.findViewById(R.id.btn_details);

            sub_items = itemView.findViewById(R.id.sub_items);

        }


    }

}
