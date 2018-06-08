package eu.napcode.gonoteit.ui.notes;

import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintSet;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ItemNoteBinding;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.utils.ImageUtils;

import static android.support.constraint.ConstraintSet.LEFT;
import static android.support.constraint.ConstraintSet.PARENT_ID;
import static android.support.constraint.ConstraintSet.RIGHT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class NotesAdapter extends PagedListAdapter<NoteModel, NotesAdapter.NoteViewHolder> {
    //TODO swipe to delete

    private NoteListener noteListener;

    public NotesAdapter(NoteListener noteListener) {
        super(DIFF_CALLBACK);
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
        NoteModel note = super.getItem(position);

        holder.itemNoteBinding.noteTextView.setText(note.getContent());
        holder.itemNoteBinding.noteTitleTextView.setText(note.getTitle());

        holder.itemNoteBinding.deleteNoteButton.setOnClickListener(v -> noteListener.onDeleteNote(note.getId()));
        holder.itemNoteBinding.noteCardView.setOnClickListener(v -> noteListener.onClickNote(note, holder.itemNoteBinding.noteCardView));

        if (!TextUtils.isEmpty(note.getImageBase64())) {
            displayImage(holder, note);
            adjustConstraintsForImageDisplaying(holder);
        } else {
            holder.itemNoteBinding.attachmentImageView.setVisibility(GONE);
            adjustConstraintsForNoImage(holder);
        }
    }

    private void displayImage(NoteViewHolder holder, NoteModel note) {
        Glide.with(holder.itemView)
                .load(ImageUtils.decodeBase64ToBitmap(note.getImageBase64()))
                .into(holder.itemNoteBinding.attachmentImageView);

        holder.itemNoteBinding.attachmentImageView.setVisibility(VISIBLE);
    }

    private void adjustConstraintsForImageDisplaying(NoteViewHolder holder) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(holder.itemNoteBinding.constraintLayout);
        constraintSet.connect(R.id.noteTitleTextView, LEFT, R.id.attachmentImageView, RIGHT);

        constraintSet.applyTo(holder.itemNoteBinding.constraintLayout);
    }

    private void adjustConstraintsForNoImage(NoteViewHolder holder) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(holder.itemNoteBinding.constraintLayout);
        constraintSet.connect(R.id.noteTitleTextView, LEFT, PARENT_ID, LEFT);

        constraintSet.applyTo(holder.itemNoteBinding.constraintLayout);
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

        void onClickNote(NoteModel noteModel, CardView noteCardView);
    }

    public static final DiffUtil.ItemCallback<NoteModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<NoteModel>() {

                @Override
                public boolean areItemsTheSame(@NonNull NoteModel oldNoteModel, @NonNull NoteModel newNoteModel) {
                    return oldNoteModel.getId().equals(newNoteModel.getId());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull NoteModel oldNoteModel, @NonNull NoteModel newNoteModel) {
                   return oldNoteModel.equals(newNoteModel);
                }
            };
}
