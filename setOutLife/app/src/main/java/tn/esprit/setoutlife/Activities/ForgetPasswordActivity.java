package tn.esprit.setoutlife.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.IRepository;
import tn.esprit.setoutlife.Repository.UserRepository;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

public class ForgetPasswordActivity extends AppCompatActivity implements IRepository {

    ScrollView scrollView;
    Button nextBtn;
    EditText edtEmail;
    ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        UserRepository.getInstance().setiRepository(this);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserRepository.getInstance().setiRepository(this);
    }

    public void initUI(){
        pb = new ProgressDialog(this);
        scrollView  = findViewById(R.id.scrollView);
        nextBtn  = findViewById(R.id.nextBtn);
        edtEmail  = findViewById(R.id.edtEmail);
        edtEmail.setText("fahd.larayedh@esprit.tn");
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtEmail.getText().toString().equals("")){
                    sendCode(edtEmail.getText().toString());
                }
                else
                    Toast.makeText(ForgetPasswordActivity.this,"Email can't be empty",Toast.LENGTH_SHORT).show();
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
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            w.setStatusBarColor(Color.BLACK);
        }*/
    }

    private void sendCode(String email) {
        pb.setMessage("");
        pb.show();
        UserRepository.getInstance().passwordRecovery(email,this);
    }

    @Override
    public void showLoadingButton() {
        pb.dismiss();
    }

    @Override
    public void doAction() {
        pb.dismiss();
        Intent intent = new Intent(ForgetPasswordActivity.this, VerifyCodeActivity.class);
        startActivity(intent);
    }

    @Override
    public void doAction2() {
        //
    }
}