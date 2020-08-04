package com.example.dairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dairy.model.Content;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNoteActivity extends AppCompatActivity  implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private FloatingActionButton floatingSaveButton;

    private String uid;
    private String title;
    private String description;
    private String randomKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Note");

        init();

        floatingSaveButton.setOnClickListener(this);

    }

    private void init() {
        editTextTitle=findViewById(R.id.editText_title);
        editTextDescription=findViewById(R.id.editText_description);
        floatingSaveButton=findViewById(R.id.floatingSaveButton);

        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference("contents");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingSaveButton:
                saveNote();
                break;
        }
    }

    private void saveNote() {
        if(!validateTitle()| !validateDescription()){
            Toast.makeText(this, "Title and Description must not be empty", Toast.LENGTH_LONG).show();
            return;
        }
        randomKey=databaseReference.push().getKey();
        Content content=new Content(title,description,"","",randomKey);
        content.setCreatedDate();
        databaseReference.child(uid).child(randomKey).setValue(content)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CreateNoteActivity.this, "Data is saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateNoteActivity.this,DashboardActivity.class));
                            finish();
                        }else{
                            Toast.makeText(CreateNoteActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private boolean validateTitle() {
        title=editTextTitle.getText().toString().trim();
        if(title.isEmpty())
            return false;
        return true;
    }
    private boolean validateDescription(){
        description=editTextDescription.getText().toString().trim();
        if(description.isEmpty())
            return false;
        return  true;
    }
}
