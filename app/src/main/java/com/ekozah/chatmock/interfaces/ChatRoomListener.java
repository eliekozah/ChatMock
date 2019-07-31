package com.ekozah.chatmock.interfaces;

import com.ekozah.chatmock.data.ChatMessage;

public interface ChatRoomListener {
    void onMessage(ChatMessage message);
}
