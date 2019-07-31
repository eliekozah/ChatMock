package com.ekozah.chatmock.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ekozah.chatmock.R;
import com.ekozah.chatmock.data.ChatMessage;
import com.ekozah.chatmock.ChatManager;

import java.util.ArrayList;
import java.util.List;


public class ChatAdapter extends BaseAdapter {

    List<ChatMessage> messages = new ArrayList<ChatMessage>();
    Context context;

    public ChatAdapter(Context context) {
        this.context = context;
    }


    public void add(ChatMessage message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
        this.messages = chatMessages;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ChatMessage message = messages.get(i);

        if (message.getUserID() == ChatManager.getInstance().getMyID()) {
            convertView = messageInflater.inflate(R.layout.item_local_message, null);
            holder.messageBody =  convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getText());
        } else {
            convertView = messageInflater.inflate(R.layout.item_remote_message, null);
            //holder.avatar = (View) convertView.findViewById(R.id.avatar);
            holder.name =  convertView.findViewById(R.id.tv_remoteMessage_name);
            holder.messageBody = convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);

            holder.name.setText(ChatManager.getInstance().getChatRoomByID(message.getRoomId()).getFriendName(ChatManager.getInstance().getMyID()));
            holder.messageBody.setText(message.getText());

        }

        return convertView;
    }

    class MessageViewHolder {
        public TextView name;
        public TextView messageBody;
    }
}

