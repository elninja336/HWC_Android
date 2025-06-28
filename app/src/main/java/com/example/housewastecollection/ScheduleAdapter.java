package com.example.housewastecollection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private Context context;
    private ArrayList<Schedule> scheduleList;
    private DatabaseHelper dbHelper;
    private HashMap<Integer, String> customerNameMap;

    public ScheduleAdapter(Context context, ArrayList<Schedule> list, HashMap<Integer, String> customerMap) {
        this.context = context;
        this.scheduleList = (list != null) ? list : new ArrayList<>();
        this.customerNameMap = (customerMap != null) ? customerMap : new HashMap<>();
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        String customerName = customerNameMap.get(schedule.getCustomerId());

        holder.tvCustomerName.setText("Customer: " + (customerName != null ? customerName : "Unknown"));
        holder.tvDayOfWeek.setText("Day of Week: " + schedule.getDayOfWeek());
        holder.tvNextCollection.setText("Next Collection: " + schedule.getNextCollection());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddScheduleActivity.class);
            intent.putExtra("schedule_id", schedule.getId());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            int result = dbHelper.deleteSchedule(schedule.getId());
            if (result > 0) {
                scheduleList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, scheduleList.size());
                Toast.makeText(context, "Schedule deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete schedule", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvDayOfWeek, tvNextCollection;
        ImageButton btnEdit, btnDelete;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            tvNextCollection = itemView.findViewById(R.id.tvNextCollection);
            btnEdit = itemView.findViewById(R.id.btnEditSchedule);
            btnDelete = itemView.findViewById(R.id.btnDeleteSchedule);
        }
    }
}
