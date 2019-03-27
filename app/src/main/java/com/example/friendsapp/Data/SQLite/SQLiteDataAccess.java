package com.example.friendsapp.Data.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.friendsapp.BE.BEFriend;
import com.example.friendsapp.Data.IDataAccess;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDataAccess implements IDataAccess {
    private static final String DATABASE_NAME = "friends.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "friend_table";

    private SQLiteDatabase db;

    private static IDataAccess SQLiteDataAccess;

    /**
     * Private Constructor
     * @param context
     */
    private SQLiteDataAccess(Context context) {
        OpenHelper openHelper = new OpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.db = openHelper.getWritableDatabase();
    }

    /**
     * Get Instance for singleton
     * @param context
     * @return
     */
    public static IDataAccess getInstance(Context context){
        if (SQLiteDataAccess == null){
            SQLiteDataAccess = new SQLiteDataAccess(context);
        }
        return SQLiteDataAccess;
    }

    /**
     * Gets a list with all friends from the SQLite database
     * @return List of BEFriend
     */
    @Override
    public List<BEFriend> getAllFriends() {
        List<BEFriend> list = new ArrayList<>();
        Cursor cursor = this.db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        if (cursor.moveToFirst()) {
            do {
                list.add(friendFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * Get a Friend with a specific id.
     * @param id
     * @return Single BEFried object
     */
    @Override
    public BEFriend getFriendById(int id) {
        Cursor cursor = this.db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id=?", new String[]{id+""});
        if (cursor.moveToFirst()) {
            cursor.close();
            return friendFromCursor(cursor);
        }
        throw new IllegalArgumentException("No friend with id " + id + " does not exist.");
    }

    /**
     * Remove with specific id from database
     * @param id
     */
    @Override
    public void deleteFriend(int id) {
        db.delete(TABLE_NAME," id = ?", new String[]{id+""});
    }

    /**
     * Update a Friend in the database
     * @param updatedFriend
     * @return the updated Friend(BEFriend) if successful else null
     */
    @Override
    public BEFriend updateFriend(BEFriend updatedFriend) {
        ContentValues cv = contentValuesFromFriend(updatedFriend);

        int affectedRows = db.update(TABLE_NAME, cv, " id = ?", new String[]{updatedFriend.getId()+""});

        if (affectedRows == 1){
            return updatedFriend;
        }
        return null;
    }

    /**
     * Add a Friend to the database
     * @param newFriend
     * @return the new Friend but now with an id from the database.
     */
    @Override
    public BEFriend addFriend(BEFriend newFriend) {
        ContentValues cv = contentValuesFromFriend(newFriend);

        long id = db.insert(TABLE_NAME, null, cv);

        newFriend.setId(id);

        return newFriend;
    }


    //Helper Methods:

    /**
     * Get content values with proper TableNames and values from friend.
     * OBS: Does not do the id
     * @param friend from which to take the values
     * @return ContentValues with name, Address, email, phone number, website and birthdate.
     */
    private ContentValues contentValuesFromFriend(BEFriend friend) {
        ContentValues cv = new ContentValues();
        cv.put(TableRow.NAME, friend.getName());
        cv.put(TableRow.ADDRESS, friend.getAddress());
        cv.put(TableRow.EMAIL, friend.getEmail());
        cv.put(TableRow.PHONE_NUMBER, friend.getPhoneNumber());
        cv.put(TableRow.WEBSITE, friend.getWebsite());
        cv.put(TableRow.BIRTHDAY, friend.getBirthdate().getTime());
        return cv;
    }

    /**
     * Get a friend from where the cursor is at this point.
     * @param cursor
     * @return a BEFriend object
     */
    private BEFriend friendFromCursor(Cursor cursor) {
        return new BEFriend(
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("address")),
                cursor.getString(cursor.getColumnIndex("email")),
                cursor.getString(cursor.getColumnIndex("phoneNumber")),
                cursor.getString(cursor.getColumnIndex("website")),
                new Date(cursor.getLong(cursor.getColumnIndex("birthdate"))));
    }
}
