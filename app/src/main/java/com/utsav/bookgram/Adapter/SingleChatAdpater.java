package com.utsav.bookgram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.utsav.bookgram.Model.ChatModel;
import com.utsav.bookgram.R;

import java.util.List;

public class SingleChatAdpater extends RecyclerView.Adapter<SingleChatAdpater.ViewHolder> {

    private Context mContext;
    private List<ChatModel> mchat;
    private int MEESAGE_TYPE_RIGHT = 1;
    private int MEESSAGE_TYPE_LEFT = 0;


    public SingleChatAdpater(Context mContext, List<ChatModel> mchat) {
        this.mContext = mContext;
        this.mchat = mchat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MEESAGE_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.right_chat_pojo, parent, false);
            return new SingleChatAdpater.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.left_chat_pojo, parent, false);
            return new SingleChatAdpater.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.message_text.setText(mchat.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView message_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message_text = itemView.findViewById(R.id.message_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (mchat.get(position).getSender().equals(userid)) {
            return MEESAGE_TYPE_RIGHT;
        } else {
            return MEESSAGE_TYPE_LEFT;
        }
    }
}
