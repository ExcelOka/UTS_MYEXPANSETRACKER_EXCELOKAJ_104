package com.informatika.myexpensetrackeroka.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView; // Import baru
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.informatika.myexpensetrackeroka.R;
import com.informatika.myexpensetrackeroka.model.ExpenseModel;
import java.util.Calendar;

public class EditExpenseActivity extends AppCompatActivity {

    private EditText etTransactionName, etAmount, etDate;
    private AutoCompleteTextView actCategory; // Ubah tipe variabel
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        etTransactionName = findViewById(R.id.etTransactionName);
        etAmount = findViewById(R.id.etAmount);
        actCategory = findViewById(R.id.actCategory); // Gunakan ID baru
        etDate = findViewById(R.id.etDate);
        Button btnSave = findViewById(R.id.btnSave);

        // Panggil method untuk setup dropdown
        setupCategoryDropdown();

        Intent intent = getIntent();
        position = intent.getIntExtra("expense_position", -1);
        ExpenseModel expense = (ExpenseModel) intent.getSerializableExtra("expense_data");

        if (expense != null) {
            etTransactionName.setText(expense.getTransactionName());
            etAmount.setText(String.valueOf(expense.getAmount()));
            // Set teks dropdown sesuai data yang ada. 'false' agar dropdown tidak langsung terbuka.
            actCategory.setText(expense.getCategory(), false);
            etDate.setText(expense.getDate());
        }

        etDate.setOnClickListener(v -> showDatePickerDialog());
        btnSave.setOnClickListener(v -> saveEditedExpense());
    }

    // Method baru untuk mengatur dropdown
    private void setupCategoryDropdown() {
        String[] categories = getResources().getStringArray(R.array.expense_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        actCategory.setAdapter(adapter);
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) -> etDate.setText(day + "-" + (month + 1) + "-" + year),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveEditedExpense() {
        String name = etTransactionName.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String category = actCategory.getText().toString().trim(); // Ambil teks dari AutoCompleteTextView
        String date = etDate.getText().toString().trim();

        if (name.isEmpty() || amountStr.isEmpty() || category.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updated_expense", new ExpenseModel(name, amount, category, date));
            resultIntent.putExtra("expense_position", position);
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Nominal harus berupa angka", Toast.LENGTH_SHORT).show();
        }
    }
}