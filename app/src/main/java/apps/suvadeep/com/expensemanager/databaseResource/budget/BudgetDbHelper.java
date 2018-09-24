package apps.suvadeep.com.expensemanager.databaseResource.budget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import apps.suvadeep.com.expensemanager.databaseResource.DBCommons;

public class BudgetDbHelper extends SQLiteOpenHelper {

    public static final String CREATE_TABLE = "create table "
            + BudgetContract.BudgetEntry.TABLE_NAME + " ("
            + BudgetContract.BudgetEntry.BUDGET_CATEGORY_NAME_COL + " text,"
            + BudgetContract.BudgetEntry.BUDGET_AMOUNT_COL + " number);";
    public static final String DROP_TABLE = "drop table if exists "
            + BudgetContract.BudgetEntry.TABLE_NAME;

    public BudgetDbHelper(Context context) {
        super(context, DBCommons.DATABASE_NAME, null, DBCommons.DATABASE_VERSION);
        Log.d("DB", "Database '" + DBCommons.DATABASE_NAME + "' created.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DBCommons.initialize(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DBCommons.upgrade(db);
    }

    /**
     * Add a row to the budget table.
     *
     * @param budgetCategoryName
     * @param budgetAmount
     * @param sqLiteDatabase
     */
    public static void addBudget(String budgetCategoryName,
                                 Float budgetAmount,
                                 SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BudgetContract.BudgetEntry.BUDGET_CATEGORY_NAME_COL, budgetCategoryName);
        contentValues.put(BudgetContract.BudgetEntry.BUDGET_AMOUNT_COL, budgetAmount);
        sqLiteDatabase.insert(BudgetContract.BudgetEntry.TABLE_NAME, null, contentValues);
        Log.d("DB", "1 row inserted.");
    }

    /**
     * Update the budget for the category name.
     *
     * @param budgetCategoryName
     * @param budgetAmount
     * @param sqLiteDatabase
     */
    public void updateBudget(String budgetCategoryName,
                             Float budgetAmount,
                             SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BudgetContract.BudgetEntry.BUDGET_AMOUNT_COL, budgetAmount);
        String selection = BudgetContract.BudgetEntry.BUDGET_CATEGORY_NAME_COL + " = '" + budgetCategoryName + "'";
        sqLiteDatabase.update(BudgetContract.BudgetEntry.TABLE_NAME, contentValues, selection, null);

    }

    /**
     * Update the budget for the category Total.
     *
     * @param budgetAmount
     * @param sqLiteDatabase
     */
    public void updateTotalBudget(Float budgetAmount,
                             SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BudgetContract.BudgetEntry.BUDGET_AMOUNT_COL, budgetAmount);
        String selection = BudgetContract.BudgetEntry.BUDGET_CATEGORY_NAME_COL + " = '" + BudgetContract.BudgetEntry.TOTAL_RECORD_NAME + "'";
        sqLiteDatabase.update(BudgetContract.BudgetEntry.TABLE_NAME, contentValues, selection, null);

    }

    /**
     * Get the budget for the Category name.
     *
     * @param budgetCategoryName
     * @param sqLiteDatabase
     * @return
     */
    public Cursor getBudget(String budgetCategoryName,
                            SQLiteDatabase sqLiteDatabase) {
        String[] columnsToGet = {BudgetContract.BudgetEntry.BUDGET_AMOUNT_COL};
        String selection = BudgetContract.BudgetEntry.BUDGET_CATEGORY_NAME_COL + " = '" + budgetCategoryName + "'";
        Cursor cursor = sqLiteDatabase.query(BudgetContract.BudgetEntry.TABLE_NAME,
                columnsToGet,
                selection,
                null,
                null,
                null,
                null);
        return cursor;
    }



}
