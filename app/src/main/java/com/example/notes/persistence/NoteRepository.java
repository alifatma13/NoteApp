package com.example.notes.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notes.async.DeleteAsyncTask;
import com.example.notes.async.InsertAsyncTask;
import com.example.notes.async.UpdateAsyncTask;
import com.example.notes.models.Note;

import java.util.List;

public class NoteRepository {
    private NoteDatabase mNoteDataBase;

    public NoteRepository(Context context) {
        mNoteDataBase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note) {
        new InsertAsyncTask(mNoteDataBase.getNoteDao()).execute(note);
    }

    public void updateNoteTask(Note note) {
        new UpdateAsyncTask(mNoteDataBase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask() {
        return mNoteDataBase.getNoteDao().getNotes();
    }

    public void deleteNoteTask(Note note) {
        new DeleteAsyncTask(mNoteDataBase.getNoteDao()).execute(note);
    }
}
