package com.ekozah.chatmock.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ekozah.chatmock.ChatActivity;
import com.ekozah.chatmock.R;
import com.ekozah.chatmock.data.ChatRoom;
import com.ekozah.chatmock.ChatManager;
import com.ekozah.chatmock.utils.TimeUtils;

import java.util.ArrayList;

public class ChatListAdapter extends Adapter<ChatListAdapter.ViewHolder>{

    ArrayList<ChatRoom> simpleData;
    Context mContext;
    int listSize;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  tvLastMessage  = this.itemView.findViewById(R.id.tv_chatRoom_chatLastMessage);
        TextView  tvUsers           = this.itemView.findViewById(R.id.tv_chatRoom_chatUsers);
        TextView  tvTime            = this.itemView.findViewById(R.id.tv_chatRoom_time);
        TextView  tvBadgeCount  = this.itemView.findViewById(R.id.tv_chatRoom_badgeValue);
        ImageView ivProfile     = this.itemView.findViewById(R.id.civ_chatRoom_profile);
        ConstraintLayout clContainer = this.itemView.findViewById(R.id.cl_chatItem);
        public ViewHolder(View view) {
            super(view);
        }
    }

    public ChatListAdapter(Context mContext, ArrayList<ChatRoom> dataList) {
        this.mContext = mContext;
        this.simpleData = dataList;
        this.listSize = dataList.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ChatRoom item = simpleData.get(position);
        holder.tvLastMessage.setText(item.getLastChatMessage().getText());
        holder.tvLastMessage.setTypeface(null, item.getNewMessagesCount() > 0 ? Typeface.BOLD:Typeface.NORMAL);
        holder.tvUsers.setText(item.getFriendName(ChatManager.getInstance().getMyID()));


        holder.tvTime.setText(TimeUtils.getTimeOrDate(item.getLastChatMessage().getTime()));
        holder.tvTime.setVisibility(item.getLastChatMessage().getRoomId() == -1 ? View.GONE: View.VISIBLE);

        holder.tvBadgeCount.setText(String.valueOf(item.getNewMessagesCount()));
        holder.tvBadgeCount.setVisibility(item.getNewMessagesCount()>0? View.VISIBLE: View.GONE);

        holder.ivProfile.setVisibility(View.VISIBLE);
        holder.ivProfile.setImageResource(Integer.valueOf(item.getFriend(ChatManager.getInstance().getMyID()).getThumb()));

        holder.clContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ChatActivity.class);
                i.putExtra("chatRoomID", item.getId());
                mContext.startActivity(i);
            }
        });
    }

    public int getItemCount() {
        return this.simpleData.size();
    }


}
