package tn.esprit.setoutlife.Activities.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tn.esprit.setoutlife.R;

public class project_schema extends AppCompatActivity {
Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_schema);
        next=findViewById(R.id.btnNextProjectSchema);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(project_schema.this,commun_task.class));
            }
        });
    }
}