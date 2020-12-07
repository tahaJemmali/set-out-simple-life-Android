package tn.esprit.setoutlife.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fraggjkee.smsconfirmationview.SmsConfirmationView;

import tn.esprit.setoutlife.R;

public class VerifyCodeActivity extends AppCompatActivity {

    public static String code;

    SmsConfirmationView code_view;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        initUI();
    }

    public void initUI(){


        code_view = findViewById(R.id.code_view);
        nextBtn = findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //System.out.println(code_view.getEnteredCode());
                //System.out.println("recieved code:" +code);

                if (code_view.getEnteredCode().toString().equals(code)){
                    Intent intent = new Intent(VerifyCodeActivity.this, ResetPasswordActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(VerifyCodeActivity.this,"Wrong verification code",Toast.LENGTH_SHORT).show();
                }

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }
    }
}