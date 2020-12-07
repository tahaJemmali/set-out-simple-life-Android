package tn.esprit.setoutlife.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.entities.Task;

public class ProjectTaskListAdapter extends RecyclerView.Adapter<ProjectTaskListAdapter.ProjectTaskHolder>{
    private Context mContext;
    private ArrayList<Task> tasks;

    public ProjectTaskListAdapter(Context mContext,ArrayList<Task> tasks){
        this.mContext = mContext;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ProjectTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.project_task_single,parent,false);
        return new ProjectTaskListAdapter.ProjectTaskHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectTaskHolder holder, int position) {
        Task task = tasks.get(position);

        holder.taskName.setText(task.getTaskName());
        holder.deadlineTask.setText(task.getNote());
        holder.tagColorInTask.getBackground().mutate().setColorFilter(Color.parseColor("#f08676"), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ProjectTaskHolder extends RecyclerView.ViewHolder {

        TextView taskId;
        TextView deadlineTask;
        TextView taskNote;
        TextView taskName;
        View tagColorInTask;
        public ProjectTaskHolder(@NonNull View itemView) {
            super(itemView);
             taskId=itemView.findViewById(R.id.taskId);
             deadlineTask=itemView.findViewById(R.id.deadlineTask);
             taskNote=itemView.findViewById(R.id.taskName);
            taskName=itemView.findViewById(R.id.tagNameTask);
             tagColorInTask=itemView.findViewById(R.id.tagColorInTask);
        }
    }
    public void notifyChange(ArrayList<Task> tasks){
        this.tasks = tasks;
        this.notifyDataSetChanged();
    }
}
