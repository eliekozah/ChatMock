package com.ekozah.chatmock;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import com.ekozah.chatmock.comparators.ChatDateComparator;
import com.ekozah.chatmock.data.ChatUser;
import com.ekozah.chatmock.db.DbHelper;
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

/**
 * this class is the most important: it is where all the chat operations are handled, it
 * is used as a singleton that extends Application class, getting the application context
 * to create the chat database
 */
public class ChatManager extends Application {

    private static ChatManager instance;
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

    /**
     * this function creates the database helper class, checks if the are no chat database
     * if no prior chat history, 100 new chats are generated randomly, then the list is
     * sorted for display
     * @param
     * @return
     */
    void init(){
        dbHelper = new DbHelper(this);
        if(getAllData() == false){
            createChatList();
        }
        Collections.sort(ChatManager.getInstance().getChatRooms(), new ChatDateComparator());
    }

    /**
     * this function checks if a previous chat history exists,
     * if yes all history from database is used to create chat list
     * @param
     * @return boolean telling whether history exists
     */
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

    /**
     * this function creates 100 random chats rooms: random user names and messages
     * @param
     * @return
     */
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

    /**
     * this function generates randomly 0, 1 or 2 messages
     * @param roomID chat room id the messages should blong to
     * @return list of messages
     */
    private ArrayList<ChatMessage> generateZeroOrOneMessage(long roomID, long chatUserID){

        StringUtils stringUtils = new StringUtils();
        Random generator = new Random();
        int numberOfMessages = generator.nextInt(3);
        ArrayList<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
        for (int i = 0; i < numberOfMessages; ++i){
            //timestamps of the messages range from now back to 3 days
            chatMessages.add(new ChatMessage(roomID, chatUserID, "Hello! " + stringUtils.getUniqueStringLongVersion(), TimeUtils.generateRandomTimeBetweenNowAndLastThreeDays()));
        }


        return chatMessages;
    }

    /**
     * this function saves all chat data to local databse
     * @param
     * @return
     */
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

    /**
     * this function sets the chat list data
     * @param data : list of chat rooms
     * @return
     */
    public void setData( ArrayList<ChatRoom> data) {
        this.data = data;
    }

    /**
     * this function gets the message written by the local user and echoes it twice
     * after a randomized delay between 0.5 and 2 secs
     * @param chatRoomID : chat room id the message should belong to,
     * @param message message text
     * @return
     */
    public void addChatMessageAndEchoTwice(final long chatRoomID, final String message){
        publishChatMessage(chatRoomID, myID, message);

        //echo message twice
        final Random rand = new Random();
        int randDelay1 = rand.nextInt(1500) + 500;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {


                        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                //echo once
                                publishChatMessage(chatRoomID, getChatRoomByID(chatRoomID).getFriendID(myID), message);

                                int randDelay2 = rand.nextInt(1500) + 500;
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {


                                                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //echo twice
                                                        publishChatMessage(chatRoomID, getChatRoomByID(chatRoomID).getFriendID(myID), message);
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

    /**
     * this function publishes a chat message: to chat list, to the current open activity
     * and to the database
     * @param chatRoomID : chat room id the message should belong to,
     * @param userID id of the user who is sending the message
     * @param message message text
     * @return
     */
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

    /**
     * this function gets the chat room in the chat list
     * @param chatRoomID : chat room id
     * @return
     */
    public ChatRoom getChatRoomByID(long chatRoomID) {
        int j = 0;
        while (data.size() > j) {
            if(data.get(j).getId() == chatRoomID){
                return data.get(j);
            }
            j++;
        }
        return null;
    }

    /**
     * this function sets local user id
     * @param id : local user id
     * @return
     */
    public void setMyID(long id) {
        this.myID = id;
    }

    /**
     * this function gets local user id
     * @return local user id
     */
    public long getMyID() {
        return myID;
    }

    /**
     * this function gets the list of chat rooms
     * @return list of chat rooms
     */
    public ArrayList<ChatRoom> getChatRooms() {
        return data;
    }

    /**
     * this function sets the chat room listener (to Chat list activity of Chat Activity)
     * @param listener the activity that should receive the one message event
     * @return
     */
    public void setGlobalChatRoomListener(ChatRoomListener listener) {
        globalChatRoomListener = listener;
    }

    /**
     * this function resets the new message count of as certain chat room to zero
     * @param roomID chat room id
     * @return
     */
    public void resetNewMessagesCount(long roomID) {
        getChatRoomByID(roomID).setNewMessagesCount(0);
        dbHelper.updateChatRoom(getChatRoomByID(roomID));
    }
}
