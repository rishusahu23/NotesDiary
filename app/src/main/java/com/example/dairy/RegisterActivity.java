package com.example.dairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private  String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private Button registerButton;
    private TextView loginTextview;

    private String email;
    private String password;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");

        init();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        loginTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogin();
            }
        });
    }

    private void sendToLogin() {
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
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

    private void register() {
        if(!validateEmail()|!validatePassword())
            return;
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //SEND USER TO LOGIN ACTIVITY
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void init() {
        textInputEmail=findViewById(R.id.text_input_email);
        textInputPassword=findViewById(R.id.text_input_password);
        registerButton=findViewById(R.id.button_register);
        loginTextview=findViewById(R.id.login_textview);

        mAuth=FirebaseAuth.getInstance();
    }
}
