package apps.suvadeep.com.expensemanager.constants;

/**
 * Class to define the Expense item for sending it to the ExpenseItem adapter for the recycle view.
 * POJOs of this class are used to set the recycle view.
 */
public class ExpenseItem {
    String expenseName;
    String expenseCategory;
    Float expenseAmount;
    String expenseTimeStamp;


    public ExpenseItem(String expenseName, String expenseCategory, Float expenseAmount, String expenseTimeStamp) {
        setExpenseName(expenseName);
        setExpenseAmount(expenseAmount);
        setExpenseCategory(expenseCategory);
        setExpenseTimeStamp(expenseTimeStamp);

    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }


    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public Float getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(Float expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseTimeStamp() {
        return expenseTimeStamp;
    }

    public void setExpenseTimeStamp(String expenseTimeStamp) {
        this.expenseTimeStamp = expenseTimeStamp;
    }

    @Override
    public String toString(){
        return expenseName+","+expenseCategory+","+expenseAmount+","+expenseTimeStamp;
    }

}
