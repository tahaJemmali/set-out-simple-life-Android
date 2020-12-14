package tn.esprit.setoutlife.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import tn.esprit.setoutlife.Enums.BalanceEnum;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.BalanceRepository;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.entities.Balance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBalanceFragment#} factory method to
 * create an instance of this fragment.
 */
public class AddBalanceFragment extends Fragment {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    private static final String FRAGMENT_NAME = "Finance";
    FragmentManager fragmentManager;

    //UI
    ImageButton closeBalance;
    EditText exchangeName;
    EditText amountExchange;
    DatePicker dateExchange;
    CheckBox spentCheck;
    CheckBox earnedCheck;
    ImageButton addBalanceBtn;

    //var
    boolean earned;
    boolean spent;

    public AddBalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_add_balance, container, false);
        mContext = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(FRAGMENT_NAME);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                fragmentManager.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        initUI();
        closeBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });
        earnedCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                earned=true;
                spent=false;
                spentCheck.setChecked(false);

            }
        });
        spentCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                earned=false;
                spent=true;
                earnedCheck.setChecked(false);
            }
        });
        addBalanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BalanceEnum type=BalanceEnum.EARNED;
                if(spent)
                    type=BalanceEnum.SPENT;

                if(validator())
                {
                    final ProgressDialog dialogg = ProgressDialog.show(mContext
                            , "","Loading..Wait.." , true);
                    dialogg.show();
                    Balance balance=new Balance("",
                            exchangeName.getText().toString(),
                            type,
                            Double.parseDouble(amountExchange.getText().toString()),
                            getDateFromDatePicker(dateExchange));
                    BalanceRepository.addBalance(mContext,balance,dialogg,fragmentManager);
                }else {
                    new AlertDialog.Builder(mContext)
                            .setTitle("EROOR")
                            .setMessage("Validation")
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        return view;
    }

    boolean validator(){
        if(exchangeName.getText().toString().isEmpty())
            return false;
        if(!spentCheck.isChecked()&&!earnedCheck.isChecked())
            return false;
        if(amountExchange.getText().toString().isEmpty())
            return false;
        try {
            Double.parseDouble(amountExchange.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
    void initUI(){
        closeBalance=view.findViewById(R.id.closeBalance);
        exchangeName=view.findViewById(R.id.exchangeName);
        amountExchange=view.findViewById(R.id.amountExchange);
        dateExchange=view.findViewById(R.id.dateExchange);
        spentCheck=view.findViewById(R.id.spentCheck);
        earnedCheck=view.findViewById(R.id.earnedCheck);
        addBalanceBtn=view.findViewById(R.id.addBalanceBtn);
    }
    public Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}