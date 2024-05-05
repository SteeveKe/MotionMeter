package com.capucinetulipe.motionmeter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.capucinetulipe.motionmeter.database.MotionMeterRepository;
import com.capucinetulipe.motionmeter.database.entities.User;
import com.capucinetulipe.motionmeter.databinding.ActivityLoginBinding;
import com.capucinetulipe.motionmeter.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private MotionMeterRepository repository;
    private boolean isNewAccount = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        repository = MotionMeterRepository.getRepository(getApplication());

        binding.SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });
    }

    private void verifyUser(){
        String username = binding.userNameSignInEditText.getText().toString();
        if (username.isEmpty()){
            toastMaker("Username may not be blank.");
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if (user == null){
                String password = binding.passwordSignInEditText.getText().toString();
                if (password.isEmpty()){
                    toastMaker("Password may not be blank");
                    binding.passwordSignInEditText.setSelection(0);
                    return;
                }

                String confirmPassword = binding.passwordSignInConfirmEditText.getText().toString();
                if (confirmPassword.isEmpty()){
                    toastMaker("Confirm password may not be blank");
                    binding.passwordSignInEditText.setSelection(0);
                    return;
                }

                if (password.equals(confirmPassword)){
                    isNewAccount = true;
                    User newUser = new User(username, password);
                    repository.insertUser(newUser);

                    LiveData<User> newUserObserver = repository.getUserByUserName(newUser.getUsername());
                    newUserObserver.observe(this, user1 -> {
                        if (user1 != null && user1.getUsername().equals(newUser.getUsername())){
                            updateSharedPreference(user1.getId());
                            startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(), user1.getId()));
                        }
                    });
                }
                else {
                    toastMaker("password field and password confirmation field are different");
                    binding.passwordSignInEditText.setSelection(0);
                }
            }else {
                if (!isNewAccount){
                    toastMaker(String.format("%s already exist", username));
                    binding.userNameSignInEditText.setSelection(0);
                }
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent SignInIntentFactory(Context context){
        return new Intent(context, SignInActivity.class);
    }

    private void updateSharedPreference(int userID){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrenferencesEditor = sharedPreferences.edit();
        sharedPrenferencesEditor.putInt(getString(R.string.preference_userid_key), userID);
        sharedPrenferencesEditor.apply();
    }
}