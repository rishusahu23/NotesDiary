package com.example.dairy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Main");

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser==null){
            //SEND TO LOGIN ACTIVITY
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        else{
            //SEND TO DASHBOARD ACTIVITY
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
        }
    }
}
