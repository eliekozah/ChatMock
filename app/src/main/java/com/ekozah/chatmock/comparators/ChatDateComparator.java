package com.ekozah.chatmock.comparators;

import com.ekozah.chatmock.data.ChatRoom;

import java.util.Comparator;

public class ChatDateComparator implements Comparator<ChatRoom> {

    @Override
    public int compare(ChatRoom lhs, ChatRoom rhs) {
        if(lhs.getLastChatMessage().getTime().before(rhs.getLastChatMessage().getTime()))
            return 1;
        return -1;
    }
}
