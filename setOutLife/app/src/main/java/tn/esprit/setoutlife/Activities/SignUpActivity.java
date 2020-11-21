package tn.esprit.setoutlife.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Retrofit.INodeJsService;
import tn.esprit.setoutlife.Retrofit.RetrofitClient;

public class SignUpActivity extends AppCompatActivity {

    ScrollView scrollView;
    Button signUpBtn;

    EditText edtFistName,edtLastName,edtEmail,edtPassword,edtConfirmPassword,edtAddress,edtPhone;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    INodeJsService iNodeJsService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Init NodeJs Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iNodeJsService = retrofitClient.create(INodeJsService.class);

        //InitUI
        initUI();

    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    public void initUI(){
        scrollView  = findViewById(R.id.scrollView);
        signUpBtn  = findViewById(R.id.signUpBtn);

        edtFistName  = findViewById(R.id.edtFistName);
        edtLastName  = findViewById(R.id.edtLastName);
        edtEmail  = findViewById(R.id.edtEmail);
        edtPassword  = findViewById(R.id.edtPassword);
        edtConfirmPassword  = findViewById(R.id.edtConfirmPassword);
        edtAddress  = findViewById(R.id.edtAddress);
        edtPhone  = findViewById(R.id.edtPhone);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(edtFistName.getText().toString(),
                        edtLastName.getText().toString(),
                        edtEmail.getText().toString(),
                        edtPassword.getText().toString(),
                        edtConfirmPassword.getText().toString(),
                        edtAddress.getText().toString(),
                        edtPhone.getText().toString());
            }
        });



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
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    void registerUser(String firstName,String lastName,String email,String password,String confirmPassword,String address,String phone){
        if (TextUtils.isEmpty(firstName))
        {
            Toast.makeText(SignUpActivity.this,"First name cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(lastName))
        {
            Toast.makeText(SignUpActivity.this,"Last name cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(SignUpActivity.this,"Email cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(SignUpActivity.this,"Password  cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(SignUpActivity.this,"Password  cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }


        //register
        try {
            compositeDisposable.add(iNodeJsService.registerUser(firstName,lastName,email,password,address,phone)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() { @Override public void accept(String response) throws Exception {
                                   Toast.makeText(SignUpActivity.this,""+response,Toast.LENGTH_SHORT).show();
                                   if (!response.contains("Email already exists") && !response.contains("Phone number already exists")) {
                                       Intent intent = new Intent();
                                       setResult(RESULT_OK, intent);
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


    }

}