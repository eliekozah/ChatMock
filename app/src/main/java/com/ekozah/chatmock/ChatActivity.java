package com.ekozah.chatmock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ekozah.chatmock.interfaces.ChatRoomListener;
import com.ekozah.chatmock.adapters.ChatAdapter;
import com.ekozah.chatmock.data.ChatMessage;

/**
 * this is the activity where a single chat is displayed
 */
public class ChatActivity extends AppCompatActivity implements ChatRoomListener{

    private TextView tvTitle;
    private ImageView ivBack;
    private EditText etMessage;
    private long roomID;
    private ChatAdapter chatMessagesAdapter;
    private ListView lvMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        long roomID = getIntent().getLongExtra("chatRoomID", -1);
        init(roomID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ChatManager.getInstance().setGlobalChatRoomListener(this);
    }


    private void init(long roomID){
        this.roomID = roomID;
        ChatManager.getInstance().setGlobalChatRoomListener(this);
        ChatManager.getInstance().resetNewMessagesCount(roomID);

        chatMessagesAdapter = new ChatAdapter(this);
        chatMessagesAdapter.setChatMessages(ChatManager.getInstance().getChatRoomByID(roomID).getChatMessages());

        etMessage = findViewById(R.id.et_Chat_messageInput);
        lvMessages = findViewById(R.id.lv_Chat_Messages);
        lvMessages.setAdapter(chatMessagesAdapter);
        lvMessages.setSelection(lvMessages.getCount() - 1);

        ChatManager.getInstance().getChatRoomByID(roomID).setNewMessagesCount(0);

        tvTitle = findViewById(R.id.tv_toolbar_name);
        tvTitle.setText(ChatManager.getInstance().getChatRoomByID(roomID).getFriendName(ChatManager.getInstance().getMyID()));

        ivBack = findViewById(R.id.iv_toolbar_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.super.onBackPressed();
            }
        });
    }

    public void sendMessage(View view) {
        String message = etMessage.getText().toString();
        if (message.length() > 0) {
            ChatManager.getInstance().addChatMessageAndEchoTwice(roomID, message);
            etMessage.getText().clear();
        }
    }

    //chat room listener function
    public void onMessage(ChatMessage message) {
        ChatManager.getInstance().resetNewMessagesCount(roomID);
        chatMessagesAdapter.notifyDataSetChanged();
        lvMessages.setSelection(lvMessages.getCount() - 1);
    }
}
