package com.example.placesaccounter.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placesaccounter.R;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private Context context;
    private List<ModelRoom> itemArray;
    private OnClickListener onClickListener;

    public MainAdapter(Context context, OnClickListener onClickListener) {
        this.context = context;
        itemArray = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(itemArray.get(position));

        // Set listener on item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(itemArray.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemArray.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_floor_number, tv_room_number, tv_room_capacity;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_floor_number = itemView.findViewById(R.id.floor_number_item);
            tv_room_number = itemView.findViewById(R.id.room_number_item);
            tv_room_capacity = itemView.findViewById(R.id.room_capacity_item);
        }
        // Get data for item in RecyclerView list
        public void setData(ModelRoom modelRoom) {
            tv_floor_number.setText(new StringBuilder().append("Этаж: ")
                    .append(String.valueOf(modelRoom.getFloor_number())).toString());

            tv_room_number.setText(new StringBuilder().append("Комната: ")
                    .append(String.valueOf(modelRoom.getRoom_number())).toString());

            tv_room_capacity.setText(new StringBuilder().append(String.valueOf(modelRoom.getLearners_in_room().size()))
                    .append("/").append(String.valueOf(modelRoom.getBeds_number())).toString());
        }
    }
    public void updateAdapter(List<ModelRoom> newList) {
        itemArray.clear();
        itemArray.addAll(newList);

        notifyDataSetChanged();
    }
}
