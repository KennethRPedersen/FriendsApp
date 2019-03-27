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
     *
     * @param updatedFriend
     * @return
     */
    @Override
    public BEFriend updateFriend(BEFriend updatedFriend) {
        return null;
    }

    @Override
    public BEFriend addFriend(BEFriend newFriend) {
        ContentValues cv = new ContentValues();
        cv.put(TableRow.NAME, newFriend.getName());
        cv.put(TableRow.ADDRESS, newFriend.getAddress());
        cv.put(TableRow.EMAIL, newFriend.getEmail());
        cv.put(TableRow.PHONE_NUMBER, newFriend.getPhoneNumber());
        cv.put(TableRow.WEBSITE, newFriend.getWebsite());
        cv.put(TableRow.BIRTHDAY, newFriend.getBirthdate().getTime());

        long id = db.insert(TABLE_NAME, null, cv);

        newFriend.setId(id);

        return newFriend;
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
