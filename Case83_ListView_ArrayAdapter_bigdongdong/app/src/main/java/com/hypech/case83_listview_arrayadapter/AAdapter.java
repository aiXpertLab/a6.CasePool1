package com.hypech.case83_listview_arrayadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class AAdapter extends ArrayAdapter<AMessage> {

    int resourceId;
    AMessage msg;
    View view;
    ViewHolder viewHolder;

    public AAdapter(Context context, int textViewResourceId, List<AMessage> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        msg = getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(resourceId, null);
            viewHolder = new ViewHolder();

            viewHolder.leftLayout = view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
            view.setTag(viewHolder);        // set ViewHolder object into View
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // get object from View to ViewHolder
        }
        if (msg.getMessageType().equals("R")) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getContent());
        }
        if (msg.getMessageType().equals("S")) {
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(msg.getContent());
        }
        return view;
    }


    class ViewHolder {
        RelativeLayout leftLayout;
        RelativeLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
    }
}