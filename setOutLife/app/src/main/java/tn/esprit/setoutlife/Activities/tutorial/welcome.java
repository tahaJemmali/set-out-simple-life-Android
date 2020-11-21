package tn.esprit.setoutlife.Activities.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tn.esprit.setoutlife.R;

public class welcome extends AppCompatActivity {

    Button go;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        go=findViewById(R.id.buttonGo);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(welcome.this,simple_task.class));
            }
        });
    }
}