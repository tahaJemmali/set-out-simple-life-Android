package tn.esprit.setoutlife.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Adapters.GridAdapter;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.Utils.CallBackInterface;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    ImageButton navBarBtn;
    Context mContext;
    CallBackInterface callBackInterface;
    TextView tvName;
    RecyclerView rv;
    GridAdapter gridAdapter;
    View view;
    CircleImageView profilImage;


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getContext();
        rv = view.findViewById(R.id.rv);
        profilImage = view.findViewById(R.id.profilImage);
        tvName =view.findViewById(R.id.tvName);
        tvName.setText(HomeActivity.getCurrentLoggedInUser().getFirstName()+" "+HomeActivity.getCurrentLoggedInUser().getLastName());

        if (!HomeActivity.getCurrentLoggedInUser().getPhoto().equals("Not mentioned"))
            Picasso.get().load(HomeActivity.getCurrentLoggedInUser().getPhoto()).into(profilImage);
        else
            Picasso.get().load("https://graph.facebook.com/10214899562601635/picture?height=1024").into(profilImage);

        createDataGrid();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

            gridAdapter.setItemCLickListener(new GridAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position,String name) {
                    callBackInterface.openFragment(name);
                }
            });
        return view;
    }

    public void setCallBackInterface (CallBackInterface callBackInterface){
        this.callBackInterface = callBackInterface;
    }

    List<String> titles;
    List<Integer> images;

    public void createDataGrid(){
        titles = new ArrayList<>();
        images = new ArrayList<>();

        titles.add("Profil");
        titles.add("Schedule");
        titles.add("Tasks");
        titles.add("Finance");
        titles.add("Progress & Statistics");
        titles.add("Forum");
        titles.add("Settings");
        titles.add("Tutorial");
        titles.add("Notification");
        titles.add("Support");


        images.add(R.drawable.ic_profil);
        images.add(R.drawable.ic_schedual);
        images.add(R.drawable.ic_task);
        images.add(R.drawable.ic_finance);
        images.add(R.drawable.ic_stat);
        images.add(R.drawable.ic_forum);
        images.add(R.drawable.ic_settings);
        images.add(R.drawable.ic_tuto);
        images.add(R.drawable.ic_notification);
        images.add(R.drawable.ic_support);


        gridAdapter = new GridAdapter(mContext,titles,images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,3,GridLayoutManager.VERTICAL,false);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(gridAdapter);
    }


}