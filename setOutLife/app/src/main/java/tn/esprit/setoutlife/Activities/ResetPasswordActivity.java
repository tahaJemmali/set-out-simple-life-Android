package tn.esprit.setoutlife.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.IRepository;
import tn.esprit.setoutlife.Repository.UserRepository;
import tn.esprit.setoutlife.entities.User;

public class ResetPasswordActivity extends AppCompatActivity implements IRepository {

    ScrollView scrollView;
    EditText edtConfirmPassword,edtPassword;
    Button confirmBtn;
    public static String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        UserRepository.getInstance().setiRepository(this);
        initUI();
    }

    public void initUI(){
        scrollView  = findViewById(R.id.scrollView);
        edtConfirmPassword  = findViewById(R.id.edtConfirmPassword);
        edtPassword  = findViewById(R.id.edtPassword);
        confirmBtn  = findViewById(R.id.confirmBtn);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (edtPassword.getText().toString().equals("") || edtConfirmPassword.getText().toString().equals("")){
                        Toast.makeText(ResetPasswordActivity.this,"Password can't be empty",Toast.LENGTH_SHORT).show();
                    }else if (edtPassword.getText().toString().length()<8){
                        Toast.makeText(ResetPasswordActivity.this,"Passwords must be at least 8 characters",Toast.LENGTH_SHORT).show();
                    }
                    else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())){
                        Toast.makeText(ResetPasswordActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                    }else{
                        User user = new User();
                        user.setEmail(email);
                        user.setPassword(edtPassword.getText().toString());
                        UserRepository.getInstance().resetPassword(user,ResetPasswordActivity.this);
                    }
            }
        });
    }

    @Override
    public void showLoadingButton() {

    }

    @Override
    public void doAction() {
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void doAction2() {
        //
    }
}