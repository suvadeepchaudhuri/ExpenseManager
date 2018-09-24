package apps.suvadeep.com.expensemanager.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import apps.suvadeep.com.expensemanager.R;
import apps.suvadeep.com.expensemanager.constants.CategoryEnum;
import apps.suvadeep.com.expensemanager.databaseResource.expense.ExpenseContract;
import apps.suvadeep.com.expensemanager.databaseResource.expense.ExpenseDbHelper;
import apps.suvadeep.com.expensemanager.utils.UtilsCommons;

/**
 * A simple {@link Fragment} subclass that sits in the main activity and holds the add expense data form and functionality.
 */
public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Button addExpensebutton;
    Spinner categorySpinner;
    String selectedCategory = "";
    EditText expName, expAmt;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_expense, container, false);
        addExpensebutton = (Button) rootView.findViewById(R.id.addExpense_button);
        categorySpinner = (Spinner) rootView.findViewById(R.id.category_spinner);
        expName = rootView.findViewById(R.id.edit_ExpenseLabel);
        expAmt = rootView.findViewById(R.id.edit_ExpenseAmount);
        categorySpinner.setOnItemSelectedListener(this);
        populateSpinner(categorySpinner, this.getContext());

        /**
         * Button onClick listener.
         * Check details and add the expense to the database.
         */
        addExpensebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eName = rootView.findViewById(R.id.edit_ExpenseLabel);
                EditText eAmt = rootView.findViewById(R.id.edit_ExpenseAmount);
                if (eName.getText().toString().isEmpty() || eAmt.getText().toString().isEmpty()) {
                    Toast t = Toast.makeText(v.getContext(), "Details Incomplete. Please complete all fields!", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                } else {
                    //Add the expense to the database
                    addExpenseRecord(v);
                }
            }
        });
        return rootView;
    }

    /**
     * Initializes the Categories for the user to select one.
     * Categories can be found in the category enum
     *
     * @param spinner
     * @param context
     */
    public static void populateSpinner(Spinner spinner, Context context) {
        List<String> categories = new ArrayList<>();
        //Get all categories
        for (CategoryEnum cat : CategoryEnum.values()) {
            categories.add(cat.getValue());
        }
        // Creating adapter for spinner and setting it
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    /**
     * Get values from the add-expense form and send the data to the database.
     *
     * @param view
     */
    public void addExpenseRecord(View view) {
        String expenseName, expenseCategory, expenseTimeStamp;
        Float expenseAmount;
        expenseName = expName.getText().toString();
        expenseAmount = Float.parseFloat(expAmt.getText().toString());
        expenseCategory = categorySpinner.getSelectedItem().toString();
        expenseTimeStamp = UtilsCommons.getTimeStamp();
        sendFormDataToDb(expenseName, expenseCategory, expenseAmount, expenseTimeStamp);
        Toast.makeText(this.getContext(), "Expense Added!", Toast.LENGTH_LONG).show();
    }

    /**
     * Method for testing. Pulls all data from the database table and displays it.
     *
     * @return
     */
    public String readExpenses() {
        ExpenseDbHelper expenseDbHelper = new ExpenseDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = expenseDbHelper.getReadableDatabase();

        Cursor cursor = expenseDbHelper.readAllExpenses(sqLiteDatabase);
        String record = "";
        while (cursor.moveToNext()) {
            String e_name = cursor.getString(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_NAME_COL));
            String e_cat = cursor.getString(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_CATEGORY_COL));
            Float e_amt = cursor.getFloat(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_AMOUNT_COL));
            String e_time = cursor.getString(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_TIMESTAMP_COL));
            record = record + e_name + "-" + e_cat + "-" + e_amt + "-" + e_time + "\n";

        }
        expenseDbHelper.close();

        return record;
    }

    /**
     * Call DB helper and add the expense.
     *
     * @param expenseName
     * @param expenseCategory
     * @param expenseAmount
     * @param timeStamp
     */
    public void sendFormDataToDb(String expenseName, String expenseCategory, Float expenseAmount, String timeStamp) {
        ExpenseDbHelper expenseDbHelper = new ExpenseDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = expenseDbHelper.getWritableDatabase();
        expenseDbHelper.addExpense(expenseName, expenseCategory, expenseAmount, timeStamp, sqLiteDatabase);
        expenseDbHelper.close();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCategory = categorySpinner.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
