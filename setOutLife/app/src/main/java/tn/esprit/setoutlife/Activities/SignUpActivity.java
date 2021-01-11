package tn.esprit.setoutlife.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tn.esprit.setoutlife.Activities.Settings.CustomSettings.CustomSettingsObjectEditText;
import tn.esprit.setoutlife.Activities.Settings.PersonalInfo.EmailActivity;
import tn.esprit.setoutlife.Activities.Settings.PersonalInfo.HomeTownActivity;
import tn.esprit.setoutlife.Activities.Settings.PersonalInfo.NameActivity;
import tn.esprit.setoutlife.Activities.Settings.PersonalInfo.PhoneActivity;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.IRepository;
import tn.esprit.setoutlife.Repository.UserRepository;
import tn.esprit.setoutlife.entities.User;

public class SignUpActivity extends AppCompatActivity implements IRepository {

    ScrollView scrollView;
    Button signUpBtn;

    EditText edtFistName,edtLastName,edtEmail,edtPassword,edtConfirmPassword,edtAddress,edtPhone;

    //CompositeDisposable compositeDisposable = new CompositeDisposable();
    //INodeJsService iNodeJsService;


    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Init NodeJs Service
        //Retrofit retrofitClient = RetrofitClient.getInstance();
        //iNodeJsService = retrofitClient.create(INodeJsService.class);

        intent = new Intent();

        UserRepository.getInstance().setiRepository(this);
        //InitUI
        initUI();

    }

    @Override
    public void onBackPressed() {
        setResult(0, intent);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        //compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //compositeDisposable.clear();
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
        edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);


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



        /*if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    void registerUser(String firstName,String lastName,String email,String password,String confirmPassword,String address,String phone){
        String namePattern = "^[\\p{L} .'-]+$";

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

        if (!firstName.matches(namePattern))
        {
            Toast.makeText(SignUpActivity.this,"Invalid first name!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!lastName.matches(namePattern))
        {
            Toast.makeText(SignUpActivity.this,"Invalid last name!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(SignUpActivity.this,"Email cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z][a-z]+";
        if (!email.matches(emailPattern))
        {
            Toast.makeText(SignUpActivity.this,"Invalid email address!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(SignUpActivity.this,"Password  cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length()<8)
        {
            Toast.makeText(SignUpActivity.this,"Password must have at least 8 characters!",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(SignUpActivity.this,"Password  cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(address)){
            address="Not mentioned";
        } else if (!address.matches(namePattern))
        {
            Toast.makeText(SignUpActivity.this,"Invalid home town!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone)){
            phone="Not mentioned";
        }else if (phone.length()!=8)
        {
            Toast.makeText(SignUpActivity.this,"Invalid phone number!",Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAddress(address);
        user.setPhone(phone);
        user.setPassword(password);

        //register
        UserRepository.getInstance().register(user,this);

    }

    @Override
    public void showLoadingButton() {

    }

    @Override
    public void doAction() {
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void doAction2() {
        //
    }
}