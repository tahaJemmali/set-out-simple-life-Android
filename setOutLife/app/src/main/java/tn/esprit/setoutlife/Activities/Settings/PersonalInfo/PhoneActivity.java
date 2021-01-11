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

public class PhoneActivity extends AppCompatActivity implements IRepository {

    androidx.appcompat.widget.Toolbar toolbar;

    private ArrayList<SettingsObject> mySettingsList;
    private Toast mToast;

    public static final String SETTINGS_KEY_PHONE = "SETTINGS_KEY_PHONE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        UserRepository.getInstance().setiRepository(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit your phone number");

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
        LinearLayout container = findViewById(R.id.PersonalInfosettingsPhoneContainer);
        EasySettings.inflateSettingsLayout(this, container, mySettingsList);
    }

    private void createSettings() {

        mySettingsList = EasySettings.createSettingsArray(
                new HeaderSettingsObject.Builder("Phone")
                        .build(),
                new CustomSettingsObjectEditText.Builder(SETTINGS_KEY_PHONE, "")
                        .setSummary("")
                        .build()
        );
    }

    public void saveBtn(View view){

        System.out.println("new phone number : " +CustomSettingsObjectEditText.phoneText);

        if (CustomSettingsObjectEditText.phoneText == null || CustomSettingsObjectEditText.phoneText.equals(""))
        {
            Toast.makeText(PhoneActivity.this,"Phone cannot be empty!",Toast.LENGTH_SHORT).show();
            return;
        }

        //String namePattern = "^[a-zA-Z]*$";
        if (CustomSettingsObjectEditText.phoneText.length()!=8)
        {
            Toast.makeText(PhoneActivity.this,"Invalid phone number!",Toast.LENGTH_SHORT).show();
            return;
        }

        User u = new User();
        u.setEmail(HomeActivity.getCurrentLoggedInUser().getEmail());
        u.setPhone(CustomSettingsObjectEditText.phoneText);
        UserRepository.getInstance().updateUser(u, PhoneActivity.this);

        //currentUser update
        HomeActivity.getCurrentLoggedInUser().setPhone(CustomSettingsObjectEditText.phoneText);

        Intent intent = new Intent();
        intent.putExtra("new_phone",CustomSettingsObjectEditText.phoneText);
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

    @Override
    public void doAction2() {
        //
    }

}