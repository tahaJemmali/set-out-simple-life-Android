package tn.esprit.setoutlife.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import tn.esprit.setoutlife.R;

import tn.esprit.setoutlife.entities.User;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kusu.loadingbutton.LoadingButton;

import org.json.JSONException;
import org.json.JSONObject;

import tn.esprit.setoutlife.Activities.tutorial.welcome;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.IRepository;
import tn.esprit.setoutlife.Repository.UserRepository;
import tn.esprit.setoutlife.entities.User;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class LoginActivity extends AppCompatActivity implements IRepository {

    //facebook
    CallbackManager callbackManager;
    LoginButton loginFacebookButton;

    ScrollView scrollView;
    Button signUpBtn,customFacebookBtn;
    LoadingButton loginBtn;
    EditText edtEmail,edtPassword;
    CheckBox cbRememberMe;
    TextView forgetPasswordBtn;


    String email ;
    String password;
    boolean cbState ;
    boolean DoTutorial;

    public static final String SHARED_PREFS = "SharedPrefsFile" ;

    public static final String EMAIL = "email" ;
    public static final String PASSWORD = "password" ;
    public static final String CHECKBOX = "cbRememberMe" ;
    public static final String TUTORIAL = "tutorial" ;

    //CompositeDisposable compositeDisposable = new CompositeDisposable();
    //INodeJsService iNodeJsService;

    private static final int REQUEST_CODE = 6;
    private static final int REQUEST_CODE_RETURN = 0;

    @Override
    protected void onStop() {
        //compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (isLoggedIn()){
            GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback(){
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response){
                            try {
                                User user = new User();
                                user.setLastName(object.getString("last_name"));
                                user.setFirstName(object.getString("first_name"));
                                user.setEmail(object.getString("email"));
                                String id = object.getString("id");
                                user.setSigned_up_with("Facebook");

                                Date date = null;
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                    date = format.parse((object.getString("birthday")));
                                    user.setBirth_date(date);
                                    //user.setSign_up_date(date);
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }

                                Date date2 = null;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                                String currentDateandTime = sdf.format(new Date());
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
                                    date2 = format.parse(currentDateandTime);
                                    user.setSign_up_date(date2);
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }

                                user.setAddress("Not mentioned");
                                user.setPhone("Not mentioned");
                                user.setScore(0);

                                user.setPhoto("https://graph.facebook.com/"+id+"/"+"picture?height=1024&access token="+AccessToken.getCurrentAccessToken().getToken());

                                UserRepository.getInstance().facebookLogin(user,LoginActivity.this);


                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle bundle = new Bundle();
            bundle.putString("fields","gender,name,id,first_name,last_name,email,birthday,location,hometown,locale");

            graphRequest.setParameters(bundle);
            graphRequest.executeAsync();
        }

        //facebook
        initFacebook();

        //Init NodeJs Service
        //Retrofit retrofitClient = RetrofitClient.getInstance();
        //iNodeJsService = retrofitClient.create(INodeJsService.class);

        UserRepository.getInstance().setiRepository(this);

        //InitUI
        initUI();

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void onClickFacebookButton(View view) {
        if (view == customFacebookBtn) {
            loginFacebookButton.performClick();
        }
    }

    private void initFacebook() {

        //Our custom Facebook button
        customFacebookBtn = (Button) findViewById(R.id.customFacebookBtn);

        callbackManager = CallbackManager.Factory.create();
        loginFacebookButton = findViewById(R.id.login_facebook_button);

        callbackManager = CallbackManager.Factory.create();
        loginFacebookButton.setPermissions(Arrays.asList("user_hometown,user_location"));
        loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("Demo facebook", "login success");
                //register

                //login

                //open home activity
                //loadUserFromJson(email);
            }

            @Override
            public void onCancel() {
                Log.e("Demo facebook", "login canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Demo facebook", "login error");
            }
        });
    }

    public void initUI(){
        //Log.e("Testing", ":"+getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(EMAIL, ""));
        //Log.e("Testing", ":"+getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(PASSWORD, ""));
        //Log.e("Testing", ":"+getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getBoolean(CHECKBOX, false) );

        scrollView  = findViewById(R.id.scrollView);
        signUpBtn = findViewById(R.id.signUpBtn);
        loginBtn = findViewById(R.id.loginBtn);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        forgetPasswordBtn = findViewById(R.id.forgetPasswordBtn);


        loadPreference();
        updatePreference();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check les donnees sont pas vide
                if (!edtEmail.getText().toString().equals("") && !edtPassword.getText().toString().equals("")){
                        loginUser(edtEmail.getText().toString(),edtPassword.getText().toString());
                }else{

                    Toast.makeText(LoginActivity.this,"Invalid Email or Password",Toast.LENGTH_LONG).show();
                }
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
           // w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }

    void loginUser(final String email,final String password){

        UserRepository.getInstance().login(email,password,this);
        savePreference();
    }

    public void loadPreference(){
        email = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(EMAIL, "");
        password = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(PASSWORD, "");
        cbState = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getBoolean(CHECKBOX, false);
        DoTutorial= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getBoolean(TUTORIAL, true);
    }

    public void updatePreference() {
        if (cbState) {
            edtEmail.setText(email);
            edtPassword.setText(password);
            cbRememberMe.setChecked(cbState);
        }
    }

    public void savePreference(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        if (cbRememberMe.isChecked()) {
            prefEditor.putString(EMAIL, edtEmail.getText().toString());
            prefEditor.putString(PASSWORD, edtPassword.getText().toString());
            prefEditor.putBoolean(CHECKBOX, cbRememberMe.isChecked());
            prefEditor.apply();
            //apply shyncrone & commit ashyncrone
            return;
        }
        clearData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserRepository.getInstance().setiRepository(this);
    }

    public void clearData(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //System.out.println("here here" + resultCode);

        if (resultCode == REQUEST_CODE_RETURN){
            return;
        }
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

        //Sign Up result
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Toast.makeText(LoginActivity.this,"Done, Welcome !",Toast.LENGTH_LONG).show();
                return;
            }
        }

        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response){
                    Log.e("Demo facebook",object.toString());
                    try {
                        User user = new User();
                        user.setLastName(object.getString("last_name"));
                        user.setFirstName(object.getString("first_name"));
                        //user.setEmail(object.getString("email"));
                        user.setEmail("tahajammali@esprit.tn");
                        String id = object.getString("id");
                        user.setSigned_up_with("Facebook");
                        //System.out.println(object.getString("birthday"));
                        //System.out.println(object.getString("hometown"));

                            Date date = null;
                            if(object.has("birthday")){
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                    date = format.parse((object.getString("birthday")));
                                    user.setBirth_date(date);
                                    //user.setSign_up_date(date);
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }
                            }


                        Date date2 = null;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                        String currentDateandTime = sdf.format(new Date());
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
                            date2 = format.parse(currentDateandTime);
                            user.setSign_up_date(date2);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }

                        user.setAddress("Not mentioned");
                        user.setPhone("Not mentioned");
                        user.setScore(0);

                        //https://graph.facebook.com/10214899562601635/picture?height=1024&access_token=

                        //String a = "https:"+"\\"+"/"+"\\"+"/"+"graph.facebook.com"+"\\"+"/";

                        // meth url
                        /*
                        String a = "https:"+"%2F"+"%2F"+"graph.facebook.com"+"%2F";
                        user.setPhoto(a+id+"%2F"+"picture%3Fheight%3D1024%26access%20token\t%3D"+AccessToken.getCurrentAccessToken().getToken());
                        */
                        user.setPhoto("https://graph.facebook.com/"+id+"/"+"picture?height=1024&access token="+AccessToken.getCurrentAccessToken().getToken());
                        //System.out.println(user.getPhoto());
                        //user.setPhoto(/*a+id+*/"\\"+"/"+"picture?height=1024&access token="+AccessToken.getCurrentAccessToken().getToken());

                        //Picasso.get().load("https://graph.facebook.com/"+ id + "/picture?type=small").into(); // large medium small
                        //System.out.println(AccessToken.getCurrentAccessToken().getToken());
                        //https://graph.facebook.com/10214899562601635/picture?type=small&access_token=


                        UserRepository.getInstance().facebookLogin(user,LoginActivity.this);

                        //user.setPhoto("https://graph.facebook.com/"+id+"/picture?height=1024&access_token="+AccessToken.getCurrentAccessToken().getToken());

                        //HomeActivity.setCurrentLoggedInUser(user);

/*
                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        if(DoTutorial){
                            intent = new Intent(LoginActivity.this, welcome.class);
                        }
                        startActivity(intent);
                        finish();
*/
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("fields","gender,name,id,first_name,last_name,email,birthday,location,hometown,locale");

        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();

    }

    //AccessToken Tracker / facebook
    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null){
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.startTracking();
    }


    @Override
    public void showLoadingButton() {
        loginBtn.showLoading();
    }

    @Override
    public void doAction() {
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
       if(DoTutorial){
        intent = new Intent(LoginActivity.this, welcome.class);
       }
       startActivity(intent);
       finish();
    }

}