package com.example.dairy.HelperActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dairy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private TextInputLayout textInputEmail;
    private TextView goBackTextView;
    private Button resetButton;

    private String email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        init();
        goBackTextView.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    private void init() {
        textInputEmail=findViewById(R.id.text_input_email);
        resetButton=findViewById(R.id.reset_password_button);
        goBackTextView=findViewById(R.id.go_back_button);

        firebaseAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_back_button:
                finish();
                break;
            case R.id.reset_password_button:
                reset();
                break;
        }
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

    private void reset() {
        if(!validateEmail()){
            return;
        }
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPassword.this, "reset link is send to your email", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ResetPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

