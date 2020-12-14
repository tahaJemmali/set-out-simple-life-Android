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

import java.util.ArrayList;

import tn.esprit.setoutlife.Fragments.AddProjectFragment;
import tn.esprit.setoutlife.Fragments.AddTaskFragment;
import tn.esprit.setoutlife.Fragments.TaskFragment;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Retrofit.RetrofitClient;
import tn.esprit.setoutlife.Utils.VolleyInstance;
import tn.esprit.setoutlife.entities.Tag;

public class TagRepository   {
    public static ArrayList<Tag> tags;

    static public void addTag(final Context mContext, Tag tag, final ProgressDialog dialogg, final FragmentManager fragmentManager){
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("tagName",tag.getName());
            object.put("color",tag.getColor());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String url = RetrofitClient.url + "/add_tag";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "done: "+response.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            getAllTags(mContext,dialogg,fragmentManager);
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
     private static void getAllTags(Context mContext, final ProgressDialog dialogg, final FragmentManager fragmentManager) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, RetrofitClient.url + "/all_tags", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tags=new ArrayList<Tag>();
                            String message = response.getString("message");
                            System.out.println(message);
                            JSONArray jsonArray = response.getJSONArray("tags");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                tags.add(new Tag(jsonTag.getString("_id"),jsonTag.getString("tagName"), jsonTag.getString("color")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            AddTaskFragment.global=tags;
                            AddProjectFragment.global=tags;
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

            }
        });
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }
    static public void getAllTags(Context mContext) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, RetrofitClient.url + "/all_tags", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tags=new ArrayList<Tag>();
                            String message = response.getString("message");
                            System.out.println(message);
                            JSONArray jsonArray = response.getJSONArray("tags");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                tags.add(new Tag(jsonTag.getString("_id"),jsonTag.getString("tagName"), jsonTag.getString("color")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            AddTaskFragment.global=tags;
                            AddProjectFragment.global=tags;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }
}
