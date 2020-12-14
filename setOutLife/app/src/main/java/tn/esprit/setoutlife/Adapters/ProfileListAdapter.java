package tn.esprit.setoutlife.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tn.esprit.setoutlife.R;


public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileElementHolder> {

    private Context mContext;
    private ArrayList<Integer> icons;
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;

    public ProfileListAdapter(Context mContext,ArrayList<Integer> icons,ArrayList<String> titles,ArrayList<String> descriptions ){
        this.mContext = mContext;
        this.icons = icons;
        this.titles = titles;
        this.descriptions = descriptions;
    }

    @NonNull
    @Override
    public ProfileListAdapter.ProfileElementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.custom_single_row_profile,parent,false);
        return new ProfileListAdapter.ProfileElementHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListAdapter.ProfileElementHolder holder, int position) {

        String title = titles.get(position);
        Integer icon = icons.get(position);
        String description = descriptions.get(position);

        holder.titleTextView.setText(title);
        holder.iconName.setImageResource(icon);
        //holder.iconName.setImageResource(R.drawable.ic_default_user);
        holder.descriptionTextView.setText(description);


        /*float mRadius=50f;
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(Color.parseColor(task.getColor()));
        shape.setCornerRadii(new float[]{mRadius, mRadius, 0, 0, 0, 0, mRadius, mRadius});

        holder.tagTaskColor.setBackground(shape);*/
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    public class ProfileElementHolder extends RecyclerView.ViewHolder {

        ImageView iconName;
        TextView titleTextView,descriptionTextView;


        public ProfileElementHolder(@NonNull View itemView) {
            super(itemView);

            iconName = itemView.findViewById(R.id.iconName);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);

        }
    }

    /*public void notifyChange(ArrayList<Task> tasks){
        this.tasks = tasks;
        this.notifyDataSetChanged();
    }*/

}
