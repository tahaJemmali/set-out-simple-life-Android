package tn.esprit.setoutlife.Activities.Settings.PersonalInfo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hotmail.or_dvir.easysettings.pojos.EasySettings;
import com.hotmail.or_dvir.easysettings.pojos.HeaderSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.SettingsObject;

import java.util.ArrayList;

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Activities.Settings.CustomSettings.CustomSettingsObjectEditText;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.IRepository;
import tn.esprit.setoutlife.Repository.UserRepository;
import tn.esprit.setoutlife.entities.User;

public class HomeTownActivity extends AppCompatActivity implements IRepository {

    androidx.appcompat.widget.Toolbar toolbar;

    private ArrayList<SettingsObject> mySettingsList;
    private Toast mToast;

    public static final String SETTINGS_KEY_HOMETOWN = "SETTINGS_KEY_HOMETOWN";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_town);

        UserRepository.getInstance().setiRepository(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit your home town");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }
        createSettings();
        commitSettings();
    }

    private void commitSettings() {
        LinearLayout container = findViewById(R.id.PersonalInfosettingshomeTownContainer);
        EasySettings.inflateSettingsLayout(this, container, mySettingsList);
    }

    private void createSettings() {

        mySettingsList = EasySettings.createSettingsArray(
                new HeaderSettingsObject.Builder("Home town")
                        .build(),
                new CustomSettingsObjectEditText.Builder(SETTINGS_KEY_HOMETOWN, "")
                        .setSummary("")
                        .build()
        );
    }

    public void saveBtn(View view){

        System.out.println("new home town : " +CustomSettingsObjectEditText.homeTownText);

        if (CustomSettingsObjectEditText.homeTownText == null || CustomSettingsObjectEditText.homeTownText.equals(""))
        {
            Toast.makeText(HomeTownActivity.this,"Home town cannot be empty!",Toast.LENGTH_SHORT).show();
            return;
        }

        String namePattern = "^[\\p{L} .'-]+$";

        if (!CustomSettingsObjectEditText.homeTownText.matches(namePattern))
        {
            Toast.makeText(HomeTownActivity.this,"Invalid home town!",Toast.LENGTH_SHORT).show();
            return;
        }


        User u = new User();
        u.setEmail(HomeActivity.getCurrentLoggedInUser().getEmail());
        u.setAddress(CustomSettingsObjectEditText.homeTownText);
        UserRepository.getInstance().updateUser(u, HomeTownActivity.this);

        //currentUser update
        HomeActivity.getCurrentLoggedInUser().setAddress(CustomSettingsObjectEditText.homeTownText);

        Intent intent = new Intent();
        intent.putExtra("new_home_town",CustomSettingsObjectEditText.homeTownText);
        setResult(RESULT_OK, intent);
        finish();

    }

    public void cancelBtn(View view) {
        onBackPressed();
    }


    @Override
    public void showLoadingButton() {

    }

    @Override
    public void doAction() {
        /*Intent intent = new Intent();
        intent.putExtra("new_first_name",CustomSettingsObjectEditText.firstNameText);
        intent.putExtra("new_last_name",CustomSettingsObjectEditText.lastNameText);
        setResult(RESULT_OK, intent);
        finish();*/
    }

}