package com.ekozah.chatmock.data;

import com.ekozah.chatmock.utils.TimeUtils;

import java.util.Date;

/**
 * this class contains the data of a chat message
 * room id the message belong to
 * user id of the person who sent the message
 * message text
 * and message date (date and time)
 */
public class ChatMessage {
    private long roomID;
    private long userID;
    private String text;
    private Date time;

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
}
