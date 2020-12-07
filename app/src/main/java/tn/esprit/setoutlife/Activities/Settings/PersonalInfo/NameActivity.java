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

public class NameActivity extends AppCompatActivity implements IRepository {

    androidx.appcompat.widget.Toolbar toolbar;

    private ArrayList<SettingsObject> mySettingsList;
    private Toast mToast;

    public static final String SETTINGS_KEY_FIRSTNAME = "SETTINGS_KEY_FIRSTNAME";
    public static final String SETTINGS_KEY_LASTNAME = "SETTINGS_KEY_LASTNAME";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        UserRepository.getInstance().setiRepository(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit your name");

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
        LinearLayout container = findViewById(R.id.PersonalInfosettingsNameContainer);
        EasySettings.inflateSettingsLayout(this, container, mySettingsList);
    }

    private void createSettings() {

        mySettingsList = EasySettings.createSettingsArray(
                new HeaderSettingsObject.Builder("First name")
                        .build(),
                new CustomSettingsObjectEditText.Builder(SETTINGS_KEY_FIRSTNAME, "")
                        .setSummary("")
                        .build(),
                new HeaderSettingsObject.Builder("")
                        .build(),
                new HeaderSettingsObject.Builder("Last name")
                        .build(),
                new CustomSettingsObjectEditText.Builder(SETTINGS_KEY_LASTNAME,"")
                        .setSummary("")
                        .build()
        );
    }

    public void saveBtn(View view){

        System.out.println("new first name : " +CustomSettingsObjectEditText.firstNameText);
        System.out.println("new last name: " +CustomSettingsObjectEditText.lastNameText);

        if (CustomSettingsObjectEditText.firstNameText == null || CustomSettingsObjectEditText.firstNameText.equals(""))
        {
            Toast.makeText(NameActivity.this,"First name cannot be empty!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (CustomSettingsObjectEditText.lastNameText == null || CustomSettingsObjectEditText.lastNameText.equals(""))
        {
            Toast.makeText(NameActivity.this,"Last name be empty!",Toast.LENGTH_SHORT).show();
            return;
        }

        /*//todo NODE jS API
        UserRepository.getInstance().updateNameUser(HomeActivity.getCurrentLoggedInUser().getEmail(),
                 CustomSettingsObjectEditText.firstNameText,
                CustomSettingsObjectEditText.lastNameText, this);
*/
    }

    public void cancelBtn(View view) {
        onBackPressed();
    }


    @Override
    public void showLoadingButton() {

    }

    @Override
    public void doAction() {
        Intent intent = new Intent();
        intent.putExtra("new_first_name",CustomSettingsObjectEditText.firstNameText);
        intent.putExtra("new_last_name",CustomSettingsObjectEditText.lastNameText);
        setResult(RESULT_OK, intent);
        finish();
    }

}