package com.informatika.myexpensetrackeroka.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.informatika.myexpensetrackeroka.R;
import com.informatika.myexpensetrackeroka.adapter.ExpenseAdapter;
import com.informatika.myexpensetrackeroka.helper.StorageHelper;
import com.informatika.myexpensetrackeroka.model.ExpenseModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.OnItemClickListener {

    private ExpenseAdapter expenseAdapter;
    private ArrayList<ExpenseModel> expenseList;
    private StorageHelper storageHelper;
    private TextView tvTotalExpense;
    private ActivityResultLauncher<Intent> addExpenseLauncher;
    private ActivityResultLauncher<Intent> editExpenseLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageHelper = new StorageHelper(this);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        ExtendedFloatingActionButton fabAddExpense = findViewById(R.id.fabAddExpense);

        loadData();
        setupRecyclerView();
        setupLaunchers();

        fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            addExpenseLauncher.launch(intent);
        });

        updateTotal();
    }

    private void setupRecyclerView() {
        RecyclerView rvExpenses = findViewById(R.id.rvExpenses);
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(expenseList, this);
        rvExpenses.setAdapter(expenseAdapter);
    }

    private void setupLaunchers() {
        addExpenseLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ExpenseModel newExpense = (ExpenseModel) result.getData().getSerializableExtra("new_expense");
                        if (newExpense != null) {
                            expenseList.add(newExpense);
                            saveAndRefresh();
                        }
                    }
                });

        editExpenseLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ExpenseModel updated = (ExpenseModel) result.getData().getSerializableExtra("updated_expense");
                        int position = result.getData().getIntExtra("expense_position", -1);
                        if (updated != null && position != -1) {
                            expenseList.set(position, updated);
                            saveAndRefresh();
                        }
                    }
                });
    }

    private void loadData() {
        expenseList = storageHelper.loadExpenses();
    }

    private void saveAndRefresh() {
        storageHelper.saveExpenses(expenseList);
        expenseAdapter.notifyDataSetChanged();
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (ExpenseModel expense : expenseList) {
            total += expense.getAmount();
        }
        Locale localeID = new Locale("in", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeID);
        tvTotalExpense.setText(currencyFormatter.format(total));
    }

    @Override
    public void onEditClick(int position) {
        Intent intent = new Intent(this, EditExpenseActivity.class);
        intent.putExtra("expense_data", expenseList.get(position));
        intent.putExtra("expense_position", position);
        editExpenseLauncher.launch(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.hapus_item)
                .setMessage(R.string.yakin_hapus)
                .setPositiveButton(R.string.ya, (dialog, which) -> {
                    expenseList.remove(position);
                    saveAndRefresh();
                    Toast.makeText(this, "Item dihapus", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.tidak, null)
                .show();
    }
}