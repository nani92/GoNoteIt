package eu.napcode.gonoteit.ui.notes;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.FragmentBoardBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.repository.Resource.Status;

public class NotesFragment extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;

    private FragmentBoardBinding binding;

    private NotesViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.viewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(NotesViewModel.class);

        setupRecyclerView();
        this.viewModel.getNotes().observe(this, this::processNotesResponse);
    }

    private void processNotesResponse(Resource<List<NoteModel>> listResource) {
        boolean loading = listResource.status == Status.LOADING;
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);

        if (listResource.status == Status.SUCCESS) {
            binding.recyclerView.setAdapter(new NotesAdapter(listResource.data));
        }

        if (listResource.status == Status.ERROR){
            Snackbar.make(binding.constraintLayout, listResource.message, Snackbar.LENGTH_LONG).show();
        }
    }

    private void setupRecyclerView() {
        //ToDO grid/linear changes no of columns depends on orientation and size
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false);

        return binding.getRoot();
    }
}
