package tn.esprit.setoutlife.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.TaskRepository;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.entities.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddScheduleFragment#} factory method to
 * create an instance of this fragment.
 */
public class AddScheduleFragment extends Fragment {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    private static final String FRAGMENT_NAME = "Schedule";
    FragmentManager fragmentManager;
    //UI
    EditText taskNameEditText;
    Spinner daysSpinner;
    TimePicker startTime;
    NumberPicker importance;
    TimePicker endTime;
    NumberPicker enjoyment;
    EditText noteEditText;
    ImageButton addScheduleBtn;
    ImageButton closeScheduleBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_schedule, container, false);
        mContext = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();


        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(FRAGMENT_NAME);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                fragmentManager.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        initUI();

        closeScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });
        addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()){
                    int enjoy=enjoyment.getValue();
                    int impo=importance.getValue();
                    Date dateStart=getSelectedDay();
                    Date dateEnd=getSelectedDay();
                    dateStart.setHours(startTime.getHour());
                    dateStart.setMinutes(startTime.getMinute());
                    dateEnd.setHours(endTime.getHour());
                    dateEnd.setMinutes(endTime.getMinute());
                    Task schedule=new Task("",taskNameEditText.getText().toString(),impo,enjoy,noteEditText.getText().toString(),
                            dateStart,dateEnd,true);
                    try {
                        final ProgressDialog dialogg = ProgressDialog.show(mContext
                                , "","Loading..Wait.." , true);
                        dialogg.show();
                        TaskRepository.addSchedule(mContext,schedule,dialogg,fragmentManager);
                        Toast.makeText(mContext, "Task added successfully", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{

                    new AlertDialog.Builder(mContext)
                            .setTitle("EROOR")
                            .setMessage("Validation")
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        return  view;
    }

    void initUI(){
        taskNameEditText=view.findViewById(R.id.taskNameEditText);
        daysSpinner=view.findViewById(R.id.daysSpinner);
        importance=view.findViewById(R.id.importance);
        enjoyment=view.findViewById(R.id.enjoyment);
        endTime=view.findViewById(R.id.endTime);
        noteEditText=view.findViewById(R.id.noteEditText);
        addScheduleBtn=view.findViewById(R.id.addScheduleBtn);
        startTime=view.findViewById(R.id.startTime);
        closeScheduleBtn=view.findViewById(R.id.closeTask);
        importance.setMaxValue(5);
        importance.setMinValue(0);
        importance.setValue(0);
        enjoyment.setMaxValue(5);
        enjoyment.setMinValue(0);
        enjoyment.setValue(0);
    }

    boolean validation(){
        if(taskNameEditText.getText().toString().isEmpty())
            return false;
        if(startTime.getHour()>endTime.getHour())
            return false;

        if(startTime.getHour()==endTime.getHour())
            if(startTime.getMinute()>=endTime.getMinute())
            return false;

            return true;
    }
    Date getSelectedDay(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int day=0;
        switch (daysSpinner.getSelectedItemPosition()){
            case 0:
                day = c.get(Calendar.DAY_OF_MONTH)-c.get(Calendar.DAY_OF_WEEK)+2;
                break;
            case 1:
                day = c.get(Calendar.DAY_OF_MONTH)-c.get(Calendar.DAY_OF_WEEK)+3;
                break;
            case 2:
                day = c.get(Calendar.DAY_OF_MONTH)-c.get(Calendar.DAY_OF_WEEK)+4;
                break;
            case 3:
                day = c.get(Calendar.DAY_OF_MONTH)-c.get(Calendar.DAY_OF_WEEK)+5;
                break;
            case 4:
                day = c.get(Calendar.DAY_OF_MONTH)-c.get(Calendar.DAY_OF_WEEK)+6;
                break;
            case 5:
                day = c.get(Calendar.DAY_OF_MONTH)-c.get(Calendar.DAY_OF_WEEK)+7;
                break;
            case 6:
                day = c.get(Calendar.DAY_OF_MONTH)-c.get(Calendar.DAY_OF_WEEK)+8;
                break;
        }
        c.set(Calendar.DAY_OF_MONTH,day);
    return c.getTime();
    }
    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }
}