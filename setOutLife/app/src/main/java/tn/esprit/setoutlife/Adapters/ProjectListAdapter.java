package tn.esprit.setoutlife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Fragments.ProjectFragment;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.entities.Project;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectHolder>{

    private Context mContext;
    private ArrayList<Project> Projects;
     Project Project;

    public ProjectListAdapter(Context mContext,ArrayList<Project> Projects){
        this.mContext = mContext;
        this.Projects = Projects;
    }

    @NonNull
    @Override
    public ProjectListAdapter.ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.custom_task_single,parent,false);
        return new ProjectListAdapter.ProjectHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectListAdapter.ProjectHolder holder, int position) {
        Project = Projects.get(position);
        holder.tvProjectName.setText(Project.getName());
        holder.tvProjectDescription.setText(Project.getDescription());
       holder.tagProjectColor.getBackground().mutate().setColorFilter(Color.parseColor(Project.getTag().getColor()), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getItemCount() {
        return Projects.size();
    }


    public class ProjectHolder extends RecyclerView.ViewHolder {

        TextView tvProjectName,tvProjectDescription;
        LinearLayout tagProjectColor;

        public ProjectHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Project=Projects.get(getAdapterPosition());
                    HomeActivity activity = (HomeActivity) view.getContext();
                    ProjectFragment myFragment = new ProjectFragment(Project);
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_container, myFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
            tvProjectName = itemView.findViewById(R.id.tvTaskName);
            tvProjectDescription = itemView.findViewById(R.id.tvTaskDescription);
            tagProjectColor = itemView.findViewById(R.id.tagTaskColor);
        }
    }

    public void notifyChange(ArrayList<Project> Projects){
        this.Projects = Projects;
        this.notifyDataSetChanged();
    }

}
