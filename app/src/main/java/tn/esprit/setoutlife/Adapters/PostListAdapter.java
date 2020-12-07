package tn.esprit.setoutlife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import tn.esprit.setoutlife.Activities.Forum.CommentsActivity;
import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.entities.Post;
import tn.esprit.setoutlife.entities.User;


public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostHolder> {

    private Context mContext;
    private ArrayList<Post> posts;


    public PostListAdapter(Context mContext, ArrayList<Post> posts ){
        this.mContext = mContext;
        this.posts = posts;

    }

    @NonNull
    @Override
    public PostListAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.custom_post,parent,false);
        return new PostListAdapter.PostHolder(rootView,likeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.PostHolder holder, int position) {

        Post post = posts.get(position);

        long diffInMillisec = new Date().getTime() - post.getPostDate().getTime() ;

        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillisec);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillisec);
        long diffInMin = TimeUnit.MILLISECONDS.toMinutes(diffInMillisec);
        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec);

        if (diffInDays>0){
            holder.tvPostTime.setText(diffInDays+" d");
        }else if (diffInHours>0 && diffInHours<23){
            holder.tvPostTime.setText(diffInHours+" hrs");
        }else if(diffInMin>0 && diffInMin<59){
            holder.tvPostTime.setText(diffInMin+" mins");
        }else if (diffInSec>0 && diffInSec<59){
            holder.tvPostTime.setText(diffInMin+" seconds");
        }

        //likes number
        holder.tv_likes.setText(String.valueOf(post.getLikedBy().size()));
        //comments nubmer
        holder.tv_comment.setText(String.valueOf(post.getComments().size())+" comments");



        if (!post.getImage().equals("noImage")){
            Bitmap bitmap = getBitmapFromString(post.getImage());
            holder.imgView_postPic.setImageBitmap(bitmap);
            holder.imgView_postPic.setVisibility(View.VISIBLE);
        }

        for (User user:posts.get(position).getLikedBy()) {
            if (user.getEmail().equals(HomeActivity.getCurrentLoggedInUser().getEmail())){
                holder.likeBtn.setLiked(true);
                holder.likeBtnText.setText("Liked");
                holder.likeBtnText.setTextColor(Color.parseColor("#008ad3"));
            }
        }

        holder.tvPostTitle.setText(post.getTitle());
        holder.tvPostDescription.setText(post.getDescription());
        holder.tvUserName.setText(post.getUser().getFirstName()+" "+post.getUser().getLastName());

        if (!post.getUser().getPhoto().equals("Not mentioned"))
            Picasso.get().load(post.getUser().getPhoto()).into(holder.imgViewProfile);
        else
            Picasso.get().load("https://graph.facebook.com/10214899562601635/picture?height=1024").into(holder.imgViewProfile);

    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public interface OnLikeClickListener{
        void onLikeClick(int position);
        void onUnLikeClick(int position);
        void onCommentClick(int position);
    }

    private OnLikeClickListener likeListener;

    public void setLikeListener(OnLikeClickListener listener){
        likeListener = listener;
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        TextView tvPostTime,tvPostTitle,tvPostDescription,tvUserName,tv_likes;
        CircleImageView imgViewProfile;
        ImageView imgView_postPic;
        //like
        TextView likeBtnText;
        LikeButton likeBtn;

        //comment
        LinearLayout commentBtn;
        TextView tv_comment;

        public PostHolder(@NonNull View itemView, final OnLikeClickListener listener) {
            super(itemView);

            tvPostTime = itemView.findViewById(R.id.tvPostTime);
            tvPostTitle = itemView.findViewById(R.id.tvPostTitle);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            imgViewProfile = itemView.findViewById(R.id.imgViewProfile);
            imgView_postPic = itemView.findViewById(R.id.imgView_postPic);

            likeBtnText = itemView.findViewById(R.id.likeBtnText);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            tv_likes = itemView.findViewById(R.id.tv_likes);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            tv_comment = itemView.findViewById(R.id.tv_comment);

            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onCommentClick(position);
                        }
                    }
                }
            });

            likeBtn.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    //+1 text view likes

                    int likes = Integer.parseInt(tv_likes.getText().toString());
                    likes++;
                    tv_likes.setText(String.valueOf(likes));

                    likeBtnText.setText("Liked");
                    likeBtnText.setTextColor(Color.parseColor("#008ad3"));

                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onLikeClick(position);
                        }
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {

                    //-1 text view likes
                    int likes = Integer.parseInt(tv_likes.getText().toString());
                    likes--;
                    tv_likes.setText(String.valueOf(likes));

                    likeBtnText.setText("Like");
                    likeBtnText.setTextColor(Color.BLACK);

                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onUnLikeClick(position);
                        }
                    }

                }
            });
/*
            //dont touch
            likeBtnText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (likeBtnText.getText().toString().equals("Like")){
                        likeBtnText.setText("Liked");
                        likeBtnText.setTextColor(Color.parseColor("#008ad3"));
                        likeBtn.performClick();
                    }
                    else if (likeBtnText.getText().toString().equals("Liked")){
                        likeBtnText.setText("Like");
                        likeBtnText.setTextColor(Color.BLACK);
                        //likeBtn.setLiked(false);
                        likeBtn.performClick();
                    }
                }
            });
*/
        }
    }


}
