package com.example.dairy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dairy.EditNote;
import com.example.dairy.R;
import com.example.dairy.model.Content;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> implements Filterable {

    private ArrayList<Content> notes;
    private ArrayList<Content> dummyNotes;
    private Context context;

    public NotesAdapter(Context context,ArrayList<Content> notes){
        this.notes=notes;
        dummyNotes=new ArrayList<>(notes);
        this.context=context;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.card,parent,false);
        return new NotesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        final Content content=notes.get(position);
        holder.titleTextView.setText(content.getTitle());
        holder.descriptionTextView.setText(content.getDescription());
        holder.createdDateTextView.setText(content.getCreatedDate());
        holder.editedDateTextView.setText(content.getEditedDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, EditNote.class);
                intent.putExtra("RANDOM_KEY",content.getUid());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public Filter getFilter() {
        return examFilter;
    }

    private Filter examFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Content> filtereNotes=new ArrayList<>();

            if(constraint==null||constraint.length()==0){
                filtereNotes.addAll(dummyNotes);
            }else {
                String filtePattern=constraint.toString().trim().toLowerCase();

                for(Content note:dummyNotes){
                    if(note.getTitle().toLowerCase().contains(filtePattern)){
                        filtereNotes.add(note);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filtereNotes;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notes.clear();
            notes.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public class NotesViewHolder extends RecyclerView.ViewHolder{

        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView createdDateTextView;
        private TextView editedDateTextView;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.title_textView);
            descriptionTextView=itemView.findViewById(R.id.description_textView);
            createdDateTextView=itemView.findViewById(R.id.createdDate_textView);
            editedDateTextView=itemView.findViewById(R.id.editedDate_textView);
        }
    }

}
