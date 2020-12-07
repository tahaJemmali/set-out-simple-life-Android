package tn.esprit.setoutlife.Adapters;


import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.R;

public class CommentListAdapter extends RecyclerView.Adapter {

    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;

    private LayoutInflater inflater;
    private List<JSONObject> comments = new ArrayList<>();

    public CommentListAdapter (LayoutInflater inflater) {
        this.inflater = inflater;
    }

    private class SentCommentHolder extends RecyclerView.ViewHolder {
        TextView messageTxt,tvCommentTime,tvUserName;
        CircleImageView imgViewProfile;

        public SentCommentHolder(@NonNull View itemView) {
            super(itemView);

            messageTxt = itemView.findViewById(R.id.sentTxt);
            tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            imgViewProfile = itemView.findViewById(R.id.imgViewProfile);
        }
    }

    private class ReceivedCommentHolder extends RecyclerView.ViewHolder {

        TextView messageTxt;
        TextView tvCommentTime,tvUserName;
        CircleImageView imgViewProfile;

        public ReceivedCommentHolder(@NonNull View itemView) {
            super(itemView);
            messageTxt = itemView.findViewById(R.id.receivedTxt);
            tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            imgViewProfile = itemView.findViewById(R.id.imgViewProfile);
        }
    }

    @Override
    public int getItemViewType(int position) {

        JSONObject message = comments.get(position);

        try {
            if (message.getBoolean("isSent")) {

                if (message.has("message"))
                    return TYPE_MESSAGE_SENT;

            } else {

                if (message.has("message"))
                    return TYPE_MESSAGE_RECEIVED;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case TYPE_MESSAGE_SENT:
                view = inflater.inflate(R.layout.item_sent_message, parent, false);
                return new SentCommentHolder(view);
            case TYPE_MESSAGE_RECEIVED:

                view = inflater.inflate(R.layout.item_received_message, parent, false);
                return new ReceivedCommentHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        JSONObject message = comments.get(position);

        try {
            if (message.getBoolean("isSent")) {

                if (message.has("message")) {

                    SentCommentHolder messageHolder = (SentCommentHolder) holder;
                    messageHolder.messageTxt.setText(message.getString("message"));
                    messageHolder.tvUserName.setText(message.getString("firstName")+" "+message.getString("lastName"));

                    if (!message.getString("user_photo").equals("Not mentioned"))
                        Picasso.get().load(message.getString("user_photo")).into(messageHolder.imgViewProfile);
                    else
                        Picasso.get().load("https://graph.facebook.com/10214899562601635/picture?height=1024").into(messageHolder.imgViewProfile);

                    long diffInMillisec = message.getLong("comment_date");
                    if (message.getLong("comment_date")!=1000)
                        diffInMillisec = new Date().getTime() - message.getLong("comment_date") ;

                    long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillisec);
                    long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillisec);
                    long diffInMin = TimeUnit.MILLISECONDS.toMinutes(diffInMillisec);
                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec);

                    if (diffInDays>0){
                        messageHolder.tvCommentTime.setText(diffInDays+" d");
                    }else if (diffInHours>0 && diffInHours<23){
                        messageHolder.tvCommentTime.setText(diffInHours+" hrs");
                    }else if(diffInMin>0 && diffInMin<59){
                        messageHolder.tvCommentTime.setText(diffInMin+" mins");
                    }else if (diffInSec>0 && diffInSec<59){
                        messageHolder.tvCommentTime.setText(diffInMin+" seconds");
                    }

                    //messageHolder.tvCommentTime.setText(message.getString("comment_date"));


                } else {
                    /*SentCommentHolder imageHolder = (SentCommentHolder) holder;
                    Bitmap bitmap = getBitmapFromString(message.getString("image"));
                    imageHolder.imageView.setImageBitmap(bitmap);*/
                }

            } else {

                if (message.has("message")) {

                    ReceivedCommentHolder messageHolder = (ReceivedCommentHolder) holder;
                    //messageHolder.nameTxt.setText(message.getString("name"));
                    messageHolder.messageTxt.setText(message.getString("message"));
                    messageHolder.tvUserName.setText(message.getString("firstName")+" "+message.getString("lastName"));

                    //System.out.println(message.getString("firstName"));
                    //System.out.println(message.getString("lastName"));
                    //System.out.println(message.getString("message"));
                    //System.out.println(message.getString("message"));

                    if (!message.getString("user_photo").equals("Not mentioned"))
                        Picasso.get().load(message.getString("user_photo")).into(messageHolder.imgViewProfile);
                    else
                        Picasso.get().load("https://graph.facebook.com/10214899562601635/picture?height=1024").into(messageHolder.imgViewProfile);

                    long diffInMillisec = 1000;
                    if (message.getLong("comment_date")!=1000)
                        diffInMillisec = new Date().getTime() - message.getLong("comment_date") ;

                    long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillisec);
                    long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillisec);
                    long diffInMin = TimeUnit.MILLISECONDS.toMinutes(diffInMillisec);
                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec);

                    if (diffInDays>0){
                        messageHolder.tvCommentTime.setText(diffInDays+" d");
                    }else if (diffInHours>0 && diffInHours<23){
                        messageHolder.tvCommentTime.setText(diffInHours+" hrs");
                    }else if(diffInMin>0 && diffInMin<59){
                        messageHolder.tvCommentTime.setText(diffInMin+" mins");
                    }else if (diffInSec>0 && diffInSec<59){
                        messageHolder.tvCommentTime.setText(diffInMin+" seconds");
                    }

                    //messageHolder.tvCommentTime.setText(message.getString("comment_date"));



                } else {
                    /*ReceivedImageHolder imageHolder = (ReceivedImageHolder) holder;
                    imageHolder.nameTxt.setText(message.getString("name"));
                    Bitmap bitmap = getBitmapFromString(message.getString("image"));
                    imageHolder.imageView.setImageBitmap(bitmap);*/
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void addItem (JSONObject jsonObject) {
        comments.add(jsonObject);
        notifyDataSetChanged();
    }
}
