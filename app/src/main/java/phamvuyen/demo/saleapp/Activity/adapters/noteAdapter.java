package phamvuyen.demo.saleapp.Activity.adapters;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import com.makeramen.roundedimageview.RoundedImageView;
import java.util.List;
import phamvuyen.demo.saleapp.Activity.entities.Note;
import phamvuyen.demo.saleapp.Activity.listeners.notesListeners;
import phamvuyen.demo.saleapp.R;

public class noteAdapter extends RecyclerView.Adapter<noteAdapter.noteViewHolder>{

    private List<Note> notes;

    private notesListeners notesListener;

    public noteAdapter(List<Note> notes, notesListeners notesListener) {
        this.notes = notes;
        this.notesListener =notesListener;
    }


    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new noteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_note, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull noteViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setNode(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesListener.onNoteClicked(notes.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class noteViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle, textSubTitle, textDataTime;
        LinearLayout layoutNote;
        RoundedImageView imageNote;
        public noteViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textTitle);
            textSubTitle = itemView.findViewById(R.id.textSubTitle);
            textDataTime = itemView.findViewById(R.id.textDataTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNote = itemView.findViewById(R.id.imageNoteMain);
        }

        void setNode(Note note){
            textTitle.setText(note.getTitle());
            if(note.getSubtitle().trim().isEmpty()){
                textSubTitle.setVisibility(View.GONE);
            }else{
                textSubTitle.setText(note.getSubtitle());
            }
            textDataTime.setText(note.getDate_time());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if(note.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else{
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if(note.getNotePad() != null){
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getNotePad()));
                imageNote.setVisibility(View.VISIBLE);
            }else{
                imageNote.setVisibility(View.GONE);
            }
        }
    }
}
