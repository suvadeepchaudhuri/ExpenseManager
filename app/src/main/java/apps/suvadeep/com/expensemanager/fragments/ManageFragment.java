package apps.suvadeep.com.expensemanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import apps.suvadeep.com.expensemanager.R;
import apps.suvadeep.com.expensemanager.constants.ExpenseItem;
import apps.suvadeep.com.expensemanager.databaseResource.expense.ExpenseContract;
import apps.suvadeep.com.expensemanager.databaseResource.expense.ExpenseDbHelper;
import apps.suvadeep.com.expensemanager.utils.UtilsCommons;
import apps.suvadeep.com.expensemanager.utils.services.GoogleServices;


/**
 * A simple {@link Fragment} subclass to se the fragment that contains the manage app options.
 */
public class ManageFragment extends Fragment {

    Button clearDbButton;
    Button exportButton;
    Button uploadButton;
    SignInButton googleSignInButton;
    LinearLayout signInLayout, userDetailsLayout;
    String latestExportFilePath = "";

    public ManageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.getContext());
        if(account!=null){

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context ctx = this.getContext();
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_manage, container, false);
        clearDbButton = (Button) rootView.findViewById(R.id.cleardb_button);
        exportButton = (Button) rootView.findViewById(R.id.export_button);
        uploadButton = (Button) rootView.findViewById(R.id.upload_button);
        signInLayout = (LinearLayout) rootView.findViewById(R.id.sign_in_layout);
        userDetailsLayout = (LinearLayout) rootView.findViewById(R.id.google_details);
        googleSignInButton = rootView.findViewById(R.id.sign_in_google_button);
        signInLayout.setVisibility(LinearLayout.INVISIBLE);
        userDetailsLayout.setVisibility(LinearLayout.INVISIBLE);

        // Handle clicks to the clear all expenses button
        clearDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder eBuilder = new AlertDialog.Builder(ctx);
                final View eView = getLayoutInflater().inflate(R.layout.alert_delete_db, null);
                Button bYes = eView.findViewById(R.id.yes_button);
                Button bNo= eView.findViewById(R.id.no_button);
                eBuilder.setView(eView);
                final AlertDialog dialog = eBuilder.create();
                dialog.show();
                bYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearAllExpenseRecords();
                        Toast.makeText(ctx, "All Expenses deleted!",Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                bNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });

        // Handle clicks to the clear all expenses button
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExpenseExportToFile();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });

        // Handle clicks to the google sign in button
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle(rootView.getContext());
                //userDetailsLayout.setVisibility(LinearLayout.VISIBLE);
                signInLayout.setVisibility(LinearLayout.INVISIBLE);
            }
        });
        return rootView;
    }

    private void signInGoogle(Context context) {
        //TODO implement sign in
        Intent myIntent = new Intent(this.getContext(), GoogleServices.class);
        myIntent.putExtra("filePathToUpload", latestExportFilePath); //Optional parameters
        this.getContext().startActivity(myIntent);
//         GoogleServices googleServices = new GoogleServices(this.getActivity());
        //TODO:
        // googleServices.saveFileToDrive("");
    }

    /**
     * Make a call to the database to  clear all expenses.
     */
    public void clearAllExpenseRecords(){
        ExpenseDbHelper expenseDbHelper = new ExpenseDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = expenseDbHelper.getWritableDatabase();
        expenseDbHelper.deleteAllExpenses(sqLiteDatabase);
        expenseDbHelper.close();

    }

    /**
     * Create an expense export file and place it in the Storage app data folder.
     */
    public void createExpenseExportToFile(){
        ExpenseDbHelper expenseDbHelper = new ExpenseDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = expenseDbHelper.getReadableDatabase();

        File f = new File(this.getContext().getExternalFilesDir(null)+"/ExpenseManagerExports");
        if(f.exists()){
            f = new File(f.getPath()+"/ExpenseExport_"+ UtilsCommons.getRawTimeStamp()+".csv");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            f.mkdir();
           f = new File(f.getPath()+"/ExpenseExport_"+ UtilsCommons.getRawTimeStamp()+".csv");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        latestExportFilePath = f.getPath();

        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(f);
            bw = new BufferedWriter(fw);
            bw.write("Expense Name,Expense Category,Expense Amount,Expense Timestamp");
            bw.newLine();
            Cursor cursor = expenseDbHelper.readAllExpenses(sqLiteDatabase);
            while (cursor.moveToNext()){
                String e_name = cursor.getString(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_NAME_COL));
                String e_cat = cursor.getString(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_CATEGORY_COL));
                Float e_amt = cursor.getFloat(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_AMOUNT_COL));
                String e_time = cursor.getString(cursor.getColumnIndex(ExpenseContract.ExpenseEntry.EXPENSE_TIMESTAMP_COL));
                bw.write(new ExpenseItem(e_name, e_cat, e_amt, e_time).toString());
                bw.newLine();
            }
            expenseDbHelper.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(), "Error in Writing Export File!",Toast.LENGTH_SHORT).show();
        } finally {
            try {
                bw.close();
                fw.close();

                Toast.makeText(this.getContext(), "File Exported: "+f.getAbsolutePath(),Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
