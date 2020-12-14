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

public class EmailActivity extends AppCompatActivity implements IRepository {

    androidx.appcompat.widget.Toolbar toolbar;

    private ArrayList<SettingsObject> mySettingsList;
    private Toast mToast;

    public static final String SETTINGS_KEY_EMAIL = "SETTINGS_KEY_EMAIL";
    public static final String SETTINGS_KEY_PASSWORD = "SETTINGS_KEY_PASSWORD";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        UserRepository.getInstance().setiRepository(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change your email");

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
        LinearLayout container = findViewById(R.id.PersonalInfosettingsEmailContainer);
        EasySettings.inflateSettingsLayout(this, container, mySettingsList);
    }

    private void createSettings() {

        mySettingsList = EasySettings.createSettingsArray(
                new HeaderSettingsObject.Builder("New email address")
                        .build(),
                new CustomSettingsObjectEditText.Builder(SETTINGS_KEY_EMAIL, "")
                        .setSummary("")
                        .build(),
                new HeaderSettingsObject.Builder("")
                        .build(),
                new HeaderSettingsObject.Builder("Current Password")
                        .build(),
                new CustomSettingsObjectEditText.Builder(SETTINGS_KEY_PASSWORD, "")
                        .setSummary("")
                        .build()
        );
    }

    public void saveBtn(View view){

        System.out.println("new email : " +CustomSettingsObjectEditText.emailText);
        System.out.println("password : " +CustomSettingsObjectEditText.passwordText);

        if (CustomSettingsObjectEditText.emailText == null || CustomSettingsObjectEditText.emailText.equals(""))
        {
            Toast.makeText(EmailActivity.this,"Email address cannot be empty!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (CustomSettingsObjectEditText.passwordText == null || CustomSettingsObjectEditText.passwordText.equals(""))
        {
            Toast.makeText(EmailActivity.this,"Password cannot be empty!",Toast.LENGTH_SHORT).show();
            return;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!CustomSettingsObjectEditText.emailText.matches(emailPattern))
        {
            Toast.makeText(EmailActivity.this,"Invalid email address!",Toast.LENGTH_SHORT).show();
            return;
        }

        //todo NODE jS API
        UserRepository.getInstance().updateEmailUser(HomeActivity.getCurrentLoggedInUser().getEmail(),
                 CustomSettingsObjectEditText.emailText,
                CustomSettingsObjectEditText.passwordText, this);

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
        intent.putExtra("new_email",CustomSettingsObjectEditText.emailText);
        //intent.putExtra("new_first_name",CustomSettingsObjectEditText.firstNameText);
        //intent.putExtra("new_last_name",CustomSettingsObjectEditText.lastNameText);
        setResult(RESULT_OK, intent);
        finish();
    }

}