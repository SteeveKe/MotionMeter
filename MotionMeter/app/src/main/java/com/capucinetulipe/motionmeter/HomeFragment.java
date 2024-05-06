package com.capucinetulipe.motionmeter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capucinetulipe.motionmeter.database.MotionMeterRepository;
import com.capucinetulipe.motionmeter.database.entities.Folder;
import com.capucinetulipe.motionmeter.database.entities.Records;
import com.capucinetulipe.motionmeter.database.entities.User;
import com.capucinetulipe.motionmeter.databinding.FragmentHomeBinding;
import com.capucinetulipe.motionmeter.viewHolders.FolderAdapter;
import com.capucinetulipe.motionmeter.viewHolders.FolderViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentHomeBinding binding;
    private FolderViewModel folderViewModel;
    private MotionMeterRepository repository;

    private List<Folder> userFolders;

    private int loggedInUserID;

    private List<Records> recordsList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater);
        View view = binding.getRoot();
        repository = MotionMeterRepository.getRepository(getActivity().getApplication());

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loggedInUserID = sharedPreferences.getInt(getString(R.string.preference_userid_key), -1);

        Update();

        binding.addFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFolder();
            }
        });

        return view;
    }

    private void addFolder(){
        String name = binding.editTextTextFolderName.getText().toString();

        if (!name.isEmpty()){
            LiveData<Folder> f = repository.getFolderName(name, loggedInUserID);
            f.observe(getViewLifecycleOwner(), folders -> {
                if (folders == null){
                    Folder folder= new Folder(loggedInUserID, name);
                    repository.insertFolder(folder);
                }
            });
        }

        Update();
    }

    private void Update(){
        LiveData<List<Folder>> folderObserver = repository.getAllFolderByUserID(loggedInUserID);
        folderObserver.observe(getViewLifecycleOwner(), folders -> {
            if (folders != null){
                userFolders = folders;
            }
        });

        String records = "";
        if (userFolders != null){
            for (Folder folder : userFolders){
                records += folder.getFolderName() + " :\n";

                LiveData<List<Records>> recordObserver = repository.getAllRecordByFolder(folder.getId());
                recordObserver.observe(getViewLifecycleOwner(), record -> {
                    if (record != null){
                        recordsList = record;
                    }
                });

                if (recordsList != null){
                    for (Records r : recordsList){
                        records += "min " + r.getMinG() + "max " + r.getMaxG() + " | " + r.getDateAtRecord() + "\n";

                    }
                }
                records += "\n";
            }
        }
        binding.recordsText.setText(records);
    }
}