package tn.esprit.setoutlife.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


import im.dacer.androidcharts.LineView;
import tn.esprit.setoutlife.Adapters.BalanceAdapter;

import tn.esprit.setoutlife.Enums.BalanceEnum;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.entities.Balance;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FinanceFragment#} factory method to
 * create an instance of this fragment.
 */
public class FinanceFragment extends Fragment {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    private static final String FRAGMENT_NAME = "Finance";
    FragmentManager fragmentManager;

    //UI
    LineView lineView;
    View averageColor;
    View earnedColor;
    View spendColor;
    Button parDay;
    Button parWeek;
    Button parMonth;
    Button parTotal;
    BalanceAdapter balanceAdapter;
    RecyclerView rvBalance;
    ImageButton addBalance;

    //Variable
    ArrayList balnces;
    ArrayList daysBalances;
    ArrayList weekBalances;
    ArrayList monthBalances;
    ArrayList totalBalances;
    ArrayList<ArrayList<Float>> dataLists;
    public static ArrayList<Balance> global;

    public FinanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_finance, container, false);
        mContext = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(FRAGMENT_NAME);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                System.out.println("on back pressed ");
                if (callBackInterface != null) {
                    callBackInterface.popBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        initUI();
     initUIRecycleViewerBalance();
     if (global!=null&&!global.isEmpty()){
         initChart();
         triClicked();
     }
        addBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBalanceFragment tagFrag = new AddBalanceFragment();
                fragmentManager
                        .beginTransaction().add(R.id.fragment_container, tagFrag, "add balance")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
    private void initUIRecycleViewerBalance() {

        balnces = new ArrayList<Balance>();
         daysBalances= new ArrayList<Date>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        if (global!=null&&!global.isEmpty()){
            balnces=global;
            for (Object row : balnces) {
                Balance balance=(Balance)row;
                if (!daysBalances.contains( formatter.format(balance.getDateCreated())))
                    daysBalances.add( formatter.format(balance.getDateCreated()));
            }
        }
        rvBalance.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        balanceAdapter = new BalanceAdapter(mContext, balnces);
        rvBalance.setAdapter(balanceAdapter);

    }

    void initUI() {
        lineView = (LineView) view.findViewById(R.id.line_view);
        earnedColor = view.findViewById(R.id.earnedCheckBox);
        spendColor = view.findViewById(R.id.spendCheckBox);
        averageColor = view.findViewById(R.id.averageCheckBox);
        parDay = view.findViewById(R.id.parDay);
        parWeek = view.findViewById(R.id.parWeek);
        parMonth = view.findViewById(R.id.parMonth);
        parTotal = view.findViewById(R.id.parTotal);
        rvBalance = view.findViewById(R.id.rvBalance);
        addBalance = view.findViewById(R.id.addBalance);
        earnedColor.getBackground().mutate().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        spendColor.getBackground().mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        averageColor.getBackground().mutate().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
    }

    void triClicked() {

        parDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perDay();
            }
        });
        parWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perWeek();
            }
        });
        parMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perMonth();
            }
        });
        parTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perTotal();
            }
        });
    }

    public void initChart() {
        lineView.setDrawDotLine(true); //optional
        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
        perDay();
    }

    //chart display
    void perDay() {
        ArrayList<Float> spent = new ArrayList<Float>();
        ArrayList<Float> earned = new ArrayList<Float>();
        ArrayList<Float> average = new ArrayList<Float>();
        DecimalFormat df = new DecimalFormat("#.####");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        float x=0;
        float y=0;
        float avg=0;

        for (int i=0;i<daysBalances.size();i++) {
            Log.e("TAG", "perDay: " );
        for (Object row : balnces) {
            Balance balance=(Balance)row;
            if (daysBalances.get(i).equals(formatter.format(((Balance) row).getDateCreated()))){

            if(balance.getType()!=BalanceEnum.EARNED)
             x +=balance.getAmount().floatValue();
            if(balance.getType()!=BalanceEnum.SPENT)
             y += balance.getAmount().floatValue();
            avg=y-x;
            if(avg<0)
                avg=0;
        }
        }
            spent.add(Float.parseFloat(df.format(x)));
            earned.add(Float.parseFloat(df.format(y)));
            average.add(Float.parseFloat(df.format(avg)));
        }
     dataLists = new ArrayList<ArrayList<Float>>();
            dataLists.add(spent);
            dataLists.add(earned);
            dataLists.add(average);
        lineView.setColorArray(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN});
        lineView.setBottomTextList(daysBalances);
        lineView.setFloatDataList(dataLists);
    }

   void perWeek(){
       weekBalances= new ArrayList<Date>();
       SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
       long weeks=0;
       ArrayList<String> bottomTextList= new ArrayList<String>();

       try {
           Date firstDate = sdf.parse(daysBalances.get(0).toString());
           Date secondDate = sdf.parse(daysBalances.get(daysBalances.size()-1).toString());
           long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
           long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
           weeks+=diff/7;

       }catch (Exception e){
           e.printStackTrace();
       }
       for (int i=0;i<=weeks+1;i++){
           bottomTextList.add("week"+(i+1));
           Calendar calendar = Calendar.getInstance();
           calendar.setTime(new Date());
           calendar.add(Calendar.DAY_OF_YEAR,7*i);
          weekBalances.add(calendar.getTime());
       }
       ArrayList<Float> spent = new ArrayList<Float>();
       ArrayList<Float> earned = new ArrayList<Float>();
       ArrayList<Float> average = new ArrayList<Float>();
       DecimalFormat df = new DecimalFormat("#.####");
       SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
       float x = 0;
       float y = 0;
       float avg = 0;
       for (int i=0;i<weekBalances.size();i++) {

           for (Object row : balnces) {
               Balance balance = (Balance) row;
               Date datebefore=(Date)weekBalances.get(i);
               if((i+1)<weekBalances.size())
               {
                   Date dateafter=(Date)weekBalances.get(i+1);

              if ( sdf.format(((Balance) row).getDateCreated()).equals(sdf.format(datebefore))  || (((Balance) row).getDateCreated().before(dateafter)&&((Balance) row).getDateCreated().after(datebefore)))
               {
                       if (balance.getType() != BalanceEnum.EARNED)
                           x += balance.getAmount().floatValue();
                       if (balance.getType() != BalanceEnum.SPENT)
                           y += balance.getAmount().floatValue();
                       avg = y - x;
                       if (avg < 0)
                           avg = 0;
               }
           }
           }
           spent.add(Float.parseFloat(df.format(x)));
           earned.add(Float.parseFloat(df.format(y)));
           average.add(Float.parseFloat(df.format(avg)));
       }

       dataLists = new ArrayList<ArrayList<Float>>();
       dataLists.add(spent);
       dataLists.add(earned);
       dataLists.add(average);
       lineView.setColorArray(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN});
       lineView.setBottomTextList(bottomTextList);
       lineView.setFloatDataList(dataLists);

   }
   void perMonth(){
       monthBalances= new ArrayList<Date>();
       SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
       long month=0;
       ArrayList<String> bottomTextList= new ArrayList<String>();

       try {
           Date firstDate = sdf.parse(daysBalances.get(0).toString());
           Date secondDate = sdf.parse(daysBalances.get(daysBalances.size()-1).toString());
           long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
           long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
           month+=diff/30;

       }catch (Exception e){
           e.printStackTrace();
       }
       for (int i=0;i<=month+1;i++){
           bottomTextList.add("month"+(i+1));
           Calendar calendar = Calendar.getInstance();
           calendar.setTime(new Date());
           calendar.add(Calendar.DAY_OF_YEAR,30*i);
           monthBalances.add(calendar.getTime());
       }
       ArrayList<Float> spent = new ArrayList<Float>();
       ArrayList<Float> earned = new ArrayList<Float>();
       ArrayList<Float> average = new ArrayList<Float>();
       DecimalFormat df = new DecimalFormat("#.####");
       SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
       float x = 0;
       float y = 0;
       float avg = 0;
       for (int i=0;i<monthBalances.size();i++) {

           for (Object row : balnces) {
               Balance balance = (Balance) row;
               Date datebefore=(Date)monthBalances.get(i);
               if((i+1)<monthBalances.size())
               {
                   Date dateafter=(Date)monthBalances.get(i+1);
  if ( sdf.format(((Balance) row).getDateCreated()).equals(sdf.format(datebefore))  || (((Balance) row).getDateCreated().before(dateafter)&&((Balance) row).getDateCreated().after(datebefore)))
                   {

                       if (balance.getType() != BalanceEnum.EARNED)
                           x += balance.getAmount().floatValue();
                       if (balance.getType() != BalanceEnum.SPENT)
                           y += balance.getAmount().floatValue();
                       avg = y - x;
                       if (avg < 0)
                           avg = 0;
                   }
               }
           }
           spent.add(Float.parseFloat(df.format(x)));
           earned.add(Float.parseFloat(df.format(y)));
           average.add(Float.parseFloat(df.format(avg)));
       }

       dataLists = new ArrayList<ArrayList<Float>>();
       dataLists.add(spent);
       dataLists.add(earned);
       dataLists.add(average);
       lineView.setColorArray(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN});
       lineView.setBottomTextList(bottomTextList);
       lineView.setFloatDataList(dataLists);
   }
   void perTotal(){
       totalBalances= new ArrayList<String>();
       totalBalances.add("total");
       float x = 0;
       float y = 0;
       float avg = 0;
       ArrayList<Float> spent = new ArrayList<Float>();
       ArrayList<Float> earned = new ArrayList<Float>();
       ArrayList<Float> average = new ArrayList<Float>();
       DecimalFormat df = new DecimalFormat("#.####");
       for (Object row : balnces) {
           Balance balance = (Balance) row;
           if (balance.getType() != BalanceEnum.EARNED)
               x += balance.getAmount().floatValue();
           if (balance.getType() != BalanceEnum.SPENT)
               y += balance.getAmount().floatValue();
           avg = y - x;
           if (avg < 0)
               avg = 0;

       }
       spent.add(Float.parseFloat(df.format(x)));
       earned.add(Float.parseFloat(df.format(y)));
       average.add(Float.parseFloat(df.format(avg)));
       dataLists = new ArrayList<ArrayList<Float>>();
       dataLists.add(spent);
       dataLists.add(earned);
       dataLists.add(average);
       lineView.setColorArray(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN});
       lineView.setBottomTextList(totalBalances);
       lineView.setFloatDataList(dataLists);
   }

    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }
}