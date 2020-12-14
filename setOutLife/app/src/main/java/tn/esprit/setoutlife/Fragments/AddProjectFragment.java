package tn.esprit.setoutlife.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.sayantan.advancedspinner.MultiSpinner;

import java.util.ArrayList;
import java.util.Date;

import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.ProjectRepository;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.entities.Project;
import tn.esprit.setoutlife.entities.Tag;
import tn.esprit.setoutlife.entities.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProjectFragment#} factory method to
 * create an instance of this fragment.
 */
public class AddProjectFragment extends Fragment {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    private static final String FRAGMENT_NAME = "Add project";
    FragmentManager fragmentManager;
    //UI
    ImageButton closeAddProject;
    EditText projectNameEditText;
    EditText description;
    ImageButton addProjectBtn;
    Spinner tagsPicker;
    View tagColor;
    MultiSpinner taskSpinner;
    //Var
    public static ArrayList<Tag> global;
    public static ArrayList<Task> globalTasks;
    Tag tag;
    ArrayList<String> tasksString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_add_project, container, false);
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
        closeAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });
        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()){
                    ArrayList<Task> tasks=new ArrayList<Task>();
                    int j=0;
                    for(String row:taskSpinner.getSelectedItems()){
                        if(tasksString.contains(row))
                            tasks.add(globalTasks.get(j));
                        j++;
                    }
                    Project p=new Project("dddd",projectNameEditText.getText().toString(),
                            description.getText().toString(),tasks,new Date(),global.get(tagsPicker.getSelectedItemPosition()));
                    final ProgressDialog dialogg = ProgressDialog.show(mContext
                            , "","Loading..Wait.." , true);
                    dialogg.show();
                    ProjectRepository.addProject(mContext,p,dialogg, fragmentManager);
                    Toast.makeText(mContext,"Project added successfully !", Toast.LENGTH_LONG).show();
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

    private void initUI() {
        closeAddProject=view.findViewById(R.id.closeAddProject);
        projectNameEditText=view.findViewById(R.id.projectNameEditText);
        description=view.findViewById(R.id.description);
        addProjectBtn=view.findViewById(R.id.addProjectBtn);
        tagColor = view.findViewById(R.id.tagColor);
        tagsPicker = view.findViewById(R.id.tagsPicker);
        taskSpinner = view.findViewById(R.id.taskSpinner);
        initSpinner();
        setAdapter();
        tagAnimation();
    }

    private void initSpinner() {
        tasksString=new ArrayList<String>();
        for(Task row:globalTasks){
            tasksString.add(row.getTaskName());
        }
        taskSpinner.setSpinnerList(tasksString);
    }

    private boolean validation(){
        if (projectNameEditText.getText().toString().isEmpty())
            return false;
        if (description.getText().toString().isEmpty())
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
}