package apps.suvadeep.com.expensemanager.databaseResource;

import android.database.sqlite.SQLiteDatabase;

import apps.suvadeep.com.expensemanager.constants.CategoryEnum;
import apps.suvadeep.com.expensemanager.databaseResource.budget.BudgetContract;
import apps.suvadeep.com.expensemanager.databaseResource.budget.BudgetDbHelper;
import apps.suvadeep.com.expensemanager.databaseResource.expense.ExpenseDbHelper;

/**
 * Class that contains common functionality for all database table helpers.
 */
public class DBCommons {
    public static final String DATABASE_NAME = "expense_db";
    public static final int DATABASE_VERSION = 1;

    /**
     * Method to initialize the all databases and all tables in them when any call to the database is made
     * To implement the starting sequence for all and make a call to initialize() in the onCreate/ onStart method
     * of each database helper.
     * @param db
     */
    public static void initialize(SQLiteDatabase db){
        db.execSQL(ExpenseDbHelper.CREATE_TABLE);

        db.execSQL(BudgetDbHelper.CREATE_TABLE);

        Float defaultBudget = 200F;

        //Loading Default Budget Values
        for(CategoryEnum cat : CategoryEnum.values())
        {
            BudgetDbHelper.addBudget(cat.getValue(), defaultBudget, db);
        }

        BudgetDbHelper.addBudget(BudgetContract.BudgetEntry.TOTAL_RECORD_NAME, defaultBudget, db);
    }

    /**
     * Method to upgrade the all databases and all tables in them when any call to the database is made
     * To implement the upgrade sequence for all and make a call to upgrade() in the onUpgrade  method
     * of each database helper.
     * @param db
     */
    public static void upgrade(SQLiteDatabase db){
        db.execSQL(ExpenseDbHelper.DROP_TABLE);

        db.execSQL(BudgetDbHelper.DROP_TABLE);
        initialize(db);
    }
}
