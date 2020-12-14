package tn.esprit.setoutlife.Repository;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tn.esprit.setoutlife.Enums.BalanceEnum;
import tn.esprit.setoutlife.Fragments.FinanceFragment;
import tn.esprit.setoutlife.Fragments.TaskFragment;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Retrofit.RetrofitClient;
import tn.esprit.setoutlife.Utils.VolleyInstance;
import tn.esprit.setoutlife.entities.Balance;
import tn.esprit.setoutlife.entities.Project;
import tn.esprit.setoutlife.entities.Tag;
import tn.esprit.setoutlife.entities.Task;

public class BalanceRepository {
    public static ArrayList<Balance> balances;
    public static void addBalance(final Context mcontext, Balance balance, final ProgressDialog dialog, final FragmentManager fragmentManager){
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            //input your API parameters
            object.put("balanceName",balance.getName());
            object.put("balanceAmount",balance.getAmount());
            object.put("dateCreation",balance.getDateCreated());
            object.put("type",balance.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String url = RetrofitClient.url + "/add_balance";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "done: "+response.getString("message"));
                            getAllBalance(mcontext,dialog, fragmentManager);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "fail: "+error);
            }
        });
        VolleyInstance.getInstance(mcontext).addToRequestQueue(request);
    }
    public static void deleteBalance(final Context mcontext, String id, final ProgressDialog dialog, final FragmentManager fragmentManager){
        final String url=RetrofitClient.url + "/delete_balance/"+id;
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("TAG", "done: "+response.getString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {

                    getAllBalance(mcontext,dialog,fragmentManager);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+url);
            }
        });
        VolleyInstance.getInstance(mcontext).addToRequestQueue(request);
    }

    private static void getAllBalance(Context mcontext,final ProgressDialog dialog,final FragmentManager fragmentManager) {
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, RetrofitClient.url + "/all_balances", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            balances=new ArrayList<Balance>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("balances");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ArrayList<Task> tasks=new ArrayList<Task>();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                JSONObject jsonBalance = jsonArray.getJSONObject(i);
                                String dateStr = jsonBalance.getString("dateCreation");
                                Date dateCreation = sdf.parse(dateStr);
                                Balance balance=new Balance(jsonBalance.getString("_id"),
                                        jsonBalance.getString("balanceName"),
                                        BalanceEnum.valueOf(jsonBalance.getString("type")),
                                        jsonBalance.getDouble("balanceAmount"),
                                        dateCreation);

                                balances.add(balance);
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();

                        }finally {
                            FinanceFragment.global =balances;
                            dialog.dismiss();
                            FinanceFragment tagFrag = new FinanceFragment();
                            fragmentManager
                                    .beginTransaction().replace(R.id.fragment_container, tagFrag, "Finances")
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+RetrofitClient.url + "/all_balances");
            }
        });
        VolleyInstance.getInstance(mcontext).addToRequestQueue(request);

    }
    public static void getAllBalance(Context mcontext) {
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, RetrofitClient.url + "/all_balances", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            balances=new ArrayList<Balance>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("balances");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                JSONObject jsonBalance = jsonArray.getJSONObject(i);
                                String dateStr = jsonBalance.getString("dateCreation");
                                Date dateCreation = sdf.parse(dateStr);
                                Balance balance=new Balance(jsonBalance.getString("_id"),
                                        jsonBalance.getString("balanceName"),
                                        BalanceEnum.valueOf(jsonBalance.getString("type")),
                                        jsonBalance.getDouble("balanceAmount"),
                                        dateCreation);

                                balances.add(balance);
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();

                        }finally {
                            FinanceFragment.global =balances;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+RetrofitClient.url + "/all_balances");
            }
        });
        VolleyInstance.getInstance(mcontext).addToRequestQueue(request);

    }
}
