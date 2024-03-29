package phamvuyen.demo.saleapp.Activity.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import phamvuyen.demo.saleapp.Activity.Database.notesDatabase;
import phamvuyen.demo.saleapp.Activity.adapters.noteAdapter;
import phamvuyen.demo.saleapp.Activity.entities.Note;
import phamvuyen.demo.saleapp.Activity.listeners.notesListeners;
import phamvuyen.demo.saleapp.R;

public class MainActivity extends AppCompatActivity implements notesListeners {

    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_ADD_NOTES = 22;

    private EditText inputSearch;
    private TextView textMyNotes;
    private ImageView imageAddNote, imageAddImage, imageAddLink;
    private RecyclerView noteRecyclerView;

    private List<Note> notesList;
    private noteAdapter noteAdapter;

    private int noteClickPosition = 1;

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
        getNote(RESULT_OK, false);

     inputSearch.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            noteAdapter.cancelTimer();
         }

         @Override
         public void afterTextChanged(Editable s) {
            if(notesList.size() != 0){
                noteAdapter.searchNotes(s.toString());
            }
         }
     });
    }

    // Update
    @Override
    public void onNoteClicked(Note note, int position) {

        noteClickPosition = position;
        Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("notes", note);
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

    private void getNote(int requestCode, final boolean isNoteDeleted){

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
               if(requestCode == RESULT_OK){
                   notesList.addAll(notes);
                   noteAdapter.notifyDataSetChanged();
               }else if(requestCode == REQUEST_CODE_ADD_NOTES){
                   notesList.add(0, notes.get(0));
                   noteAdapter.notifyItemInserted(0);
                   noteRecyclerView.smoothScrollToPosition(0);
                }else if(requestCode == REQUEST_CODE_UPDATE_NOTE){
                   notesList.remove(noteClickPosition);
                   if(isNoteDeleted){
                       noteAdapter.notifyItemRemoved(noteClickPosition);
                   }else{
                       notesList.add(noteClickPosition, notes.get(noteClickPosition));
                       noteAdapter.notifyItemChanged(noteClickPosition);
                   }

               }
            }
        }
        new getNoteTask().execute();

    }

    final private ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == REQUEST_CODE_ADD_NOTES){
                        getNote(REQUEST_CODE_ADD_NOTES, false);
                    } if(result.getResultCode() == REQUEST_CODE_UPDATE_NOTE){
                        Intent intent = result.getData();
                        if(intent != null){
                            String data = intent.getStringExtra("isNoteDeleted");
                            getNote(REQUEST_CODE_UPDATE_NOTE, intent.getBooleanExtra(data, false));
                        }
                    }
                }
            }

    );


}