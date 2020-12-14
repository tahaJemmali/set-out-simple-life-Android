package tn.esprit.setoutlife.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.R;

import com.francoisdexemple.materialpreference.ConvenienceBuilder;
import com.francoisdexemple.materialpreference.items.MaterialPreferenceActionItem;
import com.francoisdexemple.materialpreference.items.MaterialPreferenceCheckBoxItem;
import com.francoisdexemple.materialpreference.items.MaterialPreferenceItemOnClickAction;
import com.francoisdexemple.materialpreference.items.MaterialPreferenceOnCheckedChangedListener;
import com.francoisdexemple.materialpreference.items.MaterialPreferenceSwitchItem;
import com.francoisdexemple.materialpreference.items.MaterialPreferenceTitleItem;
import com.francoisdexemple.materialpreference.model.MaterialPreferenceCard;
import com.francoisdexemple.materialpreference.model.MaterialPreferenceList;
import com.francoisdexemple.materialpreference.util.OpenSourceLicense;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;


public class Demo {

    public static MaterialPreferenceList createMaterialPreferenceList(final Context c, final int colorIcon, final int theme) {
        MaterialPreferenceCard.Builder appCardBuilder = new MaterialPreferenceCard.Builder();

        MaterialPreferenceCard.Builder personalInformationCardBuilder = new MaterialPreferenceCard.Builder();
        personalInformationCardBuilder.title("Personal Information");
        personalInformationCardBuilder.addItem(new MaterialPreferenceSwitchItem.Builder()
                .text("Switch")
                .subText("Description of the switch")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_access_point)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnCheckedChanged(new MaterialPreferenceOnCheckedChangedListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Toast.makeText(c,"Now : "+isChecked,Toast.LENGTH_SHORT).show();
                    }
                })
                .setChecked(true)
                .build());

        MaterialPreferenceCard.Builder generalCardBuilder = new MaterialPreferenceCard.Builder();
        generalCardBuilder.title("General");
        generalCardBuilder.addItem(new MaterialPreferenceSwitchItem.Builder()
                .text("Switch")
                .subText("Description of the switch")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_access_point)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnCheckedChanged(new MaterialPreferenceOnCheckedChangedListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Toast.makeText(c,"Now : "+isChecked,Toast.LENGTH_SHORT).show();
                    }
                })
                .setChecked(true)
                .build());

        generalCardBuilder.addItem(new MaterialPreferenceActionItem.Builder()
                .text("Log out")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_logout)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnClickAction(new MaterialPreferenceItemOnClickAction() {
                    @Override
                    public void onClick() {
                        /*Intent intent = new Intent(c, ExampleMaterialPreferenceLicenseActivity.class);
                        intent.putExtra(ExampleMaterialPreferenceActivity.THEME_EXTRA, theme);
                        c.startActivity(intent);*/
                    }
                })
                .build());

        generalCardBuilder.addItem(new MaterialPreferenceActionItem.Builder()
                .text("Language")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_lan)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnClickAction(new MaterialPreferenceItemOnClickAction() {
                    @Override
                    public void onClick() {
                        /*Intent intent = new Intent(c, ExampleMaterialPreferenceLicenseActivity.class);
                        intent.putExtra(ExampleMaterialPreferenceActivity.THEME_EXTRA, theme);
                        c.startActivity(intent);*/
                    }
                })
                .build());

        MaterialPreferenceCard.Builder preferenceCardBuilder = new MaterialPreferenceCard.Builder();
        preferenceCardBuilder.title("Preferences");
        preferenceCardBuilder.addItem(new MaterialPreferenceSwitchItem.Builder()
                .text("Notifications")
                .subText("")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_bell)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnCheckedChanged(new MaterialPreferenceOnCheckedChangedListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //Toast.makeText(c,"Now : "+isChecked,Toast.LENGTH_SHORT).show();

                    }
                })
                .setChecked(true)
                .build());

        MaterialPreferenceCard.Builder informationCardBuilder = new MaterialPreferenceCard.Builder();
        informationCardBuilder.title("Information");

        informationCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_information_outline)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "App Version",
                false));

        return new MaterialPreferenceList(personalInformationCardBuilder.build(),
                generalCardBuilder.build(),
                preferenceCardBuilder.build(),
                informationCardBuilder.build());
    }

    public static MaterialPreferenceList createMaterialPreferenceLicenseList(final Context c, int colorIcon) {

        MaterialPreferenceCard materialPreferenceLIbraryLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "material-Preference-library", "2016", "Daniel Stone",
                OpenSourceLicense.APACHE_2);

        MaterialPreferenceCard androidIconicsLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Android Iconics", "2016", "Mike Penz",
                OpenSourceLicense.APACHE_2);

        MaterialPreferenceCard leakCanaryLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "LeakCanary", "2015", "Square, Inc",
                OpenSourceLicense.APACHE_2);

        MaterialPreferenceCard mitLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "MIT Example", "2017", "Matthew Ian Thomson",
                OpenSourceLicense.MIT);

        MaterialPreferenceCard gplLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "GPL Example", "2017", "George Perry Lindsay",
                OpenSourceLicense.GNU_GPL_3);

        return new MaterialPreferenceList(materialPreferenceLIbraryLicenseCard,
                androidIconicsLicenseCard,
                leakCanaryLicenseCard,
                mitLicenseCard,
                gplLicenseCard);
    }
}