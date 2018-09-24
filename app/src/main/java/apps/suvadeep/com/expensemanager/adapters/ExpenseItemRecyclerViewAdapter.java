package apps.suvadeep.com.expensemanager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import apps.suvadeep.com.expensemanager.R;
import apps.suvadeep.com.expensemanager.constants.ExpenseItem;

import java.util.List;

/**
 * Adapter class to set the POJOs of Expense Item class to the individual recycle view list items of the View Fragment.
 */
public class ExpenseItemRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseItemRecyclerViewAdapter.ExpenseItemViewHolder> {

    private final List<ExpenseItem> expenseList;
    private Context mCtx;
    private OnItemClickListener fListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        fListener = listener;
    }

    public ExpenseItemRecyclerViewAdapter(Context mCtx, List<ExpenseItem> items) {
        this.mCtx = mCtx;
        expenseList = items;
    }

    @Override
    public ExpenseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx)
                .inflate(R.layout.list_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ExpenseItemViewHolder(view, fListener);
    }

    @Override
    public void onBindViewHolder(final ExpenseItemViewHolder holder, int position) {
        ExpenseItem expenseItem = expenseList.get(position);

        holder.mExpenseName.setText(expenseItem.getExpenseName());
        holder.mExpenseCategory.setText(expenseItem.getExpenseCategory());
        holder.mExpenseTimeStamp.setText(expenseItem.getExpenseTimeStamp());
        holder.mExpenseAmount.setText(expenseItem.getExpenseAmount().toString());
        //holder.expenseItem = expenseList.get(position);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public static class ExpenseItemViewHolder extends RecyclerView.ViewHolder {

        public final TextView mExpenseName;
        public final TextView mExpenseCategory;
        public final TextView mExpenseTimeStamp;
        public final TextView mExpenseAmount;
        public final ImageView mExpenseDelete;

        public ExpenseItem expenseItem;

        public ExpenseItemViewHolder(View view, final OnItemClickListener listener) {
            super(view);
            mExpenseName = (TextView) view.findViewById(R.id.textViewExpenseName);
            mExpenseCategory = (TextView) view.findViewById(R.id.textViewExpenseCategory);
            mExpenseTimeStamp = (TextView) view.findViewById(R.id.textViewExpenseDate);
            mExpenseAmount = (TextView) view.findViewById(R.id.textViewExpenseAmount );
            mExpenseDelete = (ImageView) view.findViewById(R.id.image_delete_expense );

            view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position  = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }

                    }
                }
            });

            mExpenseDelete.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position  = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }

                    }
                }
            });
        }




    }
}
