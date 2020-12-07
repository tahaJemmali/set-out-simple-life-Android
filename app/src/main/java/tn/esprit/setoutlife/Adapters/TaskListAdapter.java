package tn.esprit.setoutlife.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.entities.Task;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskHolder>{

    private Context mContext;
    private ArrayList<Task> tasks;

    public TaskListAdapter(Context mContext,ArrayList<Task> tasks){
        this.mContext = mContext;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskListAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.custom_task_single,parent,false);
        return new TaskListAdapter.TaskHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.TaskHolder holder, int position) {
        Task task = tasks.get(position);



        holder.tvTaskName.setText(task.getName());
        holder.tvTaskDescription.setText(task.getDescription());

        float mRadius=50f;
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(Color.parseColor(task.getColor()));
        shape.setCornerRadii(new float[]{mRadius, mRadius, 0, 0, 0, 0, mRadius, mRadius});

        holder.tagTaskColor.setBackground(shape);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class TaskHolder extends RecyclerView.ViewHolder {

        TextView tvTaskName,tvTaskDescription;
        LinearLayout tagTaskColor;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            tvTaskDescription = itemView.findViewById(R.id.tvTaskDescription);
            tagTaskColor = itemView.findViewById(R.id.tagTaskColor);
        }
    }

    public void notifyChange(ArrayList<Task> tasks){
        this.tasks = tasks;
        this.notifyDataSetChanged();
    }

}
