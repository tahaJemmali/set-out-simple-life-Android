package tn.esprit.setoutlife.entities;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Parcelable {

    User user;
    String text;
    Date comment_date;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getComment_date() {
        return comment_date;
    }

    public void setComment_date(Date comment_date) {
        this.comment_date = comment_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(text);
        parcel.writeParcelable(user , i);
        parcel.writeSerializable(comment_date);

    }


    public static final Creator<Comment> CREATOR = new Creator<Comment>(){
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment();
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

}
