package tn.esprit.setoutlife.Repository;


import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Activities.Settings.CustomSettings.CustomSettingsObjectEditText;
import tn.esprit.setoutlife.Activities.VerifyCodeActivity;
import tn.esprit.setoutlife.Activities.ResetPasswordActivity;
import tn.esprit.setoutlife.Utils.VolleyInstance;
import tn.esprit.setoutlife.entities.User;

public class UserRepository {

    private static UserRepository instance;

   // private String baseURL = "http://10.0.2.2:3000";
    private String baseURL = "https://set-out.herokuapp.com";

    private IRepository iRepository;

    public static UserRepository getInstance() {
        if (instance==null)
            instance = new UserRepository();
        return instance;
    }

    public void setiRepository(IRepository iRepository){
        this.iRepository = iRepository;
    }

    public void login(final String email,String password,final Context context){

        String url = baseURL+"/login"/*+"/"+email+"/"+password*/;

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("email",email);
            object.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                            System.out.println("le test :");
                            System.out.println(message.equals("Login success"));
                            System.out.println("Login success");
                            System.out.println(message);

                            if (message.equals("Login success")){
                                iRepository.showLoadingButton();
                                loadUserFromJson(email,context);
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

    private void loadUserFromJson(String email, Context context) {
        String url = baseURL+"/getUser"+"/"+email;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            System.out.println(message);
                            JSONObject jsonObject = response.getJSONObject("user");
                            System.out.println(jsonObject);

                            User user = new User();

                                user.setFirstName(jsonObject.getString("firstName"));
                                user.setLastName(jsonObject.getString("lastName"));
                                user.setEmail(jsonObject.getString("email"));


                                user.setAddress(jsonObject.getString("address"));
                                user.setPhone(jsonObject.getString("phone"));
                                user.setPhoto(jsonObject.getString("photo"));

                                user.setScore(jsonObject.getInt("score"));

                                user.setSign_up_date(getDate(jsonObject.getString("sign_up_date")));
                                if (jsonObject.has("birth_date")){
                                    user.setBirth_date(getDate(jsonObject.getString("birth_date")));
                                }
                                user.setSigned_up_with(jsonObject.getString("signed_up_with"));


                            HomeActivity.setCurrentLoggedInUser(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{
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
    }

    public void register(User u, final Context context){

        String url = baseURL+"/register";

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("firstName",u.getFirstName());
            object.put("lastName",u.getLastName());
            object.put("email",u.getEmail());
            object.put("password",u.getPassword());
            object.put("address",u.getAddress());
            object.put("phone",u.getPhone());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                             Toast.makeText(context,""+message,Toast.LENGTH_SHORT).show();
                             /*if (!message.equals("Email already exists") && !message.equals("Phone number already exists")){
                                iUserRepository.doRegister();
                             }*/
                            iRepository.doAction();
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

    public void updateEmailUser(String old_email, String new_email, String password, final Context context ){
        String url = baseURL+"/updateUserEmail";

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("oldEmail",old_email);
            object.put("newEmail",new_email);
            object.put("password",password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            System.out.println(message);
                            switch (message){
                                case "Wrong password":
                                    Toast.makeText(context,message+"",Toast.LENGTH_SHORT).show();
                                    break;
                                case "Email already exists":
                                    Toast.makeText(context,message+"",Toast.LENGTH_SHORT).show();
                                    break;
                                case "Email updated":
                                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                                    HomeActivity.getCurrentLoggedInUser().setEmail(CustomSettingsObjectEditText.emailText);
                                    iRepository.doAction();
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{

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

    public void facebookLogin(final User u,final Context context) {
        String url = baseURL+"/LoginWithFacebook";

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("firstName",u.getFirstName());
            object.put("lastName",u.getLastName());
            object.put("email",u.getEmail());
            object.put("signedUpWith",u.getSigned_up_with());
            object.put("birthDate",u.getBirth_date());
            object.put("photo",u.getPhoto());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            System.out.println(message);
                            switch (message){
                                case "Registred with facebook already":
                                    //Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                                    iRepository.showLoadingButton();
                                    loadUserFromJson(u.getEmail(),context);
                                    //HomeActivity.setCurrentLoggedInUser(u);
                                    break;
                                case "new facebook account":
                                    //Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                                    //HomeActivity.getCurrentLoggedInUser().setEmail(CustomSettingsObjectEditText.emailText);
                                    //iUserRepository.doAction();
                                    HomeActivity.setCurrentLoggedInUser(u);
                                    //loadUserFromJson(u.getEmail(),context);
                                    iRepository.doAction();
                                    break;
                                default:
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{
                            //iUserRepository.doAction();
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

    public void passwordRecovery(String email,final Context context) {
        String url = baseURL+"/passwordRecovery";

        JSONObject object = new JSONObject();
        try {
            object.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");

                            switch (message){
                                case "verification code":
                                    VerifyCodeActivity.code = response.getString("code");
                                    ResetPasswordActivity.email = response.getString("email");
                                        iRepository.doAction();
                                    break;
                                case "Email does not exist":
                                    iRepository.showLoadingButton();
                                    Toast.makeText(context,"Email does not exist",Toast.LENGTH_SHORT).show();
                                    break;
                                case "Please login with your facebook account":
                                    iRepository.showLoadingButton();
                                    Toast.makeText(context,"Please login with your facebook account",Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{
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

    public void resetPassword(User user, final Context context) {
        String url = baseURL+"/passwordReset";

        JSONObject object = new JSONObject();
        try {
            object.put("email",user.getEmail());
            object.put("password",user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");

                            switch (message){
                                case "Password has been reset":
                                    Toast.makeText(context,"Your password has been reset",Toast.LENGTH_SHORT).show();
                                    iRepository.doAction();
                                    break;
                                case "Error":
                                    Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{
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

    public void updateUser(User user,final Context context) {
        String url = baseURL+"/updateUser";

        JSONObject object = new JSONObject();
        try {
            if (user.getBirth_date()!=null) object.put("birth_date",user.getBirth_date());
            if (user.getFirstName()!=null) object.put("firstName",user.getFirstName());
            if (user.getLastName()!=null) object.put("lastName",user.getLastName());
            if (user.getEmail()!=null) object.put("user_email",user.getEmail());
            if (user.getAddress()!=null) object.put("address",user.getAddress());
            if (user.getPhone()!=null) object.put("phone",user.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            switch (message){
                                case "Done":
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{
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
}
