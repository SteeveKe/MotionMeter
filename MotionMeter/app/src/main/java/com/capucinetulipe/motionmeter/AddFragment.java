package com.capucinetulipe.motionmeter;

import static android.content.Context.SENSOR_SERVICE;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capucinetulipe.motionmeter.database.MotionMeterRepository;
import com.capucinetulipe.motionmeter.database.entities.Records;
import com.capucinetulipe.motionmeter.databinding.FragmentAddBinding;

import java.sql.Time;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAddBinding binding;

    private double maxG = 0;


    private double minG = 0;


    private int count;

    double ax,ay,az;

    double gx,gy,gz;

    public Queue<Double> accelBuffer;

    double g = 9.81;

    public MotionMeterRepository repository;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        accelBuffer = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            accelBuffer.offer((double) 0);
        }
        repository = MotionMeterRepository.getRepository(getActivity().getApplication());
        SensorManager sensorManager = (SensorManager) getLayoutInflater().getContext().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity test = (MainActivity)getActivity();
        id = test.getLoggedInUserID();
        binding = FragmentAddBinding.inflate(inflater);
        View view = binding.getRoot();
        binding.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Record();
            }
        });
        return view;
    }

    private boolean isRecording = false;
    private Time time;
    private long duration;

    public int id;

    private void Record(){
        if (!isRecording){
            binding.recordInProgress.setVisibility(View.VISIBLE);
            isRecording = true;
            maxG = 1;
            minG = 1;
        }
        else{
            isRecording = false;
            binding.recordInProgress.setVisibility(View.INVISIBLE);
            Records record = new Records(1, maxG, minG);
            repository.insertRecord(record);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            ax=event.values[0];
            ay=event.values[1];
            az=event.values[2];
            double res = Math.sqrt(ax * ax + ay * ay + az * az) / g;
            accelBuffer.offer(res);
            accelBuffer.poll();
            Double average = (double) 0;
            for (Double d: accelBuffer) {
                average += d;
            }
            average /= 4;
            if (isRecording)
            {
                if (average > maxG) {
                    maxG = average;
                }
                if (average < minG) {
                    minG = average;
                }
            }
            String temp = String.format("G factor: %.3f", average);
            binding.gfactor.setText(temp);
            binding.progressBar.setProgress((int) Math.round(average * 30));
        }
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY){
            gx=event.values[0];
            gy=event.values[1];
            gz=event.values[2];
            g = Math.sqrt(gx * gx + gy * gy + gz * gz);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}