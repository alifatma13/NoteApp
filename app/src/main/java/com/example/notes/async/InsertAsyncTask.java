package com.example.notes.async;

import android.os.AsyncTask;

import com.example.notes.models.Note;
import com.example.notes.persistence.NoteDao;

public class InsertAsyncTask extends AsyncTask<Note,Void,Void> {
    private NoteDao mNoteDao;

    public InsertAsyncTask(NoteDao noteDao)
    {
        mNoteDao = noteDao;
    }
    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.insertNotes(notes);
        return null;
    }
}
