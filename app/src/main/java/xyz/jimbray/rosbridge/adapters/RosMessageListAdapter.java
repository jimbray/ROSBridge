package xyz.jimbray.rosbridge.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import xyz.jimbray.rosbridge.R;
import xyz.jimbray.rosbridge.data.RosReceivedMessage;

public class RosMessageListAdapter extends RecyclerView.Adapter<RosMessageListAdapter.MessageHolder> {

    private List<RosReceivedMessage> mData;
    private Context mContext;

    public RosMessageListAdapter(Context context, List<RosReceivedMessage> data) {
        this.mContext = context;
        this.mData = data;
    }


    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item_ros_received_message, null);
        MessageHolder holder = new MessageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        if (holder != null && mData.get(position) != null) {
            RosReceivedMessage data = mData.get(position);
            holder.tv_message.setText(data.getMessage());
            holder.tv_time.setText(data.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void addItem(RosReceivedMessage item) {
        if (mData == null) {
            mData = new LinkedList<>();
        }

        mData.add(item);
        notifyDataSetChanged();
    }

    class MessageHolder extends RecyclerView.ViewHolder {

        private TextView tv_message, tv_time;

        public MessageHolder(View itemView) {
            super(itemView);

            tv_message = itemView.findViewById(R.id.tv_message);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

}
