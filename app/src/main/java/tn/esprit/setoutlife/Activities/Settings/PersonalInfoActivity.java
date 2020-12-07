package tn.esprit.setoutlife.Activities.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hotmail.or_dvir.easysettings.events.BasicSettingsClickEvent;
import com.hotmail.or_dvir.easysettings.pojos.BasicSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.EasySettings;
import com.hotmail.or_dvir.easysettings.pojos.HeaderSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.SettingsObject;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Activities.LoginActivity;
import tn.esprit.setoutlife.Activities.Settings.CustomSettings.CallBackSettingsInterface;
import tn.esprit.setoutlife.Activities.Settings.CustomSettings.CustomSettingsObject;
import tn.esprit.setoutlife.Activities.Settings.CustomSettings.CustomSettingsObjectEditText;
import tn.esprit.setoutlife.Activities.Settings.CustomSettings.CustomSettingsObjectStatic;
import tn.esprit.setoutlife.Activities.Settings.PersonalInfo.EmailActivity;
import tn.esprit.setoutlife.Activities.Settings.PersonalInfo.NameActivity;
import tn.esprit.setoutlife.Activities.SettingsActivity;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.entities.User;

public class PersonalInfoActivity extends AppCompatActivity implements CallBackSettingsInterface {

    androidx.appcompat.widget.Toolbar toolbar;

    private ArrayList<SettingsObject> mySettingsList;
    private Toast mToast;

    public static final String SETTINGS_KEY_EMAIL = "SETTINGS_KEY_EMAIL";
    public static final String SETTINGS_KEY_NAME = "SETTINGS_KEY_NAME";
    public static final String SETTINGS_KEY_BIRTH = "SETTINGS_KEY_BIRTH";
    public static final String SETTINGS_KEY_REGION = "SETTINGS_KEY_EMAIL";
    public static final String SETTINGS_KEY_HOMETOWN = "SETTINGS_KEY_HOMETOWN";
    public static final String SETTINGS_KEY_PHONE = "SETTINGS_KEY_PHONE";

    public static final String SETTINGS_KEY_CONNECTED_WITH = "SETTINGS_KEY_CONNECTED_WITH";
    public static final String SETTINGS_KEY_SIGN_UP_DATE = "SETTINGS_KEY_CONNECTED_WITH";



    private static final int REQUEST_CODE_UPDATE_EMAIL = 1;
    private static final int REQUEST_CODE_UPDATE_NAME = 2;
    private static final int REQUEST_CODE_RETURN = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // call back inteface
        CustomSettingsObject.callBackSettingsInterface = this;


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Personal Informations");

        System.out.println("test 1 :"+CustomSettingsObject.callBackSettingsInterface);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        createSettings();
        commitSettings();
    }

    private void commitSettings() {
        LinearLayout container = findViewById(R.id.PersonalInfosettingsContainer);
        EasySettings.inflateSettingsLayout(this, container, mySettingsList);
    }


    private void updateSettings() {
        LinearLayout container = findViewById(R.id.PersonalInfosettingsContainer);
        container.removeAllViews();

       // toolbar = findViewById(R.id.toolbar);
        /*setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Personal Informations");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        EasySettings.inflateSettingsLayout(this, container, mySettingsList);
    }

    @Override
    protected void onResume() {
        super.onResume();

        CustomSettingsObjectEditText.emailText="";
        CustomSettingsObjectEditText.passwordText="";

        //CurrentLoggedInUser = HomeActivity.getCurrentLoggedInUser();
        //System.out.println("on resume"+CurrentLoggedInUser.getEmail());
    }

    private void createSettings() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateTime ="";
        String dateTime2 ="";

        if (HomeActivity.getCurrentLoggedInUser().getBirth_date()==null){
            dateTime="Not mentioned";
        }else{
            try {
                Date date = HomeActivity.getCurrentLoggedInUser().getBirth_date();
                dateTime = dateFormat.format(date);
              //  System.out.println("Current Date Time : " + dateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        CustomSettingsObjectStatic customSettingsObjectConnectedWith = new CustomSettingsObjectStatic.Builder(SETTINGS_KEY_CONNECTED_WITH, "Connected with")
                            .setSummary(HomeActivity.getCurrentLoggedInUser().getSigned_up_with()).build();

        /*SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM dd yyyy");
        try {
            Date date = HomeActivity.getCurrentLoggedInUser().getSign_up_date();
            dateTime2 = dateFormat2.format(date);
           // System.out.println("Current Date Time : " + dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        System.out.println(HomeActivity.getCurrentLoggedInUser().getSign_up_date());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(HomeActivity.getCurrentLoggedInUser().getSign_up_date());
        System.out.println(calendar.get(Calendar.MONTH)+1);

        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());

        System.out.println(month_name);
        System.out.println((calendar.get(Calendar.YEAR)));
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));

        dateTime2 = month_name+" "+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.YEAR);

        CustomSettingsObjectStatic customSettingsObjectSignUpDate = new CustomSettingsObjectStatic.Builder(SETTINGS_KEY_SIGN_UP_DATE, "Member since")
                .setSummary(dateTime2).build();

        mySettingsList = EasySettings.createSettingsArray(

                new HeaderSettingsObject.Builder("ID")
                        .build(),
                    new CustomSettingsObject.Builder(SETTINGS_KEY_EMAIL, "Email")
                            .setSummary(HomeActivity.getCurrentLoggedInUser().getEmail())
                            .build(),
                customSettingsObjectConnectedWith,
                new HeaderSettingsObject.Builder("")
                        .build(),
                new HeaderSettingsObject.Builder("Profile")
                        .build(),
                    new CustomSettingsObject.Builder(SETTINGS_KEY_NAME, "Name")
                            .setSummary(HomeActivity.getCurrentLoggedInUser().getFirstName()+" "+HomeActivity.getCurrentLoggedInUser().getLastName())
                            .build(),
                    new CustomSettingsObject.Builder(SETTINGS_KEY_BIRTH, "Date of Birth")
                            .setSummary(dateTime)
                            .build(),
                new CustomSettingsObject.Builder(SETTINGS_KEY_HOMETOWN, "Home Town")
                        .setSummary(HomeActivity.getCurrentLoggedInUser().getAddress())
                        .build(),
                new CustomSettingsObject.Builder(SETTINGS_KEY_PHONE, "Phone number")
                        .setSummary(HomeActivity.getCurrentLoggedInUser().getPhone())
                        .build(),
                    new CustomSettingsObjectStatic.Builder(SETTINGS_KEY_REGION, "Country or region")
                            .setSummary("Tunisia")
                            .build(),
                customSettingsObjectSignUpDate
        );

    }

    /*@Subscribe
    public void onBasicSettingsClicked(BasicSettingsClickEvent event) {

    }*/

    @Override
    public void showIntent(String key) {
        Intent intent;
        switch (key){
            case SETTINGS_KEY_EMAIL:
                 intent = new Intent(PersonalInfoActivity.this,EmailActivity.class);
                startActivityForResult(intent,REQUEST_CODE_UPDATE_EMAIL);
                break;
            case SETTINGS_KEY_NAME:
                 intent = new Intent(PersonalInfoActivity.this, NameActivity.class);
                startActivityForResult(intent,REQUEST_CODE_UPDATE_NAME);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == REQUEST_CODE_RETURN){
            return;
        }
        if (requestCode == REQUEST_CODE_UPDATE_EMAIL){
            if (resultCode == RESULT_OK){

               mySettingsList.set(1,new CustomSettingsObject.Builder(SETTINGS_KEY_EMAIL, "Email")
                        .setSummary(data.getStringExtra("new_email"))
                        .build());
                updateSettings();
            }
        }
        if (requestCode == REQUEST_CODE_UPDATE_NAME){
            if (resultCode == RESULT_OK){
                mySettingsList.set(5,new CustomSettingsObject.Builder(SETTINGS_KEY_NAME, "Name")
                        .setSummary(data.getStringExtra("new_first_name")+data.getStringExtra("new_last_name"))
                        .build());
                updateSettings();
            }
        }
    }
}