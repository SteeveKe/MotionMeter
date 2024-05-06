package com.capucinetulipe.motionmeter.viewHolders;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.capucinetulipe.motionmeter.database.MotionMeterRepository;
import com.capucinetulipe.motionmeter.database.entities.Folder;

import java.util.List;

public class FolderViewModel extends AndroidViewModel{
    private final MotionMeterRepository repository;
    private final LiveData<List<Folder>> allFolderByUserID;

    public FolderViewModel(@NonNull Application application, int userID) {
        super(application);
        repository = MotionMeterRepository.getRepository(application);
        allFolderByUserID = repository.getAllFolderByUserID(userID);
    }

    public LiveData<List<Folder>> getAllFolderByUserID() {
        return allFolderByUserID;
    }

    public void insert(Folder folder){
        repository.insertFolder(folder);
    }
}
