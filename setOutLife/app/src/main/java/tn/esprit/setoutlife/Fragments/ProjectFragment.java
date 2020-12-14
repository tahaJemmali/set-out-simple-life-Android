package tn.esprit.setoutlife.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tn.esprit.setoutlife.Adapters.ProjectTaskListAdapter;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.ProjectRepository;
import tn.esprit.setoutlife.entities.Project;
import tn.esprit.setoutlife.entities.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectFragment#} factory method to
 * create an instance of this fragment.
 */
public class ProjectFragment extends Fragment {

    View view;
    Context mContext;
    TextView projectName;
    TextView descriptionPrject;
    View tagColor;
    String taskName;
    String description;
    String color;
    String id;
    //taskList Adapter
    ProjectTaskListAdapter projectTaskListAdapter;
    RecyclerView rv;
    FragmentManager fragmentManager;

    //views
    ImageButton addTask;
    ImageButton close;
    ImageButton deleteProject;
    //Var
    ArrayList<Task> tasks;

    public  ProjectFragment(){
    }
    public  ProjectFragment(Project project){
        tasks = new ArrayList<Task>();
        this.taskName=project.getName();
        this.description=project.getDescription();
        this.color=project.getTag().getColor();
        this.tasks=project.getTasks();
        this.id=project.getId();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_project, container, false);
        mContext = getContext();
        fragmentManager=getActivity().getSupportFragmentManager();
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                fragmentManager.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        intUI();
        intRecyclerView();
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskFragment taskFrag= new AddTaskFragment();
                fragmentManager
                        .beginTransaction().add(R.id.fragment_container, taskFrag, "add Task")
                        .addToBackStack(null)
                        .commit();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });


        deleteProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this Project ?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                    final ProgressDialog dialogg = ProgressDialog.show(mContext
                                            , "","Loading..Wait.." , true);
                                    dialogg.show();
                                ProjectRepository.deleteProject(mContext,id,dialogg,fragmentManager);
                                Toast.makeText(mContext, "Project Deleted Successfully!", Toast.LENGTH_LONG).show();

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        if (!taskName.isEmpty())
        {
            projectName.setText(taskName);
            descriptionPrject.setText(description);
            tagColor.getBackground().mutate().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
        }

        return view;
    }

    void intUI(){
        addTask=view.findViewById(R.id.addTaskInProject);
        deleteProject=view.findViewById(R.id.deleteProject);
        close=view.findViewById(R.id.closeProject);
        projectName =view.findViewById(R.id.projectName);
        descriptionPrject=view.findViewById(R.id.descriptionProjet);
        tagColor=view.findViewById(R.id.tagColorProject);
        rv = view.findViewById(R.id.rvInProject);

    }
    void intRecyclerView(){

        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        projectTaskListAdapter = new ProjectTaskListAdapter(mContext,tasks);
        rv.setAdapter(projectTaskListAdapter);
    }
}