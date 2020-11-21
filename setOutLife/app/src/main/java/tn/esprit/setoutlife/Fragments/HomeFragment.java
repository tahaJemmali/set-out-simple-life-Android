package tn.esprit.setoutlife.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
    Button forumBtn;
    RecyclerView rv;
    GridAdapter gridAdapter;
    View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getContext();
        rv = view.findViewById(R.id.rv);

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