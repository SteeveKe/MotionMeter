package com.capucinetulipe.motionmeter;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.capucinetulipe.motionmeter.database.MotionMeterRepository;

public class MainActivity extends AppCompatActivity {

    private MotionMeterRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        repository = MotionMeterRepository.getRepository(getApplication());
    }
}