package com.ekozah.chatmock;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import com.ekozah.chatmock.comparators.ChatDateComparator;
import com.ekozah.chatmock.data.ChatUser;
import com.ekozah.chatmock.db.DbHelper;
import com.ekozah.chatmock.interfaces.CallbackWithValue;
import com.ekozah.chatmock.interfaces.ChatRoomListener;
import com.ekozah.chatmock.data.ChatMessage;
import com.ekozah.chatmock.data.ChatRoom;
import com.ekozah.chatmock.utils.NumericUtils;
import com.ekozah.chatmock.utils.StringUtils;
import com.ekozah.chatmock.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class ChatManager extends Application {

    private static ChatManager instance;
    private CallbackWithValue callback;
    private ChatRoomListener globalChatRoomListener;
    ArrayList<ChatRoom> data = new ArrayList<ChatRoom>();
    long myID;
    DbHelper dbHelper;

    public static ChatManager getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        globalChatRoomListener = null;
        init();
    }

    void init(){
        dbHelper = new DbHelper(this);
        if(getAllData() == false){
            createChatList();
        }
        Collections.sort(ChatManager.getInstance().getChatRooms(), new ChatDateComparator());
    }

    boolean getAllData () {
        ArrayList<ChatRoom> chatRooms = dbHelper.getChatRooms();
        if(chatRooms.size() == 0){
            return false;
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ChatManager.getInstance().setMyID(prefs.getLong("mySID", 0));
        for(int i = 0; i < chatRooms.size(); ++i){

            ChatRoom chatRoom = chatRooms.get(i);
            ArrayList<ChatUser> chatUsers = dbHelper.getChatUsersByRoomID(chatRoom.getId());
            ArrayList<ChatMessage> chatMessages = dbHelper.getChatMessagesByRoomID(chatRoom.getId());
            chatRoom.setChatMessages(chatMessages);
            chatRoom.setChatUsers(chatUsers);
        }
        ChatManager.getInstance().setData(chatRooms);
        return true;
    }

    private void createChatList() {
        ArrayList<ChatRoom> chatRooms = new ArrayList<>();
        StringUtils stringUtils = new StringUtils();
        long myID = NumericUtils.getID();
        ChatUser userME = new ChatUser(myID, "Elie", "Kozah", String.valueOf(R.drawable.img_2));
        ChatManager.getInstance().setMyID(myID);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("mySID", myID);
        editor.commit();

        for(int i = 0; i < 100; ++i){
            String firstName = stringUtils.getUniqueString();
            String lastName = stringUtils.getUniqueString();
            long roomID = NumericUtils.getID();
            long friendID = NumericUtils.getID();
            Random rand = new Random();
            int randInt = rand.nextInt(5);
            switch (randInt){
                case 0:
                    randInt = R.drawable.img_1;
                    break;
                case 1:
                    randInt = R.drawable.img_2;
                    break;
                case 2:
                    randInt = R.drawable.img_3;
                    break;
                case 3:
                    randInt = R.drawable.img_elie;
                    break;
                case 4:
                    randInt = R.drawable.img_unknown_user;
                    break;

                default:
                    randInt = R.drawable.img_unknown_user;
                    break;
            }
            ChatUser userFriend = new ChatUser(friendID, firstName, lastName, String.valueOf(randInt));
            ArrayList<ChatUser> chatUsers = new ArrayList<>();
            chatUsers.add(userME);
            chatUsers.add(userFriend);

            ArrayList<ChatMessage> chatMessages = generateZeroOrOneMessage(roomID, friendID);
            chatRooms.add( new ChatRoom(roomID, chatUsers, chatMessages, chatMessages.size()));
        }
        ChatManager.getInstance().setData(chatRooms);
        persistAllData();
    }

    private ArrayList<ChatMessage> generateZeroOrOneMessage(long roomID, long chatUserID){

        StringUtils stringUtils = new StringUtils();
        Random generator = new Random();
        int numberOfMessages = generator.nextInt(2);
        ArrayList<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
        if(numberOfMessages == 1){
            chatMessages.add(new ChatMessage(roomID, chatUserID, "Hello! " + stringUtils.getUniqueStringLongVersion(), TimeUtils.generateRandomTimeBetweenNowAndLastThreeDays()));
        }

        return chatMessages;
    }

    void persistAllData(){
        for(int i = 0; i < ChatManager.getInstance().getChatRooms().size(); ++i){

            ChatRoom chatRoom = ChatManager.getInstance().getChatRooms().get(i);
            ArrayList<ChatUser> chatUsers = chatRoom.getChatUsers();
            ArrayList<ChatMessage> chatMessages = chatRoom.getChatMessages();
            dbHelper.addChatRoom(chatRoom);
            for(int j = 0; j < chatUsers.size(); ++j){
                dbHelper.addChatUser(chatUsers.get(j));
                dbHelper.addRelUserToChatRoom(chatUsers.get(j).getId(), chatRoom.getId());
            }
            for(int k = 0; k < chatMessages.size(); ++k){
                dbHelper.addChatMessage(chatMessages.get(k));
            }
        }
    }

    public void setData( ArrayList<ChatRoom> data) {
        this.data = data;
    }

    public void addChatMessageAndEchoTwice(final long chatRoomID, final long userID, final String message){
        publishChatMessage(chatRoomID, userID, message);
        if(userID == myID){
            //echo message
            final Random rand = new Random();
            int randDelay1 = rand.nextInt(1500) + 500;
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            // your code here

                            new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    //Doing job here
                                    publishChatMessage(chatRoomID, getChatRoomByID(chatRoomID).getFriendID(userID), message);

                                    int randDelay2 = rand.nextInt(1500) + 500;
                                    new java.util.Timer().schedule(
                                            new java.util.TimerTask() {
                                                @Override
                                                public void run() {
                                                    // your code here

                                                    new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //Doing job here
                                                            publishChatMessage(chatRoomID, getChatRoomByID(chatRoomID).getFriendID(userID), message);
                                                        }
                                                    });


                                                }
                                            },
                                            randDelay2
                                    );
                                }
                            });


                        }
                    },
                    randDelay1
            );
        }
    }

    public void publishChatMessage(long chatRoomID, long userID, String message){
        ChatMessage chatMessage = new ChatMessage(chatRoomID, userID, message, new Date());
        getChatRoomByID(chatRoomID).addMessage(chatMessage);
        Collections.sort(getChatRooms(), new ChatDateComparator());
        if(globalChatRoomListener != null){
            globalChatRoomListener.onMessage(chatMessage);
        }
        dbHelper.updateChatRoom(getChatRoomByID(chatRoomID));
        dbHelper.addChatMessage(chatMessage);
    }

    public ChatRoom getChatRoomByID(long id) {
        int j = 0;
        while (data.size() > j) {
            if(data.get(j).getId() == id){
                return data.get(j);
            }
            j++;
        }
        return null;
    }

    public void setMyID(long id) {
        this.myID = id;
    }

    public long getMyID() {
        return myID;
    }

    public ArrayList<ChatRoom> getChatRooms() {
        return data;
    }

    public void setGlobalChatRoomListener(ChatRoomListener listener) {
        globalChatRoomListener = listener;
    }

    public void resetNewMessagesCount(long roomID) {
        getChatRoomByID(roomID).setNewMessagesCount(0);
        dbHelper.updateChatRoom(getChatRoomByID(roomID));
    }
}
