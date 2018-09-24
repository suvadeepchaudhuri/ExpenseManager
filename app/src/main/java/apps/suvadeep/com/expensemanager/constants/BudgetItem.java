package apps.suvadeep.com.expensemanager.constants;

/**
 * Class to define the Budget item for sending it to the BudgetItem adapter for the recycle view.
 * POJOs of this class are used to set the recycle view.
 */
public class BudgetItem {
    String catName;
    Float catExpenseTotal;
    Float catBudget = 100F;
    Float catBudgetPercentage;

    public void setCatBudgetPercentage(Float catBudgetPercentage) {
        this.catBudgetPercentage = catBudgetPercentage;
        setCatBudgetPercentage();
    }

    public void setCatBudgetPercentage() {
        this.catBudgetPercentage = (catExpenseTotal/catBudget) * 100;
    }

    public BudgetItem(String catName, Float catExpenseTotal) {
        this.catName = catName;
        this.catExpenseTotal = catExpenseTotal;
    }

    public BudgetItem(String catName, Float catExpenseTotal, Float catBudget) {
        this.catName = catName;
        this.catExpenseTotal = catExpenseTotal;
        this.catBudget = catBudget;
        setCatBudgetPercentage();
    }

    public Float getCatBudgetPercentage() {
        return catBudgetPercentage;
    }



    public Float getCatExpenseTotal() {
        return catExpenseTotal;
    }

    public void setCatExpenseTotal(Float catExpenseTotal) {
        this.catExpenseTotal = catExpenseTotal;
    }

    public Float getCatBudget() {
        return catBudget;
    }

    public void setCatBudget(Float catBudget) {
        this.catBudget = catBudget;
        setCatBudgetPercentage();
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    @Override
    public String toString() {
        return catName+","+catExpenseTotal+"/"+catBudget+","+catBudgetPercentage+"%";
    }
}
