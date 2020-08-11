package com.example.android0211.Refactor.Util.Chatting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android0211.R;

import java.util.List;

public class chatboxAdapter extends RecyclerView.Adapter<chatboxAdapter.MyViewHolder> {
    private List<Message> MessageList;

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public TextView message;


        public MyViewHolder(View view) {
            super(view);

            nickname = (TextView) view.findViewById(R.id.nickname);
            message = (TextView) view.findViewById(R.id.message);

        }
    }
    public chatboxAdapter(List<Message>MessagesList) {

        this.MessageList = MessagesList;


    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    @Override
    public chatboxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);



        return new chatboxAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final chatboxAdapter.MyViewHolder holder, final int position) {
        final Message m = MessageList.get(position);
        holder.nickname.setText(m.getNickname() +" : ");

        holder.message.setText(m.getMessage() );




    }
}
