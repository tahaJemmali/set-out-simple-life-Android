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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.TagRepository;
import tn.esprit.setoutlife.Repository.TaskRepository;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.entities.Tag;
import tn.esprit.setoutlife.entities.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    private static final String FRAGMENT_NAME = "Schedule";
    FragmentManager fragmentManager;
    //UI
    TimetableView timetable;
    Button addTaskInSchedule;
    TextView weekDate;
    //data base
    public static ArrayList<Task> global;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        mContext = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();
        //load Data from data base


        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(FRAGMENT_NAME);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                System.out.println("on back pressed ");
                if (callBackInterface != null) {
                    callBackInterface.popBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        initUI();
        initSchedule();
        addSchdule();
        addTaskInSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddScheduleFragment taskFrag = new AddScheduleFragment();
                fragmentManager
                        .beginTransaction().add(R.id.fragment_container, taskFrag, "add Task")
                        .addToBackStack(null)
                        .commit();
            }
        });
        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                final int index=idx;
                new AlertDialog.Builder(mContext)
                        .setTitle("Delete entry")
                        .setMessage("Is this task finished ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                final ProgressDialog dialogg = ProgressDialog.show(mContext
                                        , "","Loading..Wait.." , true);
                                dialogg.show();
                                TaskRepository.deleteTask(mContext,global.get(index),dialogg,fragmentManager);
                                timetable.remove(index);
                                Toast.makeText(mContext, "Well done !", Toast.LENGTH_LONG).show();
                            }
                        })// A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


        return view;
    }

    void initUI(){

    }
    void initSchedule(){
        timetable=view.findViewById(R.id.timetable);
        addTaskInSchedule=view.findViewById(R.id.addTaskInSchedule);
        weekDate=view.findViewById(R.id.weekDate);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, d. MMMM yyyy");
        weekDate.setText(formatter.format(new Date()));
    }
    void addSchdule(){
        if(global!=null){
            Log.e("TAG", "addSchdule: "+global );
            for(Task row:global){
                ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                Schedule schedule = new Schedule();
                schedule.setClassTitle(row.getTaskName()); // sets subject
                schedule.setClassPlace("note :"+row.getNote()); // sets place
                schedule.setProfessorName(""+row.getImportance()); // sets professor
                Calendar c = Calendar.getInstance();
                c.setTime(row.getDateCreation());
                int day=c.get(Calendar.DAY_OF_WEEK)-2;
                if (day<0)
                    day=6;
                schedule.setDay(day);
                schedule.setStartTime(new Time(row.getDateCreation().getHours(),row.getDateCreation().getMinutes())); // sets the beginning of class time (hour,minute)
                schedule.setEndTime(new Time(row.getEndTime().getHours(),row.getEndTime().getMinutes())); // sets the end of class time (hour,minute)
                schedules.add(schedule);
                timetable.add(schedules);
        }
        }
    }

    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }


}