package phamvuyen.demo.saleapp.Activity.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import phamvuyen.demo.saleapp.Activity.Dao.NoteDao;
import phamvuyen.demo.saleapp.Activity.entities.Note;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class notesDatabase extends RoomDatabase {

    private static notesDatabase notesDatabase;

    public static synchronized notesDatabase getNotesDatabase(Context context){
        if(notesDatabase == null){
            notesDatabase = Room.databaseBuilder(
                    context, notesDatabase.class, "note_db"
            ).build();
        }
        return notesDatabase;
    }
    public abstract NoteDao noteDao();
}
