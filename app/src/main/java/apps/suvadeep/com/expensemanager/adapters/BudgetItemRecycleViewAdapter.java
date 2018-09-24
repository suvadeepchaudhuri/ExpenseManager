package apps.suvadeep.com.expensemanager.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.suvadeep.com.expensemanager.R;
import apps.suvadeep.com.expensemanager.constants.BudgetItem;

/**
 * Adapter class to set the POJOs of Budget Item class to the individual recycle view list items of the Budget Fragment.
 */
public class BudgetItemRecycleViewAdapter extends RecyclerView.Adapter<BudgetItemRecycleViewAdapter.BudgetItemViewHolder> {

    private List<BudgetItem> mBudList;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public BudgetItemRecycleViewAdapter(List<BudgetItem> budList){
        mBudList = budList;
    }

    @Override
    public BudgetItemRecycleViewAdapter.BudgetItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_list_item, parent,false);
        BudgetItemViewHolder budgetItemViewHolder = new BudgetItemViewHolder(v,mListener);
        return budgetItemViewHolder;
    }

    @Override
    public void onBindViewHolder(BudgetItemRecycleViewAdapter.BudgetItemViewHolder holder, int position) {
        BudgetItem currentItem = mBudList.get(position);
        holder.bCatName.setText(currentItem.getCatName());
        holder.bCatBudgetAmt.setText("Spent/Budget: "+currentItem.getCatExpenseTotal()+"/"+currentItem.getCatBudget().toString());
        holder.bCatPercentage.setText(currentItem.getCatBudgetPercentage().intValue()+"%");
        holder.bCatProgressBar.setProgress(currentItem.getCatBudgetPercentage().intValue());
    }

    @Override
    public int getItemCount() {
        return mBudList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public static class BudgetItemViewHolder extends RecyclerView.ViewHolder{

        public TextView bCatName;
        public TextView bCatBudgetAmt;
        public TextView bCatPercentage;
        public ProgressBar bCatProgressBar;
         public BudgetItemViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
             bCatName = itemView.findViewById(R.id.cat_name_budget);
             bCatBudgetAmt = itemView.findViewById(R.id.cat_budget_amount);
             bCatPercentage = itemView.findViewById(R.id.cat_budget_percentage);
             bCatProgressBar = itemView.findViewById(R.id.cat_budget_progressBar);

             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                 }
             });
        }
    }
}
