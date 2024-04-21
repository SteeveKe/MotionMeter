package com.capucinetulipe.motionmeter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.capucinetulipe.motionmeter.database.MotionMeterRepository;
import com.capucinetulipe.motionmeter.database.entities.User;
import com.capucinetulipe.motionmeter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY_USER_ID = "com.capucinetulipe.motionmeter.MAIN_ACTIVITY_USER_ID";
    private ActivityMainBinding binding;
    private MotionMeterRepository repository;
    private User user;

    private int loggedInUserID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        loginUser();
        invalidateOptionsMenu();

        if (loggedInUserID == -1){
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }

        repository = MotionMeterRepository.getRepository(getApplication());
    }

    private void loginUser() {
        user = new User("Drew", "password");
        loggedInUserID = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        item.setTitle(user.getUsername());
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                showLogoutDialog();
                return false;
            }
        });
        return true;
    }

    private void showLogoutDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        final  AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout?");
        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertBuilder.create().show();
    }

    private void logout(){
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    static Intent mainActivityIntentFactory(Context context, int userID){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userID);
        return intent;
    }

}