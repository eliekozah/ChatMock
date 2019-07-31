package com.ekozah.chatmock.data;

import java.util.ArrayList;

/**
 * this class contains the data of a chat room
 * it's design can support multiple users in a chat room(group chat),
 * not just 2 users
 */
public class ChatRoom {
    long id;
    ArrayList<ChatUser> chatUsers;
    int newMessagesCount;
    ArrayList<ChatMessage> chatMessages;


    public ChatRoom() {
    }

    public ChatRoom(long id, ArrayList<ChatUser> chatUsers, ArrayList<ChatMessage> chatMessages, int newMessagesCount) {
        this.id = id;
        this.chatUsers = chatUsers;
        this.newMessagesCount = newMessagesCount;
        this.chatMessages = chatMessages;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * the following three functions are not as generic as the other ones
     * the get info of the chat friend (the first element in the chat user list
     * that has an id that is different than the local user id
     * they are implemented for the sake of this mock application
     * they all take the same parameter which is the local user id
     */
    public ChatUser getFriend(long myID) {
        int j = 0;
        while (chatUsers.size() > j) {
            if(chatUsers.get(j).getId() != myID){
                return chatUsers.get(j);
            }
            j++;
        }
        return null;
    }

    public String getFriendName(long myID) {
        int j = 0;
        while (chatUsers.size() > j) {
            if(chatUsers.get(j).getId() != myID){
                return chatUsers.get(j).getFullName();
            }
            j++;
        }
        return "";
    }

    public long getFriendID(long myID) {
        int j = 0;
        while (chatUsers.size() > j) {
            if(chatUsers.get(j).getId() != myID){
                return chatUsers.get(j).getId();
            }
            j++;
        }
        return -1;
    }

    public ArrayList<ChatUser> getChatUsers() {
        return chatUsers;
    }


    public int getNewMessagesCount() {
        return newMessagesCount;
    }

    public void setNewMessagesCount(int newMessagesCount) {
        this.newMessagesCount = newMessagesCount;

    }

    public ArrayList<ChatMessage> getChatMessages(){
        return chatMessages;
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages){
        this.chatMessages = chatMessages;
    }

    public void setChatUsers(ArrayList<ChatUser> chatUsers){
        this.chatUsers = chatUsers;
    }

    public void addMessage(ChatMessage message){
        newMessagesCount++;
        this.chatMessages.add(message);
    }

    public ChatMessage getLastChatMessage() {
        ChatMessage message = new ChatMessage();
        if(chatMessages.size() > 0) {
            message = chatMessages.get(chatMessages.size()-1);
        }

        return message;
    }

}
