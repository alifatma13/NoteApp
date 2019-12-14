package com.example.notes.async;

import android.os.AsyncTask;

import com.example.notes.models.Note;
import com.example.notes.persistence.NoteDao;

public class UpdateAsyncTask extends AsyncTask<Note,Void,Void> {
    private NoteDao mNoteDao;

    public UpdateAsyncTask(NoteDao noteDao)
    {
        mNoteDao = noteDao;
    }
    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.updateNotes(notes);
        return null;
    }
}
