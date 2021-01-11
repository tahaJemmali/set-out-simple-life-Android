package tn.esprit.setoutlife.Repository;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import tn.esprit.setoutlife.Activities.Forum.CommentsActivity;
import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Utils.VolleyInstance;
import tn.esprit.setoutlife.entities.Comment;
import tn.esprit.setoutlife.entities.Post;
import tn.esprit.setoutlife.entities.User;

public class PostRepository {

    private static PostRepository instance;
    //private String baseURL = "http://10.0.2.2:3000";
    //private String baseURL = "https://set-out.herokuapp.com";
    private String baseURL = "https://setoutfahd.herokuapp.com";

    private IRepository iRepository;
    ArrayList<Post> postArrayList;
    ArrayList<Comment> commentArrayList;

    public static PostRepository getInstance() {
        if (instance==null)
            instance = new PostRepository();
        return instance;
    }

    public void setiRepository(IRepository iRepository){
        this.iRepository = iRepository;
    }

    public void addPost(Post post, final Context context){

        String url = baseURL+"/addPost";

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("user_id", post.getUser().getEmail());
            object.put("title",post.getTitle());
            object.put("description",post.getDescription());
            object.put("image",post.getImage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            if (message.contains("post added")){
                                Toast.makeText(context,""+message,Toast.LENGTH_SHORT).show();
                                iRepository.doAction();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }

    public ArrayList<Post> getPosts(Context context) {
        String url = baseURL+"/getPosts";
        postArrayList = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            //System.out.println(message);
                            JSONArray jsonArray = response.getJSONArray("posts");
                            //System.out.println(jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonPost = jsonArray.getJSONObject(i);
                                Post post = new Post();
                                post.setId(jsonPost.getString("_id"));
                                post.setTitle(jsonPost.getString("title"));
                                post.setDescription(jsonPost.getString("description"));
                                post.setImage(jsonPost.getString("image"));
                                post.setPostDate(getDate(jsonPost.getString("post_date")));
                                String jsonObject =jsonPost.getString("postedBy");

                                String jsonLikedByArray = jsonPost.getString("likedBy");
                                //if (jsonLikedByArray!=null && !jsonLikedByArray.equals("") && !jsonLikedByArray.equals("[]")){
                                    ArrayList<User> likedBy = new ArrayList<>();
                                    try{
                                        JSONArray array = new JSONArray(jsonLikedByArray);
                                        for (int j=0;j<jsonLikedByArray.length();j++){
                                            JSONObject jsonObject1 = array.getJSONObject(j);
                                            User user1 = new User();
                                            user1.setPhoto(jsonObject1.getString("photo"));
                                            user1.setEmail(jsonObject1.getString("email"));
                                            user1.setFirstName(jsonObject1.getString("firstName"));
                                            user1.setLastName(jsonObject1.getString("lastName"));
                                            likedBy.add(user1);
                                        }
                                    }catch (Throwable tx) {
                                       // Log.e("My App", "Could not parse malformed JSON: \"" + jsonLikedByArray + "\"");
                                    }
                                    post.setLikedBy(likedBy);


                                String jsonCommentsArray = jsonPost.getString("comments");
                                ArrayList<Comment> comments = new ArrayList<>();
                                try{
                                    JSONArray array = new JSONArray(jsonCommentsArray);
                                    for (int j=0;j<jsonCommentsArray.length();j++){
                                        JSONObject jsonObject1 = array.getJSONObject(j);
                                        Comment comment = new Comment();
                                        //comment.setUser(jsonObject1.getString("user"));

                                        JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("user"));
                                        User user = new User();

                                        user.setFirstName(jsonObject2.getString("firstName"));

                                        user.setLastName(jsonObject2.getString("lastName"));
                                        user.setEmail(jsonObject2.getString("email"));
                                        user.setPhoto(jsonObject2.getString("photo"));

                                        comment.setUser(user);
                                        comment.setText(jsonObject1.getString("text"));
                                        comment.setComment_date(getDate(jsonObject1.getString("comment_date")));
                                        comments.add(comment);
                                    }
                                }catch (Throwable tx) {
                                    // Log.e("My App", "Could not parse malformed JSON: \"" + jsonLikedByArray + "\"");
                                }
                                post.setComments(comments);




                                User user = new User();
                                try {
                                    JSONObject obj = new JSONObject(jsonObject);
                                    user.setFirstName(obj.getString("firstName"));
                                    user.setLastName(obj.getString("lastName"));
                                    user.setEmail(obj.getString("email"));
                                    user.setPhoto(obj.getString("photo"));
                                } catch (Throwable tx) {
                                   // Log.e("My App", "Could not parse malformed JSON: \"" + jsonObject + "\"");
                                }
                                post.setUser(user);
                                postArrayList.add(post);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{
                            // refresh list
                            iRepository.doAction();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
        return postArrayList;
    }

    public Date getDate(String key){
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
            date = format.parse(key);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void likePost(Post post, User currentLoggedInUser,final Context context) {
        String url = baseURL+"/likePost";
        JSONObject object = new JSONObject();
        try {
            object.put("post_id", post.getId());
            object.put("user_liked_email",currentLoggedInUser.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            if (message.contains("post liked")){
                                Toast.makeText(context,""+message,Toast.LENGTH_SHORT).show();
                                //iRepository.doAction();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }

    public void unLikePost(Post post, User currentLoggedInUser,final Context context) {
        String url = baseURL+"/unLikePost";
        JSONObject object = new JSONObject();
        try {
            object.put("post_id", post.getId());
            object.put("user_unLiked_email",currentLoggedInUser.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            if (message.contains("post unLiked")){
                                Toast.makeText(context,""+message,Toast.LENGTH_SHORT).show();
                                //iRepository.doAction();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }

    public ArrayList<Comment> getComments(String post_id, Context context) {
        String url = baseURL+"/getComments/"+post_id;
        commentArrayList = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            System.out.println(message);
                            JSONArray jsonArray = response.getJSONArray("comments");
                            System.out.println(jsonArray);

                            try{
                                for (int j=0;j<jsonArray.length();j++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                    Comment comment = new Comment();

                                    JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("user"));
                                    User user = new User();

                                    user.setFirstName(jsonObject2.getString("firstName"));
                                    user.setLastName(jsonObject2.getString("lastName"));
                                    user.setEmail(jsonObject2.getString("email"));
                                    user.setPhoto(jsonObject2.getString("photo"));

                                    comment.setUser(user);
                                    comment.setText(jsonObject1.getString("text"));
                                    comment.setComment_date(getDate(jsonObject1.getString("comment_date")));

                                    /*System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
                                    System.out.println(comment.getUser().getEmail());
                                    System.out.println(comment.getText());
                                    System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");*/

                                    commentArrayList.add(comment);
                                }
                            }catch (Throwable tx) {
                                // Log.e("My App", "Could not parse malformed JSON: \"" + jsonLikedByArray + "\"");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{
                            // refresh list
                            iRepository.doAction();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
        return commentArrayList;
    }
}
