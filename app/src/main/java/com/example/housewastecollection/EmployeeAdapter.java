package com.example.housewastecollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    public interface OnEmployeeActionListener {
        void onEdit(Employee employee);
        void onDelete(Employee employee);
    }

    private Context context;
    private List<Employee> employeeList;
    private OnEmployeeActionListener listener;

    public EmployeeAdapter(Context context, List<Employee> employeeList, OnEmployeeActionListener listener) {
        this.context = context;
        this.employeeList = employeeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.ViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.tvFullName.setText("Name: " + employee.getFullName());
        holder.tvEmail.setText("Email: " + employee.getEmail());
        holder.tvPhone.setText("Phone: " + employee.getPhone());
        holder.tvArea.setText("Assigned Area: " + employee.getAssignedArea());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(employee);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(employee);
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public void updateList(List<Employee> newList) {
        employeeList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName, tvEmail, tvPhone, tvArea;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvEmpFullName);
            tvEmail = itemView.findViewById(R.id.tvEmpEmail);
            tvPhone = itemView.findViewById(R.id.tvEmpPhone);
            tvArea = itemView.findViewById(R.id.tvEmpArea);
            btnEdit = itemView.findViewById(R.id.btnEditEmployee);
            btnDelete = itemView.findViewById(R.id.btnDeleteEmployee);
        }
    }
}
