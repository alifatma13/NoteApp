package com.example.notes.async;

import android.os.AsyncTask;

import com.example.notes.models.Note;
import com.example.notes.persistence.NoteDao;

public class DeleteAsyncTask extends AsyncTask<Note,Void,Void> {
    private NoteDao mNoteDao;

    public DeleteAsyncTask(NoteDao noteDao)
    {
        mNoteDao = noteDao;
    }
    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.deleteNotes(notes);
        return null;
    }
}
