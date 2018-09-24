package apps.suvadeep.com.expensemanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import apps.suvadeep.com.expensemanager.fragments.AddFragment;
import apps.suvadeep.com.expensemanager.fragments.BudgetFragment;
import apps.suvadeep.com.expensemanager.fragments.ManageFragment;
import apps.suvadeep.com.expensemanager.fragments.ViewFragment;

/**
 * @author Suvadeep
 * An expense manager app that allows you to add, edit and manage your expense and shows you the spendings per category.
 * It allows the user to budget by giving the user an overview of the amount left in each category. The user can delete
 * all expenses, can set budgets and can export the data to the phone storage.
 */
public class MainActivity extends AppCompatActivity {

    AddFragment addFragment = new AddFragment();
    ViewFragment viewFragment = new ViewFragment();
    ManageFragment manageFragment = new ManageFragment();
    BudgetFragment budgetFragment = new BudgetFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Open Fragment that is tapped.
            switch (item.getItemId()) {
                case R.id.navigation_add:
                    initFragment(addFragment);
                    return true;
                case R.id.navigation_view:
                    initFragment(viewFragment);
                    return true;
                case R.id.navigation_manage:
                    initFragment(manageFragment);
                    return true;
                case R.id.navigation_budget:
                    initFragment(budgetFragment);
                    return true;
            }
            return false;
        }
    };

    /**
     * Activity on create: Set up the views
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initFragment(addFragment);
    }

    /**
     * Method to initialize a fragment when the icon is tapped.
     *
     * @param fragment
     */
    public void initFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.base_frame, fragment);
        fragmentTransaction.commit();
    }

}
