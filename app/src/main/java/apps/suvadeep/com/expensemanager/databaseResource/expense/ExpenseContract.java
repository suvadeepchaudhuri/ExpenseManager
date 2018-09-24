package apps.suvadeep.com.expensemanager.databaseResource.expense;

public final class ExpenseContract {

    private ExpenseContract(){}

    public static class ExpenseEntry{

        public static final String TABLE_NAME = "expense_table";
        public static final String EXPENSE_NAME_COL = "expense_name";
        public static final String EXPENSE_AMOUNT_COL = "expense_amount";
        public static final String EXPENSE_CATEGORY_COL = "expense_category";
        public static final String EXPENSE_TIMESTAMP_COL = "expense_timestamp";

    }
}
