package com.example.housewastecollection;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

public class ExtraPickupAdapter extends RecyclerView.Adapter<ExtraPickupAdapter.ExtraPickupViewHolder> {

    private Context context;
    private ArrayList<ExtraPickup> extraPickupList;
    private DatabaseHelper dbHelper;
    private HashMap<Integer, String> customerNameMap;

    public ExtraPickupAdapter(Context context, ArrayList<ExtraPickup> list, HashMap<Integer, String> customerMap) {
        this.context = context;
        this.extraPickupList = (list != null) ? list : new ArrayList<>(); // ✅ fixed here
        this.customerNameMap = (customerMap != null) ? customerMap : new HashMap<>();
        this.dbHelper = new DatabaseHelper(context);
    }



    @NonNull
    @Override
    public ExtraPickupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_extra_pickup, parent, false);
        return new ExtraPickupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraPickupViewHolder holder, int position) {
        ExtraPickup pickup = extraPickupList.get(position);
        String customerName = customerNameMap.get(pickup.getCustomerId());

        holder.tvCustomerName.setText("Customer: " + customerName);
        holder.tvRequestDate.setText("Date: " + pickup.getRequestDate());
        holder.tvStatus.setText("Status: " + pickup.getStatus());
        holder.tvCharge.setText("Charge: TZS " + pickup.getExtraCharge());
        holder.tvDescription.setText("Description: " + pickup.getDescription());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddExtraPickupActivity.class);
            intent.putExtra("extra_pickup_id", pickup.getId());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int result = db.delete(DatabaseHelper.TABLE_EXTRA_PICKUP, "id=?", new String[]{String.valueOf(pickup.getId())});
            db.close();

            if (result != -1) {
                extraPickupList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, extraPickupList.size());
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return extraPickupList.size();
    }

    public void updateData(ArrayList<ExtraPickup> newList, HashMap<Integer, String> newCustomerMap) {
        this.extraPickupList = newList;
        this.customerNameMap = newCustomerMap;
        notifyDataSetChanged();
    }


    public static class ExtraPickupViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvRequestDate, tvStatus, tvCharge, tvDescription;
        ImageButton btnEdit, btnDelete;

        public ExtraPickupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvRequestDate = itemView.findViewById(R.id.tvRequestDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCharge = itemView.findViewById(R.id.tvCharge);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
