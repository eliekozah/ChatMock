package com.ekozah.chatmock.db;

public class DBConstant {
    public static final String TABLE_CHAT_USERS = "tbl_chat_users";
    public static final String TABLE_CHAT_ROOMS = "tbl_chat_rooms";
    public static final String TABLE_CHAT_MESSAGES = "tbl_chat_messages";
    public static final String TABLE_REL_USER_TO_CHAT_ROOM = "tbl_rel_user_to_chat_room";

    public static final String SQL_CREATE_TABLE_CHAT_USERS = "CREATE TABLE "+ TABLE_CHAT_USERS +" (" +
            "  u_id INTEGER PRIMARY KEY, " +
            "  u_first_name TEXT NOT NULL, " +
            "  u_last_name TEXT, " +
            "  u_thumb TEXT " +
            ");";

    public static final String SQL_CREATE_TABLE_CHAT_ROOMS ="CREATE TABLE "+ TABLE_CHAT_ROOMS +" (" +
            "  r_id INTEGER PRIMARY KEY," +
            "  r_new_messages INTEGER" +
            ");";

    public static final String SQL_CREATE_TABLE_CHAT_MESSAGES ="CREATE TABLE "+ TABLE_CHAT_MESSAGES +" (" +
            "  m_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  m_text TEXT NOT NULL, " +
            "  m_ts INTEGER, " +
            "  r_id INTEGER, " +
            "  u_id INTEGER, " +
            "  FOREIGN KEY (r_id) REFERENCES " + TABLE_CHAT_ROOMS + " (r_id), " +
            "  FOREIGN KEY (u_id) REFERENCES " + TABLE_CHAT_USERS + " (u_id)" +
            ");";


    public static final String SQL_CREATE_TABLE_REL_USER_TO_CHAT_ROOM ="CREATE TABLE "+TABLE_REL_USER_TO_CHAT_ROOM+" ( " +
            "  rel_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "  r_id INTEGER, " +
            "  u_id INTEGER, " +
            "  FOREIGN KEY (u_id) REFERENCES " + TABLE_CHAT_USERS + " (u_id), " +
            "  FOREIGN KEY (r_id) REFERENCES " + TABLE_CHAT_ROOMS + " (r_id)" +
            ");";

    public static final String SQL_DELETE_CHAT_USERS = "DROP TABLE IF EXISTS " + TABLE_CHAT_USERS;
    public static final String SQL_DELETE_CHAT_ROOMS = "DROP TABLE IF EXISTS " + TABLE_CHAT_ROOMS;
    public static final String SQL_DELETE_CHAT_MESSAGES = "DROP TABLE IF EXISTS " + TABLE_CHAT_MESSAGES;
    public static final String SQL_DELETE_REL_USER_TO_CHAT_ROOM = "DROP TABLE IF EXISTS " + TABLE_REL_USER_TO_CHAT_ROOM;
}
