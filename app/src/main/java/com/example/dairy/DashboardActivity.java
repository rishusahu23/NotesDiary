package com.example.dairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dairy.adapter.NotesAdapter;
import com.example.dairy.model.Content;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private ArrayList<Content> notes;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("dashboard");

        init();
        //prepareData();
        showData();

        floatingActionButton.setOnClickListener(this);

    }

    private void showData() {
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notes=new ArrayList<>();

                    for(DataSnapshot tempSnap:dataSnapshot.getChildren()){
                        Content content=tempSnap.getValue(Content.class);
                        notes.add(content);
                        //Toast.makeText(DashboardActivity.this, "hello", Toast.LENGTH_SHORT).show();

                }
                if(notes.size()==0){

                }
                notesAdapter=new NotesAdapter(DashboardActivity.this,notes);
                recyclerView.setAdapter(notesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void prepareData() {
    }

    private void init() {
        recyclerView=findViewById(R.id.recyclerViewNotes);
        mAuth=FirebaseAuth.getInstance();
        floatingActionButton=findViewById(R.id.floatingButton);
        databaseReference= FirebaseDatabase.getInstance().getReference("contents");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu,menu);

        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                notesAdapter.getFilter().filter(newText);
                return false;
            }
        });



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
        //SEND TO LOGIN ACTIVITY
        startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingButton:
                startActivity(new Intent(DashboardActivity.this,CreateNoteActivity.class));
                finish();
                break;

        }
    }
}
