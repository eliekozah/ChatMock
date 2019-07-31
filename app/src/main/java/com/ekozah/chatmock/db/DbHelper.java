package com.ekozah.chatmock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ekozah.chatmock.data.ChatMessage;
import com.ekozah.chatmock.data.ChatRoom;
import com.ekozah.chatmock.data.ChatUser;
import java.util.ArrayList;
import java.util.Date;

/**
 * this class handles all the chat database operations:
 * getting chat rooms, chat messages and chat chat users to
 * to fill the chat room list data object in the chat manager
 * as well as saving all the data, saving new messages and
 * new message count in a certain chat room
 */
public class DbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "ChatMockDB.db";
    Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstant.SQL_CREATE_TABLE_CHAT_ROOMS);
        db.execSQL(DBConstant.SQL_CREATE_TABLE_CHAT_USERS);
        db.execSQL(DBConstant.SQL_CREATE_TABLE_CHAT_MESSAGES);
        db.execSQL(DBConstant.SQL_CREATE_TABLE_REL_USER_TO_CHAT_ROOM);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBConstant.SQL_DELETE_CHAT_ROOMS);
        db.execSQL(DBConstant.SQL_DELETE_CHAT_USERS);
        db.execSQL(DBConstant.SQL_DELETE_CHAT_MESSAGES);
        db.execSQL(DBConstant.SQL_DELETE_REL_USER_TO_CHAT_ROOM);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<ChatRoom> getChatRooms(){
        ArrayList<ChatRoom> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_chat_rooms cr ORDER BY  cr.r_id ASC;",
                new String[] {}
        );
        while(cursor.moveToNext()) {

            ChatRoom room = getChatRoomFromCursor(cursor);
            result.add(room);
        }
        cursor.close();
        return result;
    }



    public ArrayList<ChatUser> getChatUsersByRoomID(long roomID){
        ArrayList<ChatUser> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_chat_users cu " +
                        "LEFT JOIN tbl_rel_user_to_chat_room ruc ON cu.u_id = ruc.u_id "+
                        " LEFT JOIN tbl_chat_rooms cr ON ruc.r_id = cr.r_id " +
                        " WHERE cr.r_id = "+roomID+";",
                new String[] {}
        );
        while(cursor.moveToNext()) {

            ChatUser user = getChatUserFromCursor(cursor);
            result.add(user);
        }
        cursor.close();
        return result;
    }

    public ArrayList<ChatMessage> getChatMessagesByRoomID(long roomID){
        ArrayList<ChatMessage> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_chat_messages cm " +
                        " WHERE cm.r_id = "+roomID+";",
                new String[] {}
        );
        while(cursor.moveToNext()) {

            ChatMessage message = getChatMessageFromCursor(cursor);
            result.add(message);
        }
        cursor.close();
        return result;
    }


    ChatMessage getChatMessageFromCursor(Cursor cursor){
            ChatMessage message = new ChatMessage();
            message.setText( cursor.getString( cursor.getColumnIndexOrThrow("m_text")));
            message.setTime( new Date(cursor.getLong( cursor.getColumnIndexOrThrow("m_ts"))));
            message.setRoomId( cursor.getLong( cursor.getColumnIndexOrThrow("r_id")));
            message.setUserID( cursor.getLong( cursor.getColumnIndexOrThrow("u_id")));
            return message;
        }

    ChatRoom getChatRoomFromCursor(Cursor cursor){
        ChatRoom room = new ChatRoom();
        room.setId( cursor.getLong( cursor.getColumnIndexOrThrow("r_id")));
        room.setNewMessagesCount( cursor.getInt( cursor.getColumnIndexOrThrow("r_new_messages")));
        return room;
    }

    ChatUser getChatUserFromCursor(Cursor cursor){
        ChatUser user = new ChatUser();
        user.setId( cursor.getLong( cursor.getColumnIndexOrThrow("u_id")));
        user.setFirstName( cursor.getString( cursor.getColumnIndexOrThrow("u_first_name")));
        user.setLastName( cursor.getString( cursor.getColumnIndexOrThrow("u_last_name")));
        user.setThumb( cursor.getString( cursor.getColumnIndexOrThrow("u_thumb")));
        return user;
    }

    public boolean addChatRoom(ChatRoom item){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("r_id", item.getId());
        contentValues.put("r_new_messages", item.getNewMessagesCount());

        long result = db.insert(DBConstant.TABLE_CHAT_ROOMS, null, contentValues);



        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean addRelUserToChatRoom(long userID, long chatRoomID){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("u_id", userID);
        contentValues.put("r_id", chatRoomID);

        long result = db.insert(DBConstant.TABLE_REL_USER_TO_CHAT_ROOM, null, contentValues);

        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean addChatMessage(ChatMessage chatMessage){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("m_text", chatMessage.getText());
        contentValues.put("m_ts", chatMessage.getTime().getTime());
        contentValues.put("r_id", chatMessage.getRoomId());
        contentValues.put("u_id", chatMessage.getUserID());

        long result = db.insert(DBConstant.TABLE_CHAT_MESSAGES, null, contentValues);

        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean addChatUser(ChatUser ChatUser){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("u_id", ChatUser.getId());
        contentValues.put("u_first_name", ChatUser.getFirstName());
        contentValues.put("u_last_name", ChatUser.getLastName());
        contentValues.put("u_thumb", ChatUser.getThumb());

        long result = db.insert(DBConstant.TABLE_CHAT_USERS, null, contentValues);

        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public void updateChatRoom(ChatRoom chatRoom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("r_id", chatRoom.getId());
        contentValues.put("r_new_messages", chatRoom.getNewMessagesCount());
        db.update(DBConstant.TABLE_CHAT_ROOMS, contentValues, "r_id = ?", new String[] {chatRoom.getId()+""});

        db.close();
    }

}