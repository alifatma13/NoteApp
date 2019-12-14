package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.example.notes.adapters.NotesRecyclerAdapter;
import com.example.notes.models.Note;
import com.example.notes.persistence.NoteRepository;
import com.example.notes.util.VerticalSpacingItemDecorator;
import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity implements NotesRecyclerAdapter.OnNoteListener, View.OnClickListener {

    private static final String TAG = "NotesListActivity";
    //UI Components
    private RecyclerView mRecyclerView;

    //Vars
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mNotesRecyclerAdapter;
    private NoteRepository mNoteRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        mRecyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.fab).setOnClickListener(this);
        mNoteRepository = new NoteRepository(this);
        initRecyclerView();
        //insertFakeNotes();
        retrieveNotes();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle("Notes");


    }

    private void retrieveNotes()
    {
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(mNotes.size()>0)
                {
                    mNotes.clear();
                }
                if(notes!=null)
                {
                    mNotes.addAll(notes);
                }
                mNotesRecyclerAdapter.notifyDataSetChanged();

            }
        });
    }

    private void insertFakeNotes() {
        for (int i = 0; i < 1000; i++) {
            Note note = new Note();
            note.setTitle("Title # " + i);
            note.setContent("Content # " + i);
            note.setTimestamp("Dec 2019");
            mNotes.add(note);
        }
        mNotesRecyclerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mNotesRecyclerAdapter = new NotesRecyclerAdapter(mNotes,this);
        mRecyclerView.setAdapter(mNotesRecyclerAdapter);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

    }


    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: Clicked!");
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note",mNotes.get(position));
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,NoteActivity.class);
        startActivity(intent);
    }

    private void deleteNote(Note note)
    {
        mNotes.remove(note);
        mNotesRecyclerAdapter.notifyDataSetChanged();
        mNoteRepository.deleteNoteTask(note);

    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}
