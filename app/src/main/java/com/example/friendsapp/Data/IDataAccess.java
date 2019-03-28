package com.example.friendsapp.Data;

import com.example.friendsapp.BE.BEFriend;

import java.util.List;

public interface IDataAccess {
    List<BEFriend> getAllFriends();
    BEFriend getFriendById(long id);
    void deleteFriend(int id);
    BEFriend updateFriend(BEFriend updatedFriend);
    BEFriend addFriend(BEFriend newFriend);
}
