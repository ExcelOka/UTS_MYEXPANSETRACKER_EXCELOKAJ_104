package com.informatika.myexpensetrackeroka.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.informatika.myexpensetrackeroka.model.ExpenseModel;

import java.util.ArrayList;

public class StorageHelper {
    private static final String PREF_NAME = "MyExpenseTrackerPrefs";
    private static final String EXPENSES_KEY = "expenses_string";
    private static final String FIELD_DELIMITER = ";;;";
    private static final String RECORD_DELIMITER = "|||";

    private final SharedPreferences sharedPreferences;

    public StorageHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveExpenses(ArrayList<ExpenseModel> expenses) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expenses.size(); i++) {
            ExpenseModel expense = expenses.get(i);
            sb.append(expense.getTransactionName()).append(FIELD_DELIMITER);
            sb.append(expense.getAmount()).append(FIELD_DELIMITER);
            sb.append(expense.getCategory()).append(FIELD_DELIMITER);
            sb.append(expense.getDate());
            if (i < expenses.size() - 1) {
                sb.append(RECORD_DELIMITER);
            }
        }
        editor.putString(EXPENSES_KEY, sb.toString());
        editor.apply();
    }

    public ArrayList<ExpenseModel> loadExpenses() {
        ArrayList<ExpenseModel> expenses = new ArrayList<>();
        String savedString = sharedPreferences.getString(EXPENSES_KEY, null);

        if (TextUtils.isEmpty(savedString)) {
            return expenses;
        }

        String[] records = savedString.split("\\|\\|\\|");
        for (String record : records) {
            String[] fields = record.split(FIELD_DELIMITER);
            if (fields.length == 4) {
                try {
                    expenses.add(new ExpenseModel(fields[0], Double.parseDouble(fields[1]), fields[2], fields[3]));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return expenses;
    }
}
