package com.capucinetulipe.motionmeter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.capucinetulipe.motionmeter.database.MotionMeterRepository;
import com.capucinetulipe.motionmeter.database.UserDAO;
import com.capucinetulipe.motionmeter.database.entities.User;
import com.capucinetulipe.motionmeter.databinding.FragmentSettingsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MotionMeterRepository repository;

    private int id;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentSettingsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        repository = MotionMeterRepository.getRepository(getActivity().getApplication());


    }

    private void changePassword() {
        String oldPassword = binding.oldPassword.getText().toString();
        LiveData<User> userObserver = repository.getUserByUserId(id);
        userObserver.observe(this, user -> {
            if (user != null){
                if (oldPassword.equals(user.getPassword())){
                    String newPassword = binding.newPassword1.getText().toString();
                    String newPasswordConfirmation = binding.newPassword1.getText().toString();
                    if (newPassword.isEmpty()){
                        toastMaker("new password cant be empty");
                        return;
                    }
                    if (newPassword.equals(newPasswordConfirmation)){
                        binding.oldPassword.setSelection(0);
                        repository.changePass(newPassword, id);
                        toastMaker("password changed");
                        userObserver.removeObservers(this);
                    }else {
                        toastMaker("the two password dont match");
                    }
                }
                else{
                    binding.oldPassword.setSelection(0);
                    toastMaker("invalid password");
                }
            }
            else {
                toastMaker(String.format("%d is not a valid user", id));
            }
        });
    }

    private void deleteUser() {
        String userName = binding.deleteUser.getText().toString();
        LiveData<User> userObserver = repository.getUserByUserName(userName);
        userObserver.observe(getViewLifecycleOwner(), user -> {
            if (user != null){
                repository.deleteUser(userName);
                toastMaker(String.format("%s has been deleted", userName));
                userObserver.removeObservers(getViewLifecycleOwner());
            }
            else {
                toastMaker("this user does not exist");
            }
        });
    }

    private void giveAdminPower(){
        String userName = binding.whoToRaise.getText().toString();
        LiveData<User> userObserver = repository.getUserByUserName(userName);
        userObserver.observe(getViewLifecycleOwner(), user -> {
            if (user != null){
                repository.giveAdminPower(userName);
                toastMaker(String.format("%s is now worthy", userName));
                userObserver.removeObservers(getViewLifecycleOwner());
            }
            else {
                toastMaker("this user does not exist");
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity test = (MainActivity)getActivity();
        id = test.getLoggedInUserID();

        binding = FragmentSettingsBinding.inflate(inflater);
        View view = binding.getRoot();
        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

        binding.Raise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveAdminPower();
            }
        });


        LiveData<User> userObserver = repository.getUserByUserId(id);
        userObserver.observe(getViewLifecycleOwner(), user -> {
            if (user != null){
                if (user.getAdmin()){
                    binding.adminPage.setVisibility(View.VISIBLE);
                    binding.adminPower.setVisibility(View.VISIBLE);
                    binding.knights.setVisibility(View.VISIBLE);
                    binding.textAdminPower.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }


}