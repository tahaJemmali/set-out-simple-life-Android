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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import tn.esprit.setoutlife.Adapters.ProjectListAdapter;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.ProjectRepository;
import tn.esprit.setoutlife.Repository.TagRepository;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.entities.Project;
import tn.esprit.setoutlife.entities.Tag;
import tn.esprit.setoutlife.entities.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {

    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    private static final String FRAGMENT_NAME = "Projects & Tasks";
    FragmentManager fragmentManager;
    TextView tvCurrentSelectedDate;
    TextView tvCurrentSelectedMonth;

    //ScrollView
    ScrollView scrollViewTaskFragment;

    //taskList Adapter
    ProjectListAdapter projectListAdapter;
    RecyclerView rv;
    //Variable
    public static ArrayList<Project> global;

    //views
    ImageButton addTask;
    ImageButton showProject;
    ImageButton addTag;

    //Calander
    private HorizontalCalendar horizontalCalendar;


    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task, container, false);
        mContext = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();

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
        initUICalendar();
        initUIRecycleViewerTasks();
        addTask();
        addTag();
        showProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProject();
            }
        });
        return view;
    }

    void addTag() {
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddTagFragment tagFrag = new AddTagFragment();
                fragmentManager
                        .beginTransaction().add(R.id.fragment_container, tagFrag, "add Tag")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    void addProject(){
        AddProjectFragment tagFrag = new AddProjectFragment();
        fragmentManager
                .beginTransaction().add(R.id.fragment_container, tagFrag, "add Project")
                .addToBackStack(null)
                .commit();
    }

    void addTask() {
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskFragment taskFrag = new AddTaskFragment();
                fragmentManager
                        .beginTransaction().add(R.id.fragment_container, taskFrag, "add Task")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void initUI() {
        addTask = view.findViewById(R.id.addTask);
        showProject = view.findViewById(R.id.showProject);
        addTag = view.findViewById(R.id.addTag);
    }

    private void initUIRecycleViewerTasks() {
        rv = view.findViewById(R.id.rv);

        ArrayList projects = new ArrayList<Project>();

        for (Project row:global){
            projects.add(row);
        }

        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        projectListAdapter = new ProjectListAdapter(mContext, projects);
        rv.setAdapter(projectListAdapter);
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
                .selectorColor(Color.rgb(255, 89, 89)) //transparent to hide
                .textColor(Color.BLACK, Color.rgb(255, 89, 89))
                .end()
                .addEvents(new CalendarEventsPredicate() {

                    Random rnd = new Random();

                    @Override
                    public List<CalendarEvent> events(Calendar date) {
                        List<CalendarEvent> events = new ArrayList<>();
                        int count = rnd.nextInt(6);

                        for (int i = 0; i <= count; i++) {
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

    }

    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }
}