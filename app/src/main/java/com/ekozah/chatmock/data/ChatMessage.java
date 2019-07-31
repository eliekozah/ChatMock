package com.ekozah.chatmock.data;

import com.ekozah.chatmock.utils.TimeUtils;

import java.util.Date;

public class ChatMessage {
    long roomID;
    long userID;
    String text;
    Date time;
    ChatUser chatFriend;

    public ChatMessage() {
        roomID = -1;
        userID = -1;
        text = "";
        time = TimeUtils.getDateFromString("1980-12-31");
    }

    public ChatMessage(long roomID, long userID, String text, Date time) {
        this.roomID = roomID;
        this.userID = userID;
        this.text = text;
        this.time = time;
    }

    public long getRoomId() {
        return roomID;
    }

    public void setRoomId(long id) {
        this.roomID = id;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long id) {
        this.userID = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

/*

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }*/


    public ChatUser getChatFriend() {
        return chatFriend;
    }

}
