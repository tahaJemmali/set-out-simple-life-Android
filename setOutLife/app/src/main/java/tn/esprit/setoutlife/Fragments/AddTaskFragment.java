package tn.esprit.setoutlife.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.TaskRepository;
import tn.esprit.setoutlife.Utils.CallBackInterface;

import tn.esprit.setoutlife.entities.Tag;
import tn.esprit.setoutlife.entities.Task;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTaskFragment#} factory method to
 * create an instance of this fragment.
 */
public class AddTaskFragment extends Fragment {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    Tag tag;
    FragmentManager fragmentManager;
    public static ArrayList<Tag> global;
    //UI
    Spinner tagsPicker;
    View tagColor;
    ImageButton close;
    ImageButton addTaskBtn;
    EditText taskNameEditText;
    DatePicker deadline;
    NumberPicker importance;
    DatePicker reminder;
    NumberPicker enjoyment;
    EditText noteEditText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_task, container, false);
        mContext = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                fragmentManager.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        initUI();
        tagAnimation();
        close();
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {

                        Task task = new Task("",
                                taskNameEditText.getText().toString(),
                                new Date(),
                                getDateFromDatePicker(deadline),
                                getDateFromDatePicker(reminder),
                                importance.getValue(),
                                enjoyment.getValue(),
                                noteEditText.getText().toString(),
                                false);
                    Toast.makeText(mContext, "Task added successfully !", Toast.LENGTH_LONG).show();
                    fragmentManager.popBackStack();
                        try {
                            TaskRepository.addTask(mContext, task);
                        } catch (UnsupportedEncodingException e) {
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

        return view;
    }

    void close() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });
    }


    public void initUI() {
        taskNameEditText = view.findViewById(R.id.taskNameEditText);
        deadline = view.findViewById(R.id.deadline);
        importance = view.findViewById(R.id.importance);
        reminder = view.findViewById(R.id.reminder);
        enjoyment = view.findViewById(R.id.enjoyment);
        noteEditText = view.findViewById(R.id.noteEditText);
        tagColor = view.findViewById(R.id.tagColor);
        tagsPicker = view.findViewById(R.id.tagsPicker);
        close = view.findViewById(R.id.closeTask);
        addTaskBtn = view.findViewById(R.id.addTaskBtn);
        enjoyment.setMaxValue(5);
        enjoyment.setMinValue(0);
        enjoyment.setValue(0);
        importance.setMaxValue(5);
        importance.setMinValue(0);
        importance.setValue(0);
        setAdapter();
    }

    boolean validation(){
        if (taskNameEditText.getText().toString().isEmpty())
            return false;
        return true;
    }
    public void setAdapter() {
        ArrayList<String> tagsName = new ArrayList<String>();

        for (Tag row : global) {
            tagsName.add(row.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                mContext, android.R.layout.simple_spinner_item, tagsName);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagsPicker.setAdapter(adapter);
    }

    public void tagAnimation() {
        tagsPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = tagsPicker.getSelectedItemPosition();
                tag = global.get(position);
                tagColor.getBackground().mutate().setColorFilter(Color.parseColor(tag.getColor()), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }
}