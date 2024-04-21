package com.capucinetulipe.motionmeter.database;

import android.app.Application;
import android.util.Log;

import com.capucinetulipe.motionmeter.database.entities.Records;
import com.capucinetulipe.motionmeter.database.entities.User;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MotionMeterRepository {
    private UserDAO userDAO;
    private RecordsDAO recordsDAO;
    private ArrayList<User> allLogs;

    private ArrayList<Records> recordsLogs;

    public MotionMeterRepository(Application application) {
        MotionMeterDatabase db = MotionMeterDatabase.getDatabase(application);
        this.userDAO = db.UserDAO();
        this.recordsDAO = db.RecordsDAO();
        this.allLogs = (ArrayList<User>) this.userDAO.getAllUsers();
        this.recordsLogs = (ArrayList<Records>) this.recordsDAO.getAllRecords();

    }
    private static MotionMeterRepository repository;


    public static MotionMeterRepository getRepository(Application application){
        if (repository != null){
            return repository;
        }
        Future<MotionMeterRepository> future = MotionMeterDatabase.databaseWriteExecutor.submit(
                new Callable<MotionMeterRepository>() {
                    @Override
                    public MotionMeterRepository call() throws Exception {
                        return new MotionMeterRepository(application);
                    }
                }
        );
        try{
            return future.get();
        } catch (InterruptedException | ExecutionException e){

        }
        return null;

    }

    public ArrayList<User> getAllLogs() {
        Future<ArrayList<User>> future = MotionMeterDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<User>>() {
                    @Override
                    public ArrayList<User> call() throws Exception {
                        return (ArrayList<User>) userDAO.getAllUsers();
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

    public ArrayList<Records> getRecordsLogs() {
        Future<ArrayList<Records>> future = MotionMeterDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<Records>>() {
                    @Override
                    public ArrayList<Records> call() throws Exception {
                        return (ArrayList<Records>) recordsDAO.getAllRecords();
                    }
                }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            //Log.i(MainActivity.TAG, "Problem when getting all records in the repository");
        }
        return null;
    }

    public void insertUser(User... user){
        MotionMeterDatabase.databaseWriteExecutor.execute(() ->{
            userDAO.insert(user);
        });
    }

    public void insertRecord(Records record){
        MotionMeterDatabase.databaseWriteExecutor.execute(() ->{
            recordsDAO.insert(record);
        });
    }
}
