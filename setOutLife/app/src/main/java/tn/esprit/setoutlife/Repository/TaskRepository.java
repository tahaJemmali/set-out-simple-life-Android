package tn.esprit.setoutlife.Repository;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Fragments.AddProjectFragment;
import tn.esprit.setoutlife.Fragments.AddTaskFragment;
import tn.esprit.setoutlife.Fragments.ScheduleFragment;
import tn.esprit.setoutlife.Fragments.TaskFragment;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Retrofit.RetrofitClient;
import tn.esprit.setoutlife.Utils.VolleyInstance;
import tn.esprit.setoutlife.entities.Tag;
import tn.esprit.setoutlife.entities.Task;

public class TaskRepository {

    public static ArrayList<Task> schedules;
    public static ArrayList<Task> tasks;

    public static void deleteTask(Context mcontext, Task task){
        final String url=RetrofitClient.url + "/delete_task/"+task.getId();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("TAG", "done: "+response.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
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
    public static void addTask(Context mcontext, Task task)throws UnsupportedEncodingException {
        JSONObject object = new JSONObject();
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(task.getDateCreation());    // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 1);      // adds one hour
        task.setEndTime(cal.getTime());
        try {
            //input your API parameters
            object.put("taskName",task.getTaskName());
            object.put("note",task.getNote());
            object.put("importance",task.getImportance());
            object.put("enjoyment",task.getEnjoyment());
            object.put("dateCreation",task.getDateCreation());
            object.put("deadline",task.getDeadline());
            object.put("reminder",task.getReminder());
            object.put("endTime",task.getEndTime());
            object.put("schedule",false);
            object.put("user",HomeActivity.getCurrentLoggedInUser().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addPost(object,mcontext);
    }
     public static void  getAllScheudles(Context mContext){

        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, RetrofitClient.url + "/all_schedules/"+ HomeActivity.getCurrentLoggedInUser().getId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            schedules=new ArrayList<Task>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("tasks");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                String dateStr = jsonTag.getString("dateCreation");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                Date dateCreation = sdf.parse(dateStr);
                                String dateStr2 = jsonTag.getString("endTime");
                                Date dateEnd = sdf.parse(dateStr2);
                                Task schedule=new Task(jsonTag.getString("_id"),
                                        jsonTag.getString("taskName"),
                                        jsonTag.getInt("importance"),
                                        jsonTag.getInt("enjoyment"),
                                        jsonTag.getString("note"),
                                        dateCreation,
                                        dateEnd,
                                        true);
                                schedules.add(schedule);
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();

                        }finally {
                            ScheduleFragment.global=schedules;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+RetrofitClient.url + "/all_tags");
            }
        });
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }
    public static void  getAllTasks(Context mContext){
        System.out.println("Current user : " + HomeActivity.getCurrentLoggedInUser().getId());
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, RetrofitClient.url + "/all_tasks/"+ HomeActivity.getCurrentLoggedInUser().getId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tasks=new ArrayList<Task>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("tasks");
                           for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                String dateStr = jsonTag.getString("dateCreation");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                Date dateCreation = sdf.parse(dateStr);
                               String dateStr2="";
                               String dateStr3="";
                               Date deadline =new Date();
                               Date reminder =new Date();
                               if( jsonTag.has("deadline") &&jsonTag.has("reminder")){
                                    dateStr2 = jsonTag.getString("deadline");
                                    dateStr3 = jsonTag.getString("reminder");
                                    deadline = sdf.parse(dateStr2);
                                    reminder = sdf.parse(dateStr3);
                               }
                                if (jsonTag.getBoolean("schedule") == true){
                                    Task t=new Task(jsonTag.getString("_id"),
                                            jsonTag.getString("taskName"),
                                            dateCreation,
                                            deadline,
                                            reminder,
                                            jsonTag.getInt("importance"),
                                            jsonTag.getInt("enjoyment"),
                                            jsonTag.getString("note"),
                                            true);
                                    tasks.add(t);
                                }
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();

                        }finally {
                            Log.e("TAG", "globalTasks: "+tasks );
                            AddProjectFragment.globalTasks=tasks;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+RetrofitClient.url + "/all_tasks/"+ HomeActivity.getCurrentLoggedInUser().getId());
            }
        });
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }
    public static void addSchedule(Context mcontext, Task task) throws UnsupportedEncodingException {
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("taskName",task.getTaskName());
            object.put("note",task.getNote());
            object.put("importance",task.getImportance());
            object.put("enjoyment",task.getEnjoyment());
            object.put("dateCreation",task.getDateCreation());
            object.put("endTime",task.getEndTime());
            object.put("schedule",true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
      addPost(object,mcontext);
    }
    static void addPost(JSONObject object,Context mcontext){
        // Enter the correct url for your api service site
        final String url = RetrofitClient.url + "/add_task";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "done: "+response.getString("message"));
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

    ///WAITING
    public static void  getAllTasks(Context mContext, final ProgressDialog dialogg, final FragmentManager fragmentManager){
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, RetrofitClient.url + "/all_schedules/"+ HomeActivity.getCurrentLoggedInUser().getId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            schedules=new ArrayList<Task>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("tasks");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                String dateStr = jsonTag.getString("dateCreation");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                Date dateCreation = sdf.parse(dateStr);
                                String dateStr2 = jsonTag.getString("endTime");
                                Date dateEnd = sdf.parse(dateStr2);
                                Task schedule=new Task(jsonTag.getString("_id"),
                                        jsonTag.getString("taskName"),
                                        jsonTag.getInt("importance"),
                                        jsonTag.getInt("enjoyment"),
                                        jsonTag.getString("note"),
                                        dateCreation,
                                        dateEnd,
                                        true);
                                schedules.add(schedule);
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();

                        }finally {
                            ScheduleFragment.global=schedules;
                            dialogg.dismiss();
                            ScheduleFragment tagFrag = new ScheduleFragment();
                            fragmentManager
                                    .beginTransaction().replace(R.id.fragment_container, tagFrag, "Tasks")
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+RetrofitClient.url + "/all_tags");
            }
        });
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    public static void addSchedule(final Context mcontext, Task task, final ProgressDialog dialogg, final FragmentManager fragmentManager) throws UnsupportedEncodingException {
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("taskName",task.getTaskName());
            object.put("note",task.getNote());
            object.put("importance",task.getImportance());
            object.put("enjoyment",task.getEnjoyment());
            object.put("dateCreation",task.getDateCreation());
            object.put("endTime",task.getEndTime());
            object.put("schedule",true);
            object.put("user",HomeActivity.getCurrentLoggedInUser().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String url = RetrofitClient.url + "/add_task";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "done: "+response.getString("message"));
                            getAllTasks(mcontext,dialogg,fragmentManager);
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
    public static void deleteTask(final Context mcontext, Task task, final ProgressDialog dialogg, final FragmentManager fragmentManager){
        final String url=RetrofitClient.url + "/delete_task/"+task.getId();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("TAG", "done: "+response.getString("message"));
                    getAllTasks(mcontext,dialogg,fragmentManager);
                } catch (JSONException e) {
                    e.printStackTrace();
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

    public static void updateTask(final Context mcontext,JSONObject obj,String id){
        final String url=RetrofitClient.url + "/update_task/"+id;
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.PUT, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("TAG", "done: "+response.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
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
}
