package tn.esprit.setoutsimplelife.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tn.esprit.setoutsimplelife.R;
public class commun_task extends AppCompatActivity {
Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commun_task);
        next=findViewById(R.id.btnNextCommun);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(commun_task.this,gain.class));
            }
        });
    }
}