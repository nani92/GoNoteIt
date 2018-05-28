package eu.napcode.gonoteit.ui.notes;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ItemNoteBinding;
import eu.napcode.gonoteit.model.note.NoteModel;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    //TODO swipe to delete

    private final NoteListener noteListener;
    private List<NoteModel> notes;

    public NotesAdapter(List<NoteModel> notes, NoteListener noteListener) {
        this.notes = notes;
        this.noteListener = noteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemNoteBinding itemNoteBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_note, parent, false);

        return new NoteViewHolder(itemNoteBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteModel note = notes.get(position);

        holder.itemNoteBinding.noteTextView.setText(note.getContent());
        holder.itemNoteBinding.noteTitleTextView.setText(note.getTitle());

        holder.itemNoteBinding.deleteNoteButton.setOnClickListener(v -> noteListener.onDeleteNote(note.getId()));

        holder.itemNoteBinding.noteCardView.setOnClickListener(v -> noteListener.onClickNote(note.getId()));
    }

    public int getItemCount() {
        return this.notes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        public final ItemNoteBinding itemNoteBinding;

        public NoteViewHolder(ItemNoteBinding itemNoteBinding) {
            super(itemNoteBinding.getRoot());

            this.itemNoteBinding = itemNoteBinding;
        }
    }

    public interface NoteListener {
        void onDeleteNote(Long id);

        void onClickNote(Long id);
    }
}
