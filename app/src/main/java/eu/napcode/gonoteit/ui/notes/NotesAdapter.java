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

    private List<NoteModel> notes;

    public NotesAdapter(List<NoteModel> notes) {
        this.notes = notes;
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
        holder.itemNoteBinding.noteTextView.setText(notes.get(position).getMsg());
    }

    @Override
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
}
