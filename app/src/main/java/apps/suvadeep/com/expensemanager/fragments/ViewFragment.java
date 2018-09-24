package apps.suvadeep.com.expensemanager.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import apps.suvadeep.com.expensemanager.R;
import apps.suvadeep.com.expensemanager.adapters.ExpenseItemRecyclerViewAdapter;
import apps.suvadeep.com.expensemanager.constants.ExpenseItem;
import apps.suvadeep.com.expensemanager.databaseResource.expense.ExpenseContract;
import apps.suvadeep.com.expensemanager.databaseResource.expense.ExpenseDbHelper;


/**
 * A simple {@link Fragment} subclass that sits in the main activity and holds the view expense data list and functionality.
 */
public class ViewFragment extends Fragment {

    List<ExpenseItem> expenseItemList;
    RecyclerView recyclerView;
    ExpenseItemRecyclerViewAdapter expenseItemAdapter;

    public ViewFragment() {
        // Required empty public constructor
    }

    /**
     * Handle deleting the list item at position supplied by the parameter
     *
     * @param position
     */
    public void removeItem(int position) {
        ExpenseItem e = expenseItemList.get(position);
        deleteExpense(e.getExpenseTimeStamp());
        expenseItemList.remove(position);
        expenseItemAdapter.notifyItemRemoved(position);
    }

    /**
     * Handle editing the list item values at position supplied by the parameter
     *
     * @param position
     */
    public void editItem(int position) {
        ExpenseItem e = expenseItemList.get(position);
        setEditBox(e, position);
        expenseItemAdapter.notifyItemChanged(position, null);
    }

    /**
     * Inform the recycle view adapter and refresh it.
     *
     * @param position
     * @param expenseName
     * @param expenseCategory
     * @param expenseAmount
     */
    public void changeExpenseConfig(int position, String expenseName,
                                    String expenseCategory,
                                    String expenseAmount) {
        expenseItemList.get(position).setExpenseName(expenseName);
        expenseItemList.get(position).setExpenseCategory(expenseCategory);
        expenseItemList.get(position).setExpenseAmount(Float.parseFloat(expenseAmount));
        expenseItemAdapter.notifyItemChanged(position, null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view, container, false);
        expenseItemList = readExpenses();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.expense_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        expenseItemAdapter = new ExpenseItemRecyclerViewAdapter(this.getActivity(), expenseItemList);
        recyclerView.setAdapter(expenseItemAdapter);

        expenseItemAdapter.setOnItemClickListener(new ExpenseItemRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                expenseItemList.get(position);
                //Open edit box
                editItem(position);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });

        return rootView;
    }

    /**
     * Create and set the edit item Dialogue box
     *
     * @param expenseItem
     * @param position
     */
    public void setEditBox(final ExpenseItem expenseItem, final int position) {

        final AlertDialog.Builder eBuilder = new AlertDialog.Builder(this.getActivity());
        final View eView = getLayoutInflater().inflate(R.layout.alert_edit_expense, null);
        final EditText e_name = eView.findViewById(R.id.editexpense_ExpenseName);
        final EditText e_amt = eView.findViewById(R.id.edit_ExpenseAmount);
        final Spinner e_Spinner = eView.findViewById(R.id.editexpense_category_spinner);
        AddFragment.populateSpinner(e_Spinner, this.getContext());
        final Button e_Edit = eView.findViewById(R.id.edit_expenseButton);
        e_name.setText(expenseItem.getExpenseName());
        e_amt.setText(expenseItem.getExpenseAmount().toString());
        eBuilder.setView(eView);
        final AlertDialog dialog = eBuilder.create();
        dialog.show();
        e_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*If Edit item details are incomplete, alert the user else make changes to the expense item, push to DB and notify view*/
                if (e_name.getText().toString().isEmpty() || e_amt.getText().toString().isEmpty()) {
                    Toast t = Toast.makeText(v.getContext(), "Details Incomplete. Please complete all fields!", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();

                } else {
                    updateExpense(e_name.getText().toString(), e_Spinner.getSelectedItem().toString(), e_amt.getText().toString(), expenseItem.getExpenseTimeStamp());
                    changeExpenseConfig(position, e_name.getText().toString(), e_Spinner.getSelectedItem().toString(), e_amt.getText().toString());

                    Toast.makeText(eView.getContext(), "Expense Updated.", Toast.LENGTH_SHORT).show();
                    dialog.cancel();

                }

            }
        });


    }


    /**
     * Read all expenses from the DB and return a list of the Expense Item format objects.
     * @return
     */
    public List<ExpenseItem> readExpenses() {
        ExpenseDbHelper expenseDbHelper = new ExpenseDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = expenseDbHelper.getReadableDatabase();
        List<ExpenseItem> itemsList = new ArrayList<>();
        Cursor cursor = expenseDbHelper.readAllExpenses(sqLiteDatabase);
        while (cursor.moveToNext()) {
            String e_name = cursor.getString(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_NAME_COL));
            String e_cat = cursor.getString(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_CATEGORY_COL));
            Float e_amt = cursor.getFloat(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_AMOUNT_COL));
            String e_time = cursor.getString(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_TIMESTAMP_COL));
            itemsList.add(new ExpenseItem(e_name, e_cat, e_amt, e_time));

        }
        expenseDbHelper.close();

        return itemsList;
    }

    /**
     * Call the database and delete the expense.
     * @param timeStamp
     */
    public void deleteExpense(String timeStamp) {
        ExpenseDbHelper expenseDbHelper = new ExpenseDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = expenseDbHelper.getWritableDatabase();
        expenseDbHelper.deleteExpense(timeStamp, sqLiteDatabase);
        expenseDbHelper.close();
    }

    /**
     * Call the database and update the expense.
     * @param expenseName
     * @param expenseCategory
     * @param expenseAmount
     * @param expenseTimeStamp
     */
    public void updateExpense(String expenseName,
                              String expenseCategory,
                              String expenseAmount,
                              String expenseTimeStamp) {
        ExpenseDbHelper expenseDbHelper = new ExpenseDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = expenseDbHelper.getWritableDatabase();
        expenseDbHelper.upadteExpense(expenseName, expenseCategory, Float.parseFloat(expenseAmount), expenseTimeStamp, sqLiteDatabase);
        expenseDbHelper.close();
    }

}
