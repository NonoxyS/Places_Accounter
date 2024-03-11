package com.example.placesaccounter.listAdapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placesaccounter.R;
import com.example.placesaccounter.models.ModelRoom;

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
        private final TextView tv_floor_number, tv_room_number, tv_room_capacity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_floor_number = itemView.findViewById(R.id.floor_number_item);
            tv_room_number = itemView.findViewById(R.id.room_number_item);
            tv_room_capacity = itemView.findViewById(R.id.room_capacity_item);

        }
        // Get data for item in RecyclerView list
        public void setData(ModelRoom modelRoom) {
            tv_floor_number.setText(new StringBuilder().append("Этаж: ")
                    .append(modelRoom.getFloor_number()).toString());

            tv_room_number.setText(new StringBuilder().append("Комната: ")
                    .append(modelRoom.getRoom_number()).toString());

            tv_room_capacity.setText(new StringBuilder().append(modelRoom.getLearners_in_room().size())
                    .append("/").append(modelRoom.getBeds_number()).toString());


            ((LinearLayout)itemView.findViewById(R.id.stream_number_layout)).
                    removeViewsInLayout(1, ((LinearLayout)itemView.findViewById(R.id.stream_number_layout)).getChildCount() - 1);

            ((LinearLayout)itemView.findViewById(R.id.check_in_date_layout)).
                    removeViewsInLayout(1, ((LinearLayout)itemView.findViewById(R.id.check_in_date_layout)).getChildCount() - 1);

            ((LinearLayout)itemView.findViewById(R.id.check_out_date_layout)).
                    removeViewsInLayout(1, ((LinearLayout)itemView.findViewById(R.id.check_out_date_layout)).getChildCount() - 1);

            // Dynamic creation views of learners in room
            for (int i = 0; i < modelRoom.getLearners_in_room().size(); i++) {
                TextView tv_stream_number = new TextView(itemView.getContext());
                TextView tv_check_in_date = new TextView(itemView.getContext());
                TextView tv_check_out_date = new TextView(itemView.getContext());

                tv_stream_number.setText(String.valueOf(modelRoom.getLearners_in_room().get(i).getStream_number()));
                tv_stream_number.setGravity(Gravity.CENTER_HORIZONTAL);
                tv_stream_number.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.deactive));

                tv_check_in_date.setText(modelRoom.getLearners_in_room().get(i).getCheck_in_date());
                tv_check_in_date.setGravity(Gravity.CENTER_HORIZONTAL);
                tv_check_in_date.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.deactive));

                tv_check_out_date.setText(modelRoom.getLearners_in_room().get(i).getCheck_out_date());
                tv_check_out_date.setGravity(Gravity.CENTER_HORIZONTAL);
                tv_check_out_date.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.deactive));

                ((LinearLayout)itemView.findViewById(R.id.stream_number_layout)).addView(tv_stream_number);
                ((LinearLayout)itemView.findViewById(R.id.check_in_date_layout)).addView(tv_check_in_date);
                ((LinearLayout)itemView.findViewById(R.id.check_out_date_layout)).addView(tv_check_out_date);
            }
        }
    }
    public void updateAdapter(List<ModelRoom> newList) {
        itemArray.clear();
        itemArray.addAll(newList);

        notifyDataSetChanged();
    }
    public void updateAdapter(ModelRoom updatedModelRoom, int position) {
        itemArray.set(position, updatedModelRoom);
        notifyItemChanged(position);
    }
}
