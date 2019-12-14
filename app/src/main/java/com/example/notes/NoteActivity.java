package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.service.autofill.Validator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.notes.models.Note;
import com.example.notes.persistence.NoteRepository;
import com.example.notes.util.LinedEditText;
import com.example.notes.util.Utility;

public class NoteActivity extends AppCompatActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
        View.OnClickListener, TextWatcher {

    public static final String TAG = "NoteActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    //UI components
    private LinedEditText mLinedEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageButton mCheck, mBackArrow;



    //Vars
    private boolean mIsNewNode;
    private Note mNoteInitial;
    private Note mFinalNote;
    private GestureDetector mGestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mLinedEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);
        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mCheck = findViewById(R.id.toolbar_check);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);
        mNoteRepository = new NoteRepository(this);


        setListeners();

        if(getIncomingIntent())
        {
            //new Note Edit Mode
            setNewNodeProperties();
            enableEditMode();
        }
        {
            //Not a new Node ViewMode
            setNoteProperties();
            disableContentInteraction();
        }

    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("selected_note"))
        {
            mNoteInitial = getIntent().getParcelableExtra("selected_note");
            mFinalNote = new Note();
            mFinalNote.setId(mNoteInitial.getId());
            mFinalNote.setTitle(mNoteInitial.getTitle());
            mFinalNote.setContent(mNoteInitial.getContent());
            mFinalNote.setTimestamp(mNoteInitial.getTimestamp());
            //mFinalNote = getIntent().getParcelableExtra("selected_note");
            mIsNewNode=false;
            mMode = EDIT_MODE_DISABLED;
            return mIsNewNode;

        }
        mIsNewNode = true;
        mMode= EDIT_MODE_ENABLED;
        return mIsNewNode;
    }

    private void enableEditMode()
    {
        enableContentInteraction();
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;
    }

    private void disableEditMode()
    {   disableContentInteraction();
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;
        String temp = mLinedEditText.getText().toString();
        temp= temp.replace("\n","");
        temp=temp.replace(" ", "");
        if(temp.length()>0)
        {
            mFinalNote.setTitle(mEditTitle.getText().toString());
            mFinalNote.setContent(mLinedEditText.getText().toString());
            String timeStamp = Utility.getCurrentTimeStamp();
            mFinalNote.setTimestamp(timeStamp);

            if(!mFinalNote.getContent().equals(mNoteInitial.getContent())
                    || !mFinalNote.getTitle().equals(mNoteInitial.getTitle()))
            {
                saveChanges();
            }
        }
    }

    private void setNewNodeProperties(){
        mViewTitle.setText("NoteTitle");
        mEditTitle.setText("NoteTitle");
        mNoteInitial = new Note();
        mFinalNote = new Note();
        mFinalNote.setTitle("Note Title");
        mFinalNote.setTitle("Note Title");
    }

    private void setNoteProperties()
    {
        mViewTitle.setText(mNoteInitial.getTitle());
        mEditTitle.setText(mNoteInitial.getTitle());
        mLinedEditText.setText(mNoteInitial.getContent());
    }

    private void setListeners()
    {
        mLinedEditText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this,this);
        mViewTitle.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        Log.d(TAG, "onDoubleTap: Double Tapped!");
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.toolbar_check: disableEditMode();
                                    break;
            case R.id.note_text_title: enableEditMode();
                                        mEditTitle.requestFocus();
                                        mEditTitle.setSelection(mEditTitle.length());
                                        break;
            case R.id.toolbar_back_arrow: finish();
                break;

        }
    }

    private void disableContentInteraction()
    {
        mLinedEditText.setKeyListener(null);
        mLinedEditText.setFocusable(false);
        mLinedEditText.setFocusableInTouchMode(false);
        mLinedEditText.setCursorVisible(false);
        mLinedEditText.clearFocus();
        hideSoftKeyboard();
    }

    private void enableContentInteraction()
    {
        mLinedEditText.setKeyListener(new EditText(this).getKeyListener());
        mLinedEditText.setFocusable(true);
        mLinedEditText.setFocusableInTouchMode(true);
        mLinedEditText.setCursorVisible(true);
        mLinedEditText.requestFocus();
    }

    private void hideSoftKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view == null)
        {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    public void onBackPressed() {
        if(mMode == EDIT_MODE_ENABLED)
        {
            onClick(mCheck);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if(mMode== EDIT_MODE_ENABLED)
        {
            enableEditMode();
        }
    }

    private void saveChanges()
    {
        if(mIsNewNode)
        {
            saveNewNote();
        }
        else
        {
            updateNote();
        }
    }

    private void updateNote()
    {
        mNoteRepository.updateNoteTask(mFinalNote);
    }

    private void saveNewNote()
    {
        mNoteRepository.insertNoteTask(mFinalNote);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mViewTitle.setText(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
