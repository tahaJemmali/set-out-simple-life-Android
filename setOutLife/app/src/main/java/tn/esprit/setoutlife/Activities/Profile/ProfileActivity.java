package tn.esprit.setoutlife.Activities.Profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Date;

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Adapters.ProfileListAdapter;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Utils.CallBackInterface;

public class ProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    protected HeaderView toolbarHeaderView;

    protected HeaderView floatHeaderView;

    protected AppBarLayout appBarLayout;

    protected Toolbar toolbar;

    //taskList Adapter
    ProfileListAdapter profileListAdapter;
    RecyclerView rvProfile;

    ImageView profileImage;

    private boolean isHideToolbarView = false;

    ArrayList<Integer> icons;
    ArrayList<String> titles;
    ArrayList<String> descriptions;

    CallBackInterface callBackInterface;

    public void setCallBackInterface (CallBackInterface callBackInterface){
        this.callBackInterface = callBackInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setCallBackInterface(HomeActivity.myContext);

        toolbar = findViewById(R.id.toolbar);
        toolbarHeaderView = findViewById(R.id.toolbar_header_view);
        floatHeaderView = findViewById(R.id.float_header_view);
        appBarLayout = findViewById(R.id.appbar);
        rvProfile = findViewById(R.id.rvProfile);

        profileImage = findViewById(R.id.profileImage);




        /*if (!HomeActivity.getCurrentLoggedInUser().getPhoto().equals("Not mentioned"))
        Picasso.get().load(HomeActivity.getCurrentLoggedInUser().getPhoto()).into(profileImage);
        else
        Picasso.get().load("https://graph.facebook.com/10214899562601635/picture?height=1024").into(profileImage);
        */




        if (HomeActivity.getCurrentLoggedInUser().getPhoto().startsWith("/")) {
            Bitmap bitmap = getBitmapFromString(HomeActivity.getCurrentLoggedInUser().getPhoto());
            profileImage.setImageBitmap(bitmap);
        }
        else if (!HomeActivity.getCurrentLoggedInUser().getPhoto().equals("Not mentioned")){
            Picasso.get().load(HomeActivity.getCurrentLoggedInUser().getPhoto()).into(profileImage);
        }
        else {
            Picasso.get().load("https://graph.facebook.com/10214899562601635/picture?height=1024").into(profileImage);
        }




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            w.setStatusBarColor(Color.BLACK);
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //toolbar.setSubtitleTextColor(Color.BLACK);
        //toolbar.setTitleTextColor(Color.BLACK);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(HomeActivity.getCurrentLoggedInUser().getFirstName()+"'s profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUi();
    }


    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callBackInterface.popBackb();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void initUi() {
        setArrays();

        rvProfile.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        profileListAdapter = new ProfileListAdapter(this,icons,titles,descriptions);
        rvProfile.setAdapter(profileListAdapter);

        //appBarLayout.addOnOffsetChangedListener(this);

        //toolbarHeaderView.bindTo("Larry Page", "Last seen today at 7.00PM");
        //floatHeaderView.bindTo("Larry Page", "Last seen today at 7.00PM");
    }

    private void setArrays() {

        icons = new ArrayList<>();
        titles = new ArrayList<>();
        descriptions = new ArrayList<>();

        icons.add(R.drawable.ic_default_user);
        titles.add("Name");
        descriptions.add(HomeActivity.getCurrentLoggedInUser().getFirstName()+" "+HomeActivity.getCurrentLoggedInUser().getLastName());

        icons.add(R.mipmap.ic_email);
        titles.add("Email");
        descriptions.add(HomeActivity.getCurrentLoggedInUser().getEmail());


        if (!HomeActivity.getCurrentLoggedInUser().getAddress().equals("Not mentioned")){
            icons.add(R.mipmap.ic_address);
            titles.add("Home Town");
            descriptions.add(HomeActivity.getCurrentLoggedInUser().getAddress());
        }

        if (!HomeActivity.getCurrentLoggedInUser().getPhone().equals("Not mentioned")){
            icons.add(R.mipmap.ic_phone);
            titles.add("Mobile");
            descriptions.add(HomeActivity.getCurrentLoggedInUser().getPhone());
        }

        if (HomeActivity.getCurrentLoggedInUser().getBirth_date()!=null){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = HomeActivity.getCurrentLoggedInUser().getBirth_date();
                String dateTime = dateFormat.format(date);

                icons.add(R.drawable.ic_birth_date);
                titles.add("Date of Birth");
                descriptions.add(dateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(HomeActivity.getCurrentLoggedInUser().getSign_up_date());
                SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                String month_name = month_date.format(calendar.getTime());
                String dateTime2 = month_name+" "+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.YEAR);
                icons.add(R.drawable.ic_join);
                titles.add("Member since");
                descriptions.add(dateTime2);
            } catch (ParseException e) {
                e.printStackTrace();
            }


    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }
}
