package apps.suvadeep.com.expensemanager.constants;

/**
 *Enumeration listing out the categories for the expenses.
 */
public enum CategoryEnum {
    Groceries("Groceries"),
    Utilities("Utilities"),
    EatingOut("Eating Out"),
    Medical("Medical"),
    Shopping("Shopping"),
    Transport("Transport"),
    Sports("Sports"),
    Entertainment("Entertainment"),
    Others("Others");

    private String value;

    CategoryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
