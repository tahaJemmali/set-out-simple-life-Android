package tn.esprit.setoutlife.Activities;
import com.facebook.login.LoginManager;
import com.hotmail.or_dvir.easysettings.events.SeekBarSettingsValueChangedEvent;
import com.hotmail.or_dvir.easysettings.events.SwitchSettingsClickEvent;
import com.hotmail.or_dvir.easysettings_dialogs.events.ListSettingsValueChangedEvent;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.hotmail.or_dvir.easysettings.events.BasicSettingsClickEvent;
import com.hotmail.or_dvir.easysettings.pojos.BasicSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.CheckBoxSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.EasySettings;
import com.hotmail.or_dvir.easysettings.pojos.HeaderSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.SeekBarSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.SettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.SwitchSettingsObject;
import com.hotmail.or_dvir.easysettings_dialogs.pojos.EditTextSettingsObject;
import com.hotmail.or_dvir.easysettings_dialogs.pojos.ListSettingsObject;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import tn.esprit.setoutlife.Activities.Settings.PersonalInfoActivity;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Utils.CallBackInterface;

public class SettingsActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;

    private ArrayList<SettingsObject> mySettingsList;

    public static final String DEFAULT_VALUE_EDIT_TEXT = "default value";
    //todo remember that these keys must NOT be changed because they are being used as id's!!!
    public static final String SETTINGS_KEY_BASIC = "SETTINGS_KEY_BASIC";
    public static final String SETTINGS_KEY_LOGOUT = "SETTINGS_KEY_LOGOUT";
    public static final String SETTINGS_KEY_RINGTONE = "SETTINGS_KEY_RINGTONE";
    public static final String SETTINGS_KEY_PERSONAL_DETAILS = "SETTINGS_KEY_PERSONAL_DETAILS";
    public static final String SETTINGS_KEY_SWITCH_NOTIFICATIONS = "SETTINGS_KEY_SWITCH_NOTIFICATIONS";
    public static final String SETTINGS_KEY_LIST_LANGUAGE_CHOICE = "SETTINGS_KEY_LIST_LANGUAGE_CHOICE";


    SharedPreferences settingsSharedPrefs;

    private static final int REQUEST_CODE_NOTIFICATION_PICKER = 1000;
    private Toast mToast;

    @Nullable
    private TextView tvNotificationToneSummary;
    public static final String SETTINGS_RINGTONE_SILENT_VALUE = "";
    public static final String DEFAULT_NOTIFICATION_SUMMARY = "Silent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
             w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        settingsSharedPrefs = EasySettings.retrieveSettingsSharedPrefs(this);

        createSettings();
        commitSettings();

    }

    private void commitSettings() {
        LinearLayout container = findViewById(R.id.settingsContainer);
        EasySettings.inflateSettingsLayout(this, container, mySettingsList);

        SettingsObject notificationSetting =
                EasySettings.findSettingsObject(SETTINGS_KEY_RINGTONE, mySettingsList);
        if(notificationSetting != null &&
                notificationSetting.getTextViewSummaryId() != null)
        {

            View root = findViewById(notificationSetting.getRootId());
            tvNotificationToneSummary = root.findViewById(notificationSetting.getTextViewSummaryId());

            String notificationUriAsString = EasySettings.retrieveSettingsSharedPrefs(this)
                    .getString(SETTINGS_KEY_RINGTONE,
                            SETTINGS_RINGTONE_SILENT_VALUE);
            //silent was chosen
            if(notificationUriAsString.equals(SETTINGS_RINGTONE_SILENT_VALUE))
            {
                tvNotificationToneSummary.setText(DEFAULT_NOTIFICATION_SUMMARY);
            }

            else
            {
                Uri notificationUri = Uri.parse(notificationUriAsString);

                Ringtone ringtone = RingtoneManager.getRingtone(this, notificationUri);
                String soundTitle = ringtone.getTitle(this);
                tvNotificationToneSummary.setText(soundTitle);
            }
        }

    }

    private void createSettings() {

        ArrayList<String> listLaguageOptionsItems = new ArrayList<>();
        listLaguageOptionsItems.add("English");
        listLaguageOptionsItems.add("Frensh");

        mySettingsList = EasySettings.createSettingsArray(
                new HeaderSettingsObject.Builder("Personal Informations")
                        .build(),
                    new BasicSettingsObject.Builder(SETTINGS_KEY_PERSONAL_DETAILS, "Edit personal details")
                        .setSummary("")
                        .addDivider()
                        .build(),
                new HeaderSettingsObject.Builder("General")
                        .build(),
                    new BasicSettingsObject.Builder(SETTINGS_KEY_LOGOUT, "Log out")
                        .setIcon(R.drawable.ic_logout)
                        .setSummary("")
                        .addDivider()
                        .build(),
                new ListSettingsObject.Builder(SETTINGS_KEY_LIST_LANGUAGE_CHOICE, "Language", "English", listLaguageOptionsItems, "save")
                        .setUseValueAsSummary()
                        .setNegativeBtnText("cancel")
                        .build(),
                new HeaderSettingsObject.Builder("Preferences")
                        .build(),
                    new SwitchSettingsObject.Builder(SETTINGS_KEY_SWITCH_NOTIFICATIONS, "Notifications",false)
                        .setSummary("")
                        .addDivider()
                        .build(),
                    new BasicSettingsObject.Builder(SETTINGS_KEY_RINGTONE,"Notification Sound")
                        .setSummary("aaa")
                        .setIcon(R.drawable.ic_notification_sound)
                        .addDivider()
                        .build(),
                new HeaderSettingsObject.Builder("Information")
                        .build(),
                    new BasicSettingsObject.Builder(SETTINGS_KEY_BASIC, "App Version")
                            .setSummary("1.0.0")
                            .addDivider()
                            .build(),
                    new BasicSettingsObject.Builder(SETTINGS_KEY_BASIC, "About us")
                            .setSummary("")
                            .addDivider()
                            .build()
        );
    }

    @Subscribe
    public void onBasicSettingsClicked(BasicSettingsClickEvent event)
    {
        if (event.getClickedSettingsObj()
                .getKey()
                .equals(SETTINGS_KEY_PERSONAL_DETAILS)){
            Intent intent = new Intent(SettingsActivity.this, PersonalInfoActivity.class);
            startActivity(intent);
        }
        if (event.getClickedSettingsObj()
                .getKey()
                .equals(SETTINGS_KEY_LIST_LANGUAGE_CHOICE)){

        }
        if (event.getClickedSettingsObj().getKey().equals(SETTINGS_KEY_LOGOUT)){
            //if (HomeActivity.getCurrentLoggedInUser().getSigned_up_with().equals("facebook"))
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
        if(event.getClickedSettingsObj()
                .getKey()
                .equals(SETTINGS_KEY_RINGTONE))
        {
            String uriAsString = EasySettings.retrieveSettingsSharedPrefs(this)
                    .getString(SETTINGS_KEY_RINGTONE,
                            SETTINGS_RINGTONE_SILENT_VALUE);

            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
                    .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false)
                    .putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
                    .putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "");

            if(uriAsString.equals(SETTINGS_RINGTONE_SILENT_VALUE))
            {
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, 0);
            }

            else
            {
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                        Uri.parse(uriAsString));
            }

            startActivityForResult(intent, REQUEST_CODE_NOTIFICATION_PICKER);
        }

        /*else
        {
            makeToast(event.getClickedSettingsObj().getTitle());
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {   super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQUEST_CODE_NOTIFICATION_PICKER &&
                resultCode == RESULT_OK)
        {
            Uri notificationToneUri = (Uri) data.getExtras().get(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            //todo save the value to shared preferences
            EasySettings.retrieveSettingsSharedPrefs(this)
                    .edit()
                    .putString(SettingsActivity.SETTINGS_KEY_RINGTONE,
                            //if notificationToneUri is null, it means "silent"
                            //was chosen.
                            notificationToneUri == null ? SETTINGS_RINGTONE_SILENT_VALUE
                                    : notificationToneUri.toString())
                    .apply();

            String soundTitle;

            //"silent" was chosen
            if(notificationToneUri == null)
            {
                soundTitle = DEFAULT_NOTIFICATION_SUMMARY;
            }

            else
            {
                Ringtone ringtone = RingtoneManager.getRingtone(this, notificationToneUri);
                soundTitle = ringtone.getTitle(this);
            }

            if(tvNotificationToneSummary != null)
            {
                tvNotificationToneSummary.setText(soundTitle);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void makeToast(String message)
    {
        if (mToast != null)
        {
            mToast.cancel();
        }

        mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onSwitchSettingsClicked(SwitchSettingsClickEvent event)
    {
        boolean prefValue = EasySettings.retrieveSettingsSharedPrefs(this)
                .getBoolean(event.getClickedSettingsObj().getKey(),
                        event.getClickedSettingsObj().getDefaultValue());

        makeToast(prefValue + "");
    }

    @Subscribe
    public void onListSettingsValueChanged(ListSettingsValueChangedEvent event)
    {
        //todo remember: event.getNewValueAsSaved() returns the value with the delimiter
        makeToast( event.getNewValueAsSaved()+" selected");

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

}