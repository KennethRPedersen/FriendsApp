package com.example.friendsapp.Data;

import android.content.Context;

import com.example.friendsapp.Data.SQLite.SQLiteDataAccess;

public class DataAccessFactory {
    public enum DataTechnology{
        SQLite
    }

    private IDataAccess SQLiteDataAccessInstance;

    public DataAccessFactory(Context context) {
        this.SQLiteDataAccessInstance = SQLiteDataAccess.getInstance(context);
    }

    /**
     * Gives a instance of Data Access class with the given DataTechnology.
     * When choosing technology remember that different technologies cant access each others data.
     * @param tech the data technology.
     * @return An implementation of the dataAccess
     */
    public IDataAccess getDataAccessUsing(DataTechnology tech){
        switch (tech){
            case SQLite:
                return SQLiteDataAccessInstance;
            default:
                return null;
        }
    }

}

