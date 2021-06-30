package com.hypech.case83_room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModelWord extends AndroidViewModel {

    private RepositoryWord mRepository;
    private final LiveData<List<L1_Entity>> mAllWords;

    public ViewModelWord (Application application) {
        super(application);
        mRepository = new RepositoryWord(application);
        mAllWords = mRepository.getAllWords();
    }

    LiveData<List<L1_Entity>> getAllWords() {
        return mAllWords;
    }

    public void insert(L1_Entity word) {
        mRepository.insert(word);
    }
}