package com.ekozah.chatmock;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekozah.chatmock.comparators.ChatDateComparator;
import com.ekozah.chatmock.db.DbHelper;
import com.ekozah.chatmock.interfaces.CallbackWithValue;
import com.ekozah.chatmock.adapters.ChatListAdapter;
import com.ekozah.chatmock.data.ChatMessage;
import com.ekozah.chatmock.data.ChatRoom;
import com.ekozah.chatmock.data.ChatUser;
import com.ekozah.chatmock.interfaces.ChatRoomListener;
import com.ekozah.chatmock.utils.NumericUtils;
import com.ekozah.chatmock.utils.StringUtils;
import com.ekozah.chatmock.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ChatListActivity extends AppCompatActivity implements ChatRoomListener {

    RecyclerView rvChats;
    ChatListAdapter chatRoomsAdapter;
    LinearLayoutManager layoutManager;

    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    void init(){
        dbHelper = new DbHelper(this);
        rvChats = findViewById(R.id.rv_Main_chats);
        ChatManager.getInstance().setGlobalChatRoomListener(this);
        Collections.sort(ChatManager.getInstance().getChatRooms(), new ChatDateComparator());
        chatRoomsAdapter = new ChatListAdapter(this, ChatManager.getInstance().getChatRooms());
        layoutManager = new LinearLayoutManager(this);
        rvChats.setLayoutManager(layoutManager);
        rvChats.setItemAnimator(new DefaultItemAnimator());
        rvChats.invalidate();
        rvChats.setAdapter(chatRoomsAdapter);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG", "On Start .....");
        ChatManager.getInstance().setGlobalChatRoomListener(this);
        chatRoomsAdapter.notifyDataSetChanged();
    }



    public void onMessage(ChatMessage message) {
        chatRoomsAdapter.notifyDataSetChanged();
    }
}
