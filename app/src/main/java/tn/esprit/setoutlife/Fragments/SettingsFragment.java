package tn.esprit.setoutlife.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;


import com.francoisdexemple.materialpreference.MaterialPreferenceFragment;
import com.francoisdexemple.materialpreference.items.MaterialPreferenceActionItem;
import com.francoisdexemple.materialpreference.items.MaterialPreferenceItemOnClickAction;
import com.francoisdexemple.materialpreference.model.MaterialPreferenceList;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;


import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.Utils.Demo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class SettingsFragment extends MaterialPreferenceFragment {

    public static final int THEME_LIGHT_DARKBAR = 1;
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    private static final String FRAGMENT_NAME = "Settings";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    protected MaterialPreferenceList getMaterialPreferenceList(final Context c) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
       // ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(FRAGMENT_NAME);

        MaterialPreferenceList list = Demo.createMaterialPreferenceList(c, R.color.mp_color_icon_dark_theme, THEME_LIGHT_DARKBAR);

//        list.getCards().get(2).getItems().add(createDynamicItem("Tap for a random number FAHD", c));

        final MaterialPreferenceActionItem time = new MaterialPreferenceActionItem.Builder()
                .text("Unix Time In Millis FAHD")
                .subText("Time")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_clock)
                        .color(ContextCompat.getColor(c, R.color.mp_color_icon_dark_theme)
                        ).sizeDp(18))
                .build();
//        list.getCards().get(2).getItems().add(time);


        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                System.out.println("on back pressed ");
                if (callBackInterface!=null){
                    callBackInterface.popBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return list;
    }

    private MaterialPreferenceActionItem createDynamicItem(String subText, final Context c) {
        System.out.println("ALOOOOOOOOOOOO");
        final MaterialPreferenceActionItem item = new MaterialPreferenceActionItem.Builder()
                .text("Dynamic UI FAHD")
                .subText(subText)
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_refresh)
                        .color(ContextCompat.getColor(c, R.color.mp_color_icon_dark_theme)
                        ).sizeDp(18))
                .build();
        item.setOnClickAction(new MaterialPreferenceItemOnClickAction() {
            @Override
            public void onClick() {
                item.setSubText("Random number FAHD: " + ((int) (Math.random() * 10)));
                refreshMaterialPreferenceList();
            }
        });
        return item;

    }

    @Override
    protected int getTheme() {
        return R.style.AppTheme_MaterialPreferenceActivity_Fragment;
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        mContext = getContext();

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(FRAGMENT_NAME);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                System.out.println("on back pressed ");
                if (callBackInterface!=null){
                    callBackInterface.popBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        return view;
    }*/

    public void setCallBackInterface (CallBackInterface callBackInterface){
        this.callBackInterface = callBackInterface;
    }
}