package eu.napcode.gonoteit.ui.notes;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.data.results.DeletedResult;
import eu.napcode.gonoteit.databinding.FragmentBoardBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.data.results.NotesResult;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.repository.Resource.Status;
import eu.napcode.gonoteit.ui.create.CreateActivity;
import eu.napcode.gonoteit.ui.note.NoteActivity;

import static eu.napcode.gonoteit.ui.note.NoteActivity.NOTE_ID_KEY;

public class NotesFragment extends Fragment implements NotesAdapter.NoteListener {

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    Tracker tracker;

    private FragmentBoardBinding binding;

    private NotesViewModel viewModel;
    private NotesAdapter notesAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        AndroidSupportInjection.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.viewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(NotesViewModel.class);

        setupViews();
        subscribeToNotes();

        trackScreen();
    }

    private void subscribeToNotes() {
        NotesResult notesResult = this.viewModel.getNotes();
        notesResult.getNotes().observe(this, noteModels -> notesAdapter.submitList(noteModels));
        notesResult.getResource().observe(this, this::processResource);
    }

    private void processResource(Resource resource) {
        //TODO display pb in appbar
        boolean loading = resource.status == Status.LOADING;
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);

        if (resource.status == Status.SUCCESS) {
        }

        if (resource.status == Status.ERROR) {
            Snackbar.make(binding.constraintLayout, resource.message, Snackbar.LENGTH_LONG).show();
        }
    }

    private void setupViews() {
        setupRecyclerView();

        this.binding.createFab.setOnClickListener(v ->
                startActivity(new Intent(NotesFragment.this.getContext(), CreateActivity.class)));
    }

    private void setupRecyclerView() {
        //ToDO grid/linear changes no of columns depends on orientation and size
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        this.notesAdapter = new NotesAdapter(this);
        this.binding.recyclerView.setAdapter(notesAdapter);
    }

    private void trackScreen() {
        tracker.setScreenName("Displaying notes");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.getNotes();
    }

    @Override
    public void onDeleteNote(Long id) {
        DeletedResult deletedResult = viewModel.deleteNote(id);
        deletedResult.getResource().observe(this, this::processDeleteResponse);
    }

    @Override
    public void onClickNote(Long id) {
        Intent intent = new Intent(getContext(), NoteActivity.class);
        intent.putExtra(NOTE_ID_KEY, id);

        startActivity(intent);
    }

    private void processDeleteResponse(Resource<Boolean> booleanResource) {
        boolean loading = booleanResource.status == Status.LOADING;
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
