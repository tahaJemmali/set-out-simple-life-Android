package tn.esprit.setoutlife.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tn.esprit.setoutlife.R;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    List<String> titles;
    List<Integer> images;

    Context context;
    LayoutInflater layoutInflater;


    public GridAdapter(Context ctx, List<String> titles, List<Integer> images){
        this.images=images;
        this.titles=titles;
        this.layoutInflater = LayoutInflater.from(ctx);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_grid_layout,parent,false);

        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv.setText(titles.get(position));
        holder.imgBtn.setImageResource(images.get(position));


    }




    @Override
    public int getItemCount() {
        return titles.size();
    }



    public interface OnItemClickListener{
        void onItemClick(int position, String name);
    }

    private OnItemClickListener mListener;

    public void setItemCLickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView imgBtn;

        public ViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            imgBtn = itemView.findViewById(R.id.imgBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position,tv.getText().toString());
                        }
                    }
                }
            });

           /* itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        System.out.println(position+" is beign long pressed !");
                    }
                    return true;
                }
            });
               */
            /*
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.postDelayed(run, 5000);
                            break;

                        default:
                            handler.removeCallbacks(run);
                            break;

                    }
                    return true;
                }
            });*/
        }

    }
}
