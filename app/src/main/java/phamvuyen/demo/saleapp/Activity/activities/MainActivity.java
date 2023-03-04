package phamvuyen.demo.saleapp.Activity.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import phamvuyen.demo.saleapp.Activity.Database.notesDatabase;
import phamvuyen.demo.saleapp.Activity.adapters.noteAdapter;
import phamvuyen.demo.saleapp.Activity.entities.Note;
import phamvuyen.demo.saleapp.Activity.listeners.notesListeners;
import phamvuyen.demo.saleapp.R;

public class MainActivity extends AppCompatActivity implements notesListeners {

    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;

    private EditText inputSearch;
    private TextView textMyNotes;
    private ImageView imageAddNote, imageAddImage, imageAddLink;
    private RecyclerView noteRecyclerView;

    private List<Note> notesList;
    private noteAdapter noteAdapter;

    private int noteClickPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddNoteMain = findViewById(R.id.addNote);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);

                intentActivityResultLauncher.launch(intent);

            }
        });
        init();
        noteRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        notesList = new ArrayList<>();
        noteAdapter = new noteAdapter(notesList, this);
        noteRecyclerView.setAdapter(noteAdapter);
        getNote(REQUEST_CODE_SHOW_NOTES);
    }

    @Override
    public void onNoteClicked(Note note, int position) {

        noteClickPosition = position;
        Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        intentActivityResultLauncher.launch(intent);
    }

    void init(){
        inputSearch = findViewById(R.id.inputSearch);
        textMyNotes = findViewById(R.id.textMyNotes);
        imageAddImage = findViewById(R.id.imageAddImage);
        imageAddLink = findViewById(R.id.imageAddLink);
        imageAddNote = findViewById(R.id.imageAddNote);
        noteRecyclerView = findViewById(R.id.noteRecyclerView);
    }

    private void getNote(final int requestCode){

        @SuppressLint("StaticFieldLeak")
        class getNoteTask extends AsyncTask<Void, Void, List<Note>>{

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return notesDatabase.getNotesDatabase(getApplicationContext()).noteDao().getAllNotes();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
               if(requestCode == REQUEST_CODE_SHOW_NOTES){
                   notesList.addAll(notes);
                   noteAdapter.notifyDataSetChanged();
               }else if(requestCode == 1){
                   notesList.add(0, notes.get(0));
                   noteAdapter.notifyItemInserted(0);
                   noteRecyclerView.smoothScrollToPosition(0);
                }else if(requestCode == REQUEST_CODE_UPDATE_NOTE){
                   notesList.remove(noteClickPosition);
                   notesList.add(noteClickPosition, notes.get(noteClickPosition));
                   noteAdapter.notifyItemChanged(noteClickPosition);

               }
            }
        }
        new getNoteTask().execute();
    }

    final private ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK ){
                       getNote(REQUEST_CODE_SHOW_NOTES);
                    }
                }
            }
    );
}