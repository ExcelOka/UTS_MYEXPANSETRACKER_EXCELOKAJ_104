package com.informatika.myexpensetrackeroka.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.informatika.myexpensetrackeroka.R;
import com.informatika.myexpensetrackeroka.model.ExpenseModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final ArrayList<ExpenseModel> expenseList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public ExpenseAdapter(ArrayList<ExpenseModel> expenseList, OnItemClickListener listener) {
        this.expenseList = expenseList;
        this.listener = listener;
    }

    @NonNull @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseModel currentItem = expenseList.get(position);
        holder.tvTransactionName.setText(currentItem.getTransactionName());
        holder.tvCategory.setText("Kategori: " + currentItem.getCategory());
        holder.tvDate.setText(currentItem.getDate());

        Locale localeID = new Locale("in", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeID);
        holder.tvAmount.setText(currencyFormatter.format(currentItem.getAmount()));
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTransactionName, tvAmount, tvCategory, tvDate;
        public ImageButton btnEdit, btnDelete;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransactionName = itemView.findViewById(R.id.tvTransactionName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(v -> listener.onEditClick(getAdapterPosition()));
            btnDelete.setOnClickListener(v -> listener.onDeleteClick(getAdapterPosition()));
        }
    }
}
