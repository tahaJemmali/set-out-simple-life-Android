package tn.esprit.setoutlife.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Enums.BalanceEnum;
import tn.esprit.setoutlife.Fragments.FinanceFragment;
import tn.esprit.setoutlife.Fragments.ProjectFragment;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.BalanceRepository;
import tn.esprit.setoutlife.Repository.ProjectRepository;
import tn.esprit.setoutlife.entities.Balance;
import tn.esprit.setoutlife.entities.Task;


public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.BalanceHolder> {
    private Context mContext;
    private ArrayList<Balance> balances;

    public BalanceAdapter(Context mContext,ArrayList<Balance> balances){
        this.mContext = mContext;
        this.balances = balances;
    }

    @NonNull
    @Override
    public BalanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.balance_single,parent,false);
        return new BalanceAdapter.BalanceHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceHolder holder, int position) {
        Balance balance = balances.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        holder.balanceName.setText(balance.getName());
        holder.dateBalance.setText(formatter.format(balance.getDateCreated()));
        if(balance.getType()== BalanceEnum.EARNED){
            holder.amountBalance.setText("+"+balance.getAmount().toString()+" DNT");
            holder.amountBalance.setTextColor(Color.GREEN);
            holder.balanceTagColor.getBackground().mutate().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            holder.balanceArrow.setImageResource(R.drawable.greenarrow_balance);
        }else {
            holder.amountBalance.setText("-"+balance.getAmount().toString()+" DNT");
            holder.amountBalance.setTextColor(Color.RED);
            holder.balanceTagColor.getBackground().mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            holder.balanceArrow.setImageResource(R.drawable.redarrow_balance);
        }

    }

    @Override
    public int getItemCount() {
        return balances.size();
    }

    public class BalanceHolder extends RecyclerView.ViewHolder {
        ImageView balanceArrow;
        View balanceTagColor;
        TextView balanceName;
        TextView dateBalance;
        TextView amountBalance;
        public BalanceHolder(@NonNull final View itemView) {
            super(itemView);
            balanceArrow=itemView.findViewById(R.id.balanceArrow);
            balanceTagColor=itemView.findViewById(R.id.balanceTagColor);
            balanceName=itemView.findViewById(R.id.balanceName);
            dateBalance=itemView.findViewById(R.id.dateBalance);
            amountBalance= itemView.findViewById(R.id.amountBalance);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this Project ?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    HomeActivity activity = (HomeActivity) view.getContext();
                                    Balance balance=BalanceRepository.balances.get(getAdapterPosition());
                                    final ProgressDialog dialogg = ProgressDialog.show(mContext
                                            , "","Loading..Wait.." , true);
                                    dialogg.show();
                                    BalanceRepository.deleteBalance(mContext,balance.getId(),dialogg,activity.getSupportFragmentManager());
                                    Toast.makeText(mContext, "Balance Deleted Successfully!", Toast.LENGTH_LONG).show();

                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });
        }
    }
}
