package apps.suvadeep.com.expensemanager.databaseResource.budget;

public class BudgetContract {
    private BudgetContract(){}

    public static class BudgetEntry{

        public static final String TABLE_NAME = "category_budget_table";
        public static final String BUDGET_CATEGORY_NAME_COL = "category_name";
        public static final String BUDGET_AMOUNT_COL = "category_budget";
        public static final String TOTAL_RECORD_NAME = "Total";

    }
}
