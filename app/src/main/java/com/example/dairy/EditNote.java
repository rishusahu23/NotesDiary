package com.example.dairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dairy.model.Content;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.Locale;

public class EditNote extends AppCompatActivity implements View.OnClickListener {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private TextToSpeech mTTS;

    private FloatingActionButton saveButton;
    private FloatingActionButton shareButton;
    private FloatingActionButton deleteButton;
    private FloatingActionButton speechButton;

    private String title;
    private String description;
    private String randomKey;
    private String createdDate;
    private String editedDate;

    private String uid;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        mTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int res=mTTS.setLanguage(Locale.ENGLISH);
                    if(res==TextToSpeech.LANG_MISSING_DATA || res==TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(EditNote.this, "Language not supported", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(EditNote.this, "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        init();
        initialiseApp();
        saveButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        speechButton.setOnClickListener(this);
    }

    private void initialiseApp() {
        Intent intent=getIntent();
        randomKey=intent.getStringExtra("RANDOM_KEY");
        databaseReference.child(uid).child(randomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    title=titleEditText.getText().toString().trim();
                    description=descriptionEditText.getText().toString().trim();
                    createdDate=dataSnapshot.child("createdDate").getValue().toString();
                    titleEditText.setText(dataSnapshot.child("title").getValue().toString());
                    descriptionEditText.setText(dataSnapshot.child("description").getValue().toString());
                    editedDate=dataSnapshot.child("editedDate").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        titleEditText=findViewById(R.id.title_editText);
        descriptionEditText=findViewById(R.id.description_editText);

        saveButton=findViewById(R.id.floatingSaveButton);
        shareButton=findViewById(R.id.floatingShareButton);
        deleteButton=findViewById(R.id.floatingDeleteButton);
        speechButton=findViewById(R.id.floatingSpeechButton);

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("contents");
    }

    private boolean validateTitle() {
        title=titleEditText.getText().toString().trim();
        if(title.isEmpty())
            return false;
        return true;
    }
    private boolean validateDescription(){
        description=descriptionEditText.getText().toString().trim();
        if(description.isEmpty())
            return false;
        return  true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingSaveButton:
                saveNote();
                break;
            case R.id.floatingShareButton:
                shareNote();
                break;
            case R.id.floatingDeleteButton:
                deleteNote();
                break;
            case R.id.floatingSpeechButton:
                speech();
                break;
        }
    }

    private void speech() {
        String text="Title is "+titleEditText.getText().toString()+"and  Description is "+descriptionEditText.getText().toString();
        float pitch=0.5f;
        float speed=0.5f;

        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);

        mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    private void deleteNote() {
        databaseReference.child(uid).child(randomKey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditNote.this, "Note Removed Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditNote.this,DashboardActivity.class));
                        finish();
                    }
                });

    }

    private void shareNote() {

        Intent intent=new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody="Here is share content body";
        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
        intent.putExtra(Intent.EXTRA_TEXT,"Title: "+title+"\nDescription: "+description+"\nCreatedAt: "+createdDate+"\nEditedAt: "+editedDate);
        startActivity(Intent.createChooser(intent,"Share via"));
    }

    private void saveNote() {
        if(!validateTitle()| !validateDescription()){
            Toast.makeText(this, "Title and Descripton can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Content content=new Content();
        content.setEditedDate();
        editedDate=content.getEditedDate();
        databaseReference.child(uid).child(randomKey).child("editedDate").setValue(editedDate);
        databaseReference.child(uid).child(randomKey).child("title").setValue(title).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                databaseReference.child(uid).child(randomKey).child("description").setValue(description)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditNote.this, "Note is successfully updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditNote.this,DashboardActivity.class));
                        finish();
                    }
                });
            }
        });
       // databaseReference.child(uid).child(randomKey).child("description").setValue(description);





    }

    @Override
    protected void onDestroy() {
        if(mTTS!=null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
}
