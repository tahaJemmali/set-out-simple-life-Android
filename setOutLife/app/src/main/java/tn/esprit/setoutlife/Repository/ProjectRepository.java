package tn.esprit.setoutlife.Repository;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Fragments.ProjectFragment;
import tn.esprit.setoutlife.Fragments.ScheduleFragment;
import tn.esprit.setoutlife.Fragments.TaskFragment;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Retrofit.RetrofitClient;
import tn.esprit.setoutlife.Utils.VolleyInstance;
import tn.esprit.setoutlife.entities.Project;
import tn.esprit.setoutlife.entities.Tag;
import tn.esprit.setoutlife.entities.Task;

public class ProjectRepository {
private static ArrayList<Project> projects;
    public static void addProject(final Context mContext, Project project, final ProgressDialog dialogg, final FragmentManager fragmentManager){
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            //input your API parameters
            object.put("projectName",project.getName());
            object.put("description",project.getDescription());
            object.put("dateCreation",project.getDateCreated());
            for(Task row:project.getTasks()){
                JSONObject object1 = new JSONObject();
                object1.put("_id",row.getId());
                object1.put("taskName",row.getTaskName());
                object1.put("note",row.getNote());
                object1.put("importance",row.getImportance());
                object1.put("enjoyment",row.getEnjoyment());
                object1.put("dateCreation",row.getDateCreation());
                object1.put("deadline",row.getDeadline());
                object1.put("reminder",row.getReminder());
                object1.put("schedule",false);
                jsonArray.put(object1);
                TaskRepository.updateTask(mContext,object1,row.getId());
            }
            JSONObject object1 = new JSONObject();

            object1.put("_id",project.getTag().getId());
            object1.put("tagName",project.getTag().getName());
            object1.put("color",project.getTag().getColor());
            object.put("tasks",jsonArray);
            object.put("tag",object1);
            object.put("user",HomeActivity.getCurrentLoggedInUser().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String url = RetrofitClient.url + "/add_project";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "done: "+response.getString("message"));
                            getAllProject(mContext,dialogg, fragmentManager);
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
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }
    public static void getAllProject(Context mContext){

        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, RetrofitClient.url + "/all_projects/"+ HomeActivity.getCurrentLoggedInUser().getId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            projects=new ArrayList<Project>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("projects");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ArrayList<Task> tasks=new ArrayList<Task>();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                JSONObject jsonProject = jsonArray.getJSONObject(i);
                                JSONArray jsonTasks=jsonProject.getJSONArray("tasks");
                                for (int j = 0; j < jsonTasks.length(); j++) {
                                    JSONObject jsonTask = jsonTasks.getJSONObject(j);
                                    String d = jsonTask.getString("dateCreation");
                                    Date dateCreation = sdf.parse(d);

                                    String dateStr2="";
                                    String dateStr3="";
                                    Date deadline =new Date();
                                    Date reminder =new Date();
                                    if( jsonTask.has("deadline") &&jsonTask.has("reminder")){
                                        dateStr2 = jsonTask.getString("deadline");
                                        dateStr3 = jsonTask.getString("reminder");
                                        deadline = sdf.parse(dateStr2);
                                        reminder = sdf.parse(dateStr3);
                                    }
                                    Task task=new Task(jsonTask.getString("_id"),
                                            jsonTask.getString("taskName"),
                                            dateCreation,
                                            deadline,
                                            reminder,
                                            jsonTask.getInt("importance"),
                                            jsonTask.getInt("enjoyment"),
                                            jsonTask.getString("note"),
                                            false);
                                    tasks.add(task);
                                }
                                JSONObject tagg=jsonProject.getJSONObject("tag");
                                Tag tag=new Tag(tagg.getString("_id"),tagg.getString("tagName"),tagg.getString("color"));
                                String dateStr = jsonProject.getString("dateCreation");
                                Date dateCreation = sdf.parse(dateStr);
                                Project project=new Project(jsonProject.getString("_id"),
                                        jsonProject.getString("projectName"),
                                        jsonProject.getString("description"),
                                        tasks,
                                        dateCreation,
                                       tag);

                                projects.add(project);
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();

                        }finally {
                            TaskFragment.global =projects;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+RetrofitClient.url + "/all_projects/"+ HomeActivity.getCurrentLoggedInUser().getId());
            }
        });
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }
    public static void getAllProject(final Context mContext, final ProgressDialog dialogg, final FragmentManager fragmentManager){

        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, RetrofitClient.url + "/all_projects/"+ HomeActivity.getCurrentLoggedInUser().getId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            projects=new ArrayList<Project>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("projects");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ArrayList<Task> tasks=new ArrayList<Task>();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                JSONObject jsonProject = jsonArray.getJSONObject(i);
                                JSONArray jsonTasks=jsonProject.getJSONArray("tasks");
                                for (int j = 0; j < jsonTasks.length(); j++) {
                                    JSONObject jsonTask = jsonTasks.getJSONObject(j);
                                    String d = jsonTask.getString("dateCreation");
                                    Date dateCreation = sdf.parse(d);
                                    String dateStr2="";
                                    String dateStr3="";
                                    Date deadline =new Date();
                                    Date reminder =new Date();
                                    if( jsonTask.has("deadline") &&jsonTask.has("reminder")){
                                        dateStr2 = jsonTask.getString("deadline");
                                        dateStr3 = jsonTask.getString("reminder");
                                        deadline = sdf.parse(dateStr2);
                                        reminder = sdf.parse(dateStr3);
                                    }
                                    Task task=new Task(jsonTask.getString("_id"),
                                            jsonTask.getString("taskName"),
                                            dateCreation,
                                            deadline,
                                            reminder,
                                            jsonTask.getInt("importance"),
                                            jsonTask.getInt("enjoyment"),
                                            jsonTask.getString("note"),
                                            false);
                                    tasks.add(task);
                                }
                                JSONObject tagg=jsonProject.getJSONObject("tag");
                                Tag tag=new Tag(tagg.getString("_id"),tagg.getString("tagName"),tagg.getString("color"));
                                String dateStr = jsonProject.getString("dateCreation");
                                Date dateCreation = sdf.parse(dateStr);
                                Project project=new Project(jsonProject.getString("_id"),
                                        jsonProject.getString("projectName"),
                                        jsonProject.getString("description"),
                                        tasks,
                                        dateCreation,
                                        tag);

                                projects.add(project);
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();

                        }finally {
                            TaskFragment.global =projects;
                            dialogg.dismiss();
                            TaskFragment tagFrag = new TaskFragment();
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
                Log.e("TAG", "onResponse: "+RetrofitClient.url + "/all_projects");
            }
        });
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }
    public static void deleteProject(final Context mcontext, String id, final ProgressDialog dialogg, final FragmentManager fragmentManager, ArrayList<Task> tasks){
        final String url=RetrofitClient.url + "/delete_project/"+id;
        if (tasks!=null){
            for (Task row:tasks){
                TaskRepository.deleteTask(mcontext,row);
            }
        }
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("TAG", "done: "+response.getString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    getAllProject(mcontext,dialogg, fragmentManager);
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
