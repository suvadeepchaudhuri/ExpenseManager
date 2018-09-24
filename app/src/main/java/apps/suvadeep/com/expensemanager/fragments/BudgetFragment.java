package apps.suvadeep.com.expensemanager.fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import apps.suvadeep.com.expensemanager.R;
import apps.suvadeep.com.expensemanager.adapters.BudgetItemRecycleViewAdapter;
import apps.suvadeep.com.expensemanager.constants.BudgetItem;
import apps.suvadeep.com.expensemanager.databaseResource.budget.BudgetContract;
import apps.suvadeep.com.expensemanager.databaseResource.budget.BudgetDbHelper;
import apps.suvadeep.com.expensemanager.databaseResource.expense.ExpenseDbHelper;

/**
 * A simple {@link Fragment} subclass that sits in the main activity and holds the view and edit budget data and functionality.
 */
public class BudgetFragment extends Fragment {

    List<BudgetItem> budgetItemList;
    BudgetItem totalItem;
    private RecyclerView bRecyclerView;
    private BudgetItemRecycleViewAdapter bAdapter;
    private RecyclerView.LayoutManager bLayoutManager;
    TextView expenseTotalDetails;
    TextView expenseTotalPercentage;
    ProgressBar totalExpenseProgress;

    public BudgetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_budget, container, false);
        budgetItemList = getCategoryBudgetList();
        bRecyclerView = rootView.findViewById(R.id.category_budget_list);
        bRecyclerView.setHasFixedSize(true);
        bLayoutManager = new LinearLayoutManager(this.getActivity());
        bAdapter = new BudgetItemRecycleViewAdapter(budgetItemList);
        bRecyclerView.setLayoutManager(bLayoutManager);
        bRecyclerView.setAdapter(bAdapter);
        bAdapter.setOnItemClickListener(new BudgetItemRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                editBudget(position);
            }
        });

        //Handle Total Budget
        //Initialize Total POJO
        totalItem = new BudgetItem(BudgetContract.BudgetEntry.TOTAL_RECORD_NAME,
                getTotalExpenditure(),
                getBudgetForCategory(BudgetContract.BudgetEntry.TOTAL_RECORD_NAME));
        expenseTotalDetails = rootView.findViewById(R.id.cat_totalBudget_amount);
        expenseTotalDetails.setText("Spent/Budget: "+totalItem.getCatExpenseTotal()+"/"+totalItem.getCatBudget());
        expenseTotalPercentage = rootView.findViewById(R.id.cat_totalBudget_percentage);
        int totalExpensePercentage = totalItem.getCatBudgetPercentage().intValue();
        expenseTotalPercentage.setText(totalExpensePercentage+"%");
        totalExpenseProgress = rootView.findViewById(R.id.cat_totalBudget_progressBar);
        totalExpenseProgress.setProgress(totalExpensePercentage,true);
        ConstraintLayout totLayout = rootView.findViewById(R.id.total_details);
        if(totalItem.getCatExpenseTotal()==0){
            totLayout.setVisibility(View.GONE);
        }
        totLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder eBuilder = new AlertDialog.Builder(rootView.getContext());
                final View eView = getLayoutInflater().inflate(R.layout.alert_edit_budget, null);
                final EditText b_Amt = eView.findViewById(R.id.editBudget_editText);
                final Button e_Edit = eView.findViewById(R.id.update_budget_buton);
                eBuilder.setView(eView);
                final AlertDialog dialog = eBuilder.create();
                dialog.show();
                e_Edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Verify the details and update the budget.
                        if (b_Amt.getText().toString().isEmpty()) {
                            Toast t = Toast.makeText(v.getContext(), "Details Incomplete. Please complete all fields!", Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();

                        } else {
                            Float newBud = Float.parseFloat(b_Amt.getText().toString());
                            updateTotalBudgetDb(newBud);
                            totalItem.setCatBudget(newBud);
                            expenseTotalDetails.setText("Spent/Budget: "+totalItem.getCatExpenseTotal()+"/"+totalItem.getCatBudget());
                            int totalExpensePercentage = totalItem.getCatBudgetPercentage().intValue();
                            expenseTotalPercentage.setText(totalExpensePercentage+"%");
                            totalExpenseProgress.setProgress(totalExpensePercentage);
                            Toast.makeText(eView.getContext(), "Budget Updated.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }

                    }
                });
            }
        });

        return rootView;
    }

    /**
     * Function to get the reference of the item to be edited and passes the information to the edit dialog box
     *
     * @param position
     */
    private void editBudget(int position) {
        BudgetItem item = budgetItemList.get(position);
        setEditBox(item, position);
    }

    /**
     * Set the edit dialog and accept data to edit the budget of the category clicked.
     *
     * @param budgetItem
     * @param position
     */
    public void setEditBox(final BudgetItem budgetItem, final int position) {
        AlertDialog.Builder eBuilder = new AlertDialog.Builder(this.getActivity());
        final View eView = getLayoutInflater().inflate(R.layout.alert_edit_budget, null);
        final EditText b_Amt = eView.findViewById(R.id.editBudget_editText);
        final Button e_Edit = eView.findViewById(R.id.update_budget_buton);
        eBuilder.setView(eView);
        final AlertDialog dialog = eBuilder.create();
        dialog.show();
        e_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verify the details and update the budget.
                if (b_Amt.getText().toString().isEmpty()) {
                    Toast t = Toast.makeText(v.getContext(), "Details Incomplete. Please complete all fields!", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();

                } else {
                    updateBudgetDb(position, Float.parseFloat(b_Amt.getText().toString()));
                    changeBudgetConfig(position, Float.parseFloat(b_Amt.getText().toString()));
                    Toast.makeText(eView.getContext(), "Budget Updated.", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }

            }
        });


    }

    public void updateTotalBudgetDb(float newBudget) {
        BudgetDbHelper budgetDbHelper = new BudgetDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = budgetDbHelper.getWritableDatabase();
        budgetDbHelper.updateTotalBudget(newBudget, sqLiteDatabase);
        budgetDbHelper.close();
    }

    /**
     * Call the database and update the budget of an item
     *
     * @param position
     * @param newBudget
     */
    public void updateBudgetDb(int position, float newBudget) {
        BudgetDbHelper budgetDbHelper = new BudgetDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = budgetDbHelper.getWritableDatabase();
        budgetDbHelper.updateBudget(budgetItemList.get(position).getCatName(), newBudget, sqLiteDatabase);
        budgetDbHelper.close();
    }

    /**
     * Update the view of the changes in budget that were made.
     *
     * @param position
     * @param newBudget
     */
    public void changeBudgetConfig(int position, Float newBudget) {
        budgetItemList.get(position).setCatBudget(newBudget);
        bAdapter.notifyItemChanged(position, null);
    }

    /**
     * Makes a call to the database and retrieves the budgets by category to be supplied to the view.
     * Hits the expense table and gets data for the current month.
     *
     * @return
     */
    public ArrayList<BudgetItem> getCategoryBudgetList() {
        ExpenseDbHelper expenseDbHelper = new ExpenseDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = expenseDbHelper.getReadableDatabase();
        Cursor cursor = expenseDbHelper.getTotalExpensesByCategoryCurrentMonth(sqLiteDatabase);
        ArrayList<BudgetItem> itemsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String catName = cursor.getString(0);
            Float catTotAmt = cursor.getFloat(1);
            Float catBudget = getBudgetForCategory(catName);
            itemsList.add(new BudgetItem(catName, catTotAmt, catBudget));

        }

        expenseDbHelper.close();
        return itemsList;
    }

    public Float getTotalExpenditure() {
        ExpenseDbHelper expenseDbHelper = new ExpenseDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = expenseDbHelper.getReadableDatabase();
        return expenseDbHelper.getTotalExpensesForCurrentMonth(sqLiteDatabase);
    }

    /**
     * Gets the budget for the specified category from the database.
     *
     * @param catName
     * @return
     */
    private Float getBudgetForCategory(String catName) {
        BudgetDbHelper budgetDbHelper = new BudgetDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = budgetDbHelper.getReadableDatabase();
        Cursor cursor = budgetDbHelper.getBudget(catName, sqLiteDatabase);
        Float budForCat = 0F;
        while (cursor.moveToNext()) {
            budForCat = cursor.getFloat(0);
            break;
        }
        budgetDbHelper.close();
        return budForCat;
    }

}
