package tn.esprit.setoutlife.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import tn.esprit.setoutlife.Activities.tutorial.welcome;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Retrofit.INodeJsService;
import tn.esprit.setoutlife.Retrofit.RetrofitClient;

public class LoginActivity extends AppCompatActivity {

    ScrollView scrollView;
    Button signUpBtn,loginBtn;
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

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    INodeJsService iNodeJsService;

    private static final int REQUEST_CODE = 6;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init NodeJs Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iNodeJsService = retrofitClient.create(INodeJsService.class);

        //InitUI
        initUI();
    }

    public void initUI(){

        Log.e("Testing", ":"+getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(EMAIL, ""));
        Log.e("Testing", ":"+getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(PASSWORD, ""));
        Log.e("Testing", ":"+getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getBoolean(CHECKBOX, false) );

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

/*
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            scrollView.setOnTouchListener( new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        } else {
            scrollView.setOnTouchListener(null);
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
           // w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }

    void loginUser(String email,String password){
        try {
            compositeDisposable.add(iNodeJsService.loginUser(email,password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() { @Override public void accept(String response) throws Exception {
                                   Toast.makeText(LoginActivity.this,""+response,Toast.LENGTH_SHORT).show();
                                   if (response.contains("Login success")){
                                       Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                       if(DoTutorial){
                                           intent = new Intent(LoginActivity.this, welcome.class);
                                       }
                                       startActivity(intent);
                                       finish();
                                   }
                               } },
                            new Consumer<Throwable>() { @Override public void accept(Throwable throwable) throws Exception {
                                System.out.println(throwable.getMessage());
                            } }));
        }
        catch (Exception e){
            e.printStackTrace();
        }

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

    public void clearData(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Toast.makeText(LoginActivity.this,"Done, Welcome !",Toast.LENGTH_LONG).show();
            }
        }
    }
}