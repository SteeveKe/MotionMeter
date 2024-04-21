package com.capucinetulipe.motionmeter.database;

import android.app.Application;

import com.capucinetulipe.motionmeter.database.entities.User;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MotionMeterRepository {
    private UserDAO userDAO;
    private ArrayList<User> allLogs;
    public MotionMeterRepository(Application application){
        MotionMeterDatabase db = MotionMeterDatabase.getDatabase(application);
        this.userDAO = db.UserDAO();
        this.allLogs = (ArrayList<User>) this.userDAO.getAllRecords();
    }

    public ArrayList<User> getAllLogs() {
        Future<ArrayList<User>> future = MotionMeterDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<User>>() {
                    @Override
                    public ArrayList<User> call() throws Exception {
                        return (ArrayList<User>) userDAO.getAllRecords();
                    }
                }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            //Log.i(MainActivity.TAG, "Problem when getting all user in the repository");
        }
        return null;
    }

    public void insertUser(User user){
        MotionMeterDatabase.databaseWriteExecutor.execute(() ->{
            userDAO.insert(user);
        });
    }
}
