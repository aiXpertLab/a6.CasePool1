package com.hypech.case16_listview_chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
/* ListView中的每一个Item显示都需要Adapter调用一次getView的方法，
// 这个方法会传入一个convertView的参数，返回的View就是这个Item显示的View

// ListView 针对List中每个item，要求 adapter “给我一个视图” (getView)。

// Android在绘制Adapter时，系统首先调用getCount()方法，根据它的返回值得到ListView的长度，然后根据这个长度，
// 调用getView()方法逐行绘制。如果ListView的长度超过了屏幕的长度，android只会绘制显示出来的Item，
// 同时，系统会回收走隐藏的Item。
2.getItemViewType()/getViewTypeCount()方法
若果Item的View都是同一个模板则用不到这俩方法，但很多情况下我们的AdapterView中可能会用到2个或以上的不同的模板，
那这些不同的模板如何复用，那就是这俩方法的作用。
　　看下官方对convertView的解释：The old view to reuse, if possible.
Note: You should check that this view is non-null and of an appropriate type before using. If it is not
possible to convert this view to display the correct data, this method can create a new view. Heterogeneous
lists can specify their number of view types, so that this View is always of the right type
(see getViewTypeCount() and getItemViewType(int)).
意思大概就是： 在有些AdapterView的使用中，比如微博中 有的item中包含图片 有的item包含视频 那么必然的
我们需要用到2种item的布局方式此时如果只是单纯判断“null==convertView”，会造成回收的view不符合你当前
需要的布局 而类似转换失败出错退出。
*/
public class ChatsAdapter extends ArrayAdapter<ChatData> {

    private int resourceId;

    public ChatsAdapter(Context context, int textViewResourceId, List<ChatData> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatData chat = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();

            viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
            view.setTag(viewHolder);        // set ViewHolder object into View
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // get object from View to ViewHolder
        }
        if (chat.getType() == chat.TYPE_RECEIVED) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(chat.getContent());
        }
        if (chat.getType() == chat.TYPE_SENT) {
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(chat.getContent());
        }
        return view;
    }

    class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
    }
}
