package tn.esprit.setoutlife.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import tn.esprit.setoutlife.Adapters.GridAdapter;
import tn.esprit.setoutlife.Adapters.TaskListAdapter;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.entities.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {

    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    private static final String FRAGMENT_NAME = "Tasks";

    TextView tvCurrentSelectedDate;
    TextView tvCurrentSelectedMonth;

    //ScrollView
    ScrollView scrollViewTaskFragment;

    //taskList Adapter
    TaskListAdapter taskListAdapter;
    RecyclerView rv;

    //Calander
    private HorizontalCalendar horizontalCalendar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_task, container, false);
        mContext = getContext();



        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(FRAGMENT_NAME);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                System.out.println("on back pressed ");
                if (callBackInterface!=null){
                    callBackInterface.popBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        initUI();
        initUICalendar();
        initUIRecycleViewerTasks();
        return view;
    }

    private void initUI() {
        //scrollViewTaskFragment = view.findViewById(R.id.scrollViewTaskFragment);
    }

    private void initUIRecycleViewerTasks() {
        rv = view.findViewById(R.id.rv);

        ArrayList tasks = new ArrayList<Task>();

        tasks.add(new Task("Task 1","description 1","#ff0000"));
        tasks.add(new Task("Task 2","description 2","#FF5959"));
        tasks.add(new Task("Task 3","description 3","#ffee00"));
        tasks.add(new Task("Task 4","description 4","#64e600"));
        tasks.add(new Task("Task 5","description 5","#a7a7a7"));
        tasks.add(new Task("Task 6","description 6","#FF8C00"));
        tasks.add(new Task("Task 7","description 7","#919191"));
        tasks.add(new Task("Task 8","description 8","#E8EBEC"));
        tasks.add(new Task("Task 1","description 1","#ff0000"));
        tasks.add(new Task("Task 2","description 2","#FF5959"));
        tasks.add(new Task("Task 3","description 3","#ffee00"));
        tasks.add(new Task("Task 4","description 4","#64e600"));
        tasks.add(new Task("Task 5","description 5","#a7a7a7"));
        tasks.add(new Task("Task 6","description 6","#FF8C00"));
        tasks.add(new Task("Task 7","description 7","#919191"));
        tasks.add(new Task("Task 8","description 8","#E8EBEC"));


        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        taskListAdapter = new TaskListAdapter(mContext,tasks);
        rv.setAdapter(taskListAdapter);

    }

    private void initUICalendar() {

        tvCurrentSelectedDate = view.findViewById(R.id.tvCurrentSelectedDate);
        tvCurrentSelectedMonth = view.findViewById(R.id.tvCurrentSelectedMonth);

        /* start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* end after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 12);

        tvCurrentSelectedMonth.setText(DateFormat.format("MMMM", Calendar.MONTH));

        horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EE")
                .textSize(11, 17, 11)
                .showTopText(true)
                .showBottomText(true)
                .selectorColor(Color.rgb(255,89,89)) //transparent to hide
                .textColor(Color.BLACK, Color.rgb(255,89,89))
                .end()
                .addEvents(new CalendarEventsPredicate() {

                    Random rnd = new Random();
                    @Override
                    public List<CalendarEvent> events(Calendar date) {
                        List<CalendarEvent> events = new ArrayList<>();
                        int count = rnd.nextInt(6);

                        for (int i = 0; i <= count; i++){
                            events.add(new CalendarEvent(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)), "event"));
                        }

                        return events;
                    }
                })
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                tvCurrentSelectedDate.setText(DateFormat.format("EEEE,  MMMM  d,  yyyy", date));
                tvCurrentSelectedMonth.setText(DateFormat.format("MMMM", date));
            }
        });

        //go to today btn
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalCalendar.goToday(false);
            }
        });*/
    }

    public void setCallBackInterface (CallBackInterface callBackInterface){
        this.callBackInterface = callBackInterface;
    }
}