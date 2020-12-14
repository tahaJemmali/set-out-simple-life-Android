package tn.esprit.setoutlife.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import tn.esprit.setoutlife.Activities.SignUpActivity;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.TagRepository;
import tn.esprit.setoutlife.Retrofit.INodeJsService;
import tn.esprit.setoutlife.Retrofit.RetrofitClient;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.entities.Tag;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTagFragment#} factory method to
 * create an instance of this fragment.
 */
public class AddTagFragment extends Fragment {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    EditText tagName;
    View tagColor;
    ColorPickerView colorPickerView;
    ImageButton close;
    INodeJsService iNodeJsService;
    ImageButton addTagBtn;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_tag, container, false);
        mContext = getContext();
        fragmentManager= getActivity().getSupportFragmentManager();
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                fragmentManager.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        //Init NodeJs Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iNodeJsService = retrofitClient.create(INodeJsService.class);

        initUI();
        colorSelector();
        close();

        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTag(tagName.getText().toString(), "#"+colorPickerView.getColorHtml());
            }
        });

        return view;
    }

    void close() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });
    }

    void colorSelector() {
        colorPickerView.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                tagColor.getBackground().mutate().setColorFilter(colorEnvelope.getColor(), PorterDuff.Mode.SRC_ATOP);
            }
        });
    }

    public void initUI() {
        tagColor = view.findViewById(R.id.tagColor);
        colorPickerView = view.findViewById(R.id.colorPickerView);
        close = view.findViewById(R.id.closeTag);
        addTagBtn = view.findViewById(R.id.addTagBtn);
        tagName = view.findViewById(R.id.tagName);
    }


    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }

    void addTag(String tagName, String tagColor) {
        if (TextUtils.isEmpty(tagName)) {
            Toast.makeText(mContext, "Tag name cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog dialogg = ProgressDialog.show(mContext
                , "","Loading..Wait.." , true);
        dialogg.show();
        TagRepository.addTag(mContext,new Tag("",tagName,tagColor),dialogg,fragmentManager);
        //addTag

    }
}