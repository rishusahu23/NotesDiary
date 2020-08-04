package com.example.dairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dairy.HelperActivity.ResetPassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private Button loginButton;
    private TextView registerTextview;
    private TextView forgotPasswordTextView;

    private String email;
    private String password;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        init();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToRegiter();
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPassword.class));

            }
        });

    }

    private void sendToRegiter() {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        finish();
    }

    private boolean validateEmail(){
        email=textInputEmail.getEditText().getText().toString().trim();

        if(email.isEmpty()){
            textInputEmail.setError("Field can't be empty ");
            return false;
        }
        if(!email.matches((emailPattern))){
            textInputEmail.setError("enter correct email");
            return  false;
        }
        textInputEmail.setError(null);
        return true;
    }

    private boolean validatePassword(){
        password=textInputPassword.getEditText().getText().toString().trim();
        if(password.length()<6){
            textInputPassword.setError("Password can'n be less than 6 character");
            return  false;
        }
        textInputPassword.setError(null);
        return true;
    }

    private void login() {
        if(!validateEmail()|!validatePassword())
            return;
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //SEND TO DASHBOARD
                            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                            Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void init() {
        textInputEmail=findViewById(R.id.text_input_email);
        textInputPassword=findViewById(R.id.text_input_password);
        loginButton=findViewById(R.id.button_login);
        registerTextview=findViewById(R.id.register_textview);
        forgotPasswordTextView=findViewById(R.id.forgot_password_textview);
        mAuth=FirebaseAuth.getInstance();
    }
}
