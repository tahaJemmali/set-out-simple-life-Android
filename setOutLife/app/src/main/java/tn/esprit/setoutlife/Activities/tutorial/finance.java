package tn.esprit.setoutlife.Activities.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Activities.LoginActivity;
import tn.esprit.setoutlife.R;
public class finance extends AppCompatActivity {
Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        start=findViewById(R.id.btnStartFinance);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePreference();
                Intent intent = new Intent(finance.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    void updatePreference(){
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putBoolean(LoginActivity.TUTORIAL, false);
        prefEditor.apply();
    }
}