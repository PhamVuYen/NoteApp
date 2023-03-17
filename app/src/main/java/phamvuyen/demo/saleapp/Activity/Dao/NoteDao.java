package phamvuyen.demo.saleapp.Activity.Dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import phamvuyen.demo.saleapp.Activity.entities.Note;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note ORDER BY id DESC")
    List<Note> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);


    @Query("SELECT * FROM note WHERE subtitle LIKE '%' || :subtitle || '%'")
    List<Note> searchNote(String subtitle);

    @Query("Delete from note")
    void deleteAll();
}
