package apps.suvadeep.com.expensemanager.databaseResource.expense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import apps.suvadeep.com.expensemanager.databaseResource.DBCommons;
import apps.suvadeep.com.expensemanager.utils.UtilsCommons;

public class ExpenseDbHelper extends SQLiteOpenHelper {


    public static final String CREATE_TABLE = "create table "
            + ExpenseContract.ExpenseEntry.TABLE_NAME + " ("
            + ExpenseContract.ExpenseEntry.EXPENSE_NAME_COL + " text,"
            + ExpenseContract.ExpenseEntry.EXPENSE_AMOUNT_COL + " number,"
            + ExpenseContract.ExpenseEntry.EXPENSE_CATEGORY_COL + " text,"
            + ExpenseContract.ExpenseEntry.EXPENSE_TIMESTAMP_COL + " text);";
    public static final String DROP_TABLE = "drop table if exists "
            + ExpenseContract.ExpenseEntry.TABLE_NAME;


    public ExpenseDbHelper(Context context) {
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
     * Add an expense to the expense table
     * @param expenseName
     * @param expenseCategory
     * @param expenseAmount
     * @param expenseTimeStamp
     * @param sqLiteDatabase
     */
    public void addExpense(String expenseName,
                           String expenseCategory,
                           Float expenseAmount,
                           String expenseTimeStamp,
                           SQLiteDatabase sqLiteDatabase) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(ExpenseContract.ExpenseEntry.EXPENSE_NAME_COL, expenseName);
        contentValues.put(ExpenseContract.ExpenseEntry.EXPENSE_CATEGORY_COL, expenseCategory);
        contentValues.put(ExpenseContract.ExpenseEntry.EXPENSE_AMOUNT_COL, expenseAmount);
        contentValues.put(ExpenseContract.ExpenseEntry.EXPENSE_TIMESTAMP_COL, expenseTimeStamp);

        sqLiteDatabase.insert(ExpenseContract.ExpenseEntry.TABLE_NAME, null, contentValues);
        Log.d("DB", "1 row inserted.");
    }

    /**
     * Update an expense of the expense table
     * @param expenseName
     * @param expenseCategory
     * @param expenseAmount
     * @param expenseTimeStamp
     * @param sqLiteDatabase
     */
    public void upadteExpense(String expenseName,
                           String expenseCategory,
                           Float expenseAmount,
                           String expenseTimeStamp,
                           SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ExpenseContract.ExpenseEntry.EXPENSE_NAME_COL, expenseName);
        contentValues.put(ExpenseContract.ExpenseEntry.EXPENSE_CATEGORY_COL, expenseCategory);
        contentValues.put(ExpenseContract.ExpenseEntry.EXPENSE_AMOUNT_COL, expenseAmount);
        String selection = ExpenseContract.ExpenseEntry.EXPENSE_TIMESTAMP_COL + "= '" + expenseTimeStamp +"'";
        sqLiteDatabase.update(ExpenseContract.ExpenseEntry.TABLE_NAME,contentValues, selection, null);
    }

    /**
     * Retrieve all expense in the expense Table
     * @param sqLiteDatabase
     * @return
     */
    public Cursor readAllExpenses(SQLiteDatabase sqLiteDatabase) {
        String[] columnsToGet = {ExpenseContract.ExpenseEntry.EXPENSE_NAME_COL,
                ExpenseContract.ExpenseEntry.EXPENSE_AMOUNT_COL,
                ExpenseContract.ExpenseEntry.EXPENSE_CATEGORY_COL,
                ExpenseContract.ExpenseEntry.EXPENSE_TIMESTAMP_COL
        };

        Cursor cursor = sqLiteDatabase.query(ExpenseContract.ExpenseEntry.TABLE_NAME,
                columnsToGet,
                null,
                null,
                null,
                null,
                null);
        return  cursor;
    }

    /**
     * Get the total of expenses in each category for the current month
     * @param sqLiteDatabase
     * @return
     */
    public Cursor getTotalExpensesByCategoryCurrentMonth(SQLiteDatabase sqLiteDatabase){
        String[] columnsToGet = {ExpenseContract.ExpenseEntry.EXPENSE_CATEGORY_COL,
                "sum("+ExpenseContract.ExpenseEntry.EXPENSE_AMOUNT_COL+")"
        };
        String selection = "substr("+ExpenseContract.ExpenseEntry.EXPENSE_TIMESTAMP_COL+",1,7) = '" + UtilsCommons.getCurrentYearMonth() +"'";
        Cursor cursor = sqLiteDatabase.query(ExpenseContract.ExpenseEntry.TABLE_NAME,
                columnsToGet,
                selection,
                null,
                ExpenseContract.ExpenseEntry.EXPENSE_CATEGORY_COL,
                null,
                ExpenseContract.ExpenseEntry.EXPENSE_CATEGORY_COL);
        return cursor;
    }

    /**
     * Get the total for all expenses in the current month.
     * @param sqLiteDatabase
     * @return
     */
    public Float getTotalExpensesForCurrentMonth(SQLiteDatabase sqLiteDatabase){
        String[] columnsToGet = {"sum("+ExpenseContract.ExpenseEntry.EXPENSE_AMOUNT_COL+")"
        };
        String selection = "substr("+ExpenseContract.ExpenseEntry.EXPENSE_TIMESTAMP_COL+",1,7) = '" + UtilsCommons.getCurrentYearMonth() +"'";
        Cursor cursor = sqLiteDatabase.query(ExpenseContract.ExpenseEntry.TABLE_NAME,
                columnsToGet,
                selection,
                null,
                null,
                null,
                null);
        Float total = 0F;
        while(cursor.moveToNext()){
            total = cursor.getFloat(0);
            break;
        }
        return total;

    }

    /**
     * Delete a particular expense
     * @param timeStamp
     * @param sqLiteDatabase
     */
    public void deleteExpense(String timeStamp, SQLiteDatabase sqLiteDatabase){
        String selection = ExpenseContract.ExpenseEntry.EXPENSE_TIMESTAMP_COL + "= '" + timeStamp +"'";
        sqLiteDatabase.delete(ExpenseContract.ExpenseEntry.TABLE_NAME, selection,null);

    }

    /**
     * Delete all expenses in expense table
     * @param sqLiteDatabase
     */
    public void deleteAllExpenses(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.delete(ExpenseContract.ExpenseEntry.TABLE_NAME, null,null);

    }
}
