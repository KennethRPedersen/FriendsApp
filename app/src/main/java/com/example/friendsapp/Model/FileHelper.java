package com.example.friendsapp.Model;

import android.os.Environment;

import com.example.friendsapp.BE.BEFriend;

import java.io.File;

public class FileHelper {

    public static File getOutputMediaFile(BEFriend friend) {
        String DIRECTORY_NAME = "Camera01";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), DIRECTORY_NAME);

        if (!checkDirectoryExists(mediaStorageDir)){
            return null;
        }

        // Create a media file name
        //String timeStamp = Calendar.getInstance().toString();
        String postfix = "jpg";
        String prefix = "IMG";

        File mediaFile = new File(mediaStorageDir.getPath() +
                File.separator + prefix +
                friend.getId()
                + "." + postfix);

        return mediaFile;
    }

    /**
     * Check if Directory exists, if not try to make it. If succesful return true.
     * @param mediaStorageDir
     * @return
     */
    private static boolean checkDirectoryExists(File mediaStorageDir) {
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return false;
            }
        }
        return true;
    }
}
