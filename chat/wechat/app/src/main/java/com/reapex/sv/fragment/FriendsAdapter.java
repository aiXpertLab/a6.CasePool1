package com.reapex.sv.fragment;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.reapex.sv.R;
import com.reapex.sv.entitydaodb.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 通讯录/朋友圈
 */
public class FriendsAdapter extends ArrayAdapter<User> {

    List<User> mFriendList;
    int mResource;
    private LayoutInflater mLayoutInflater;

    public FriendsAdapter(Context context, int resource, List<User> friendList) {
        super(context, resource, friendList);
        this.mResource = resource;
        this.mFriendList = friendList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();
            viewHolder.mAvatarSdv = convertView.findViewById(R.id.sdv_avatar);
            viewHolder.mNameTv = convertView.findViewById(R.id.tv_name);
            viewHolder.mHeaderTv = convertView.findViewById(R.id.tv_header);
            viewHolder.mTempView = convertView.findViewById(R.id.view_header);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User friend = getItem(position);
        String header;
        if (TextUtils.isEmpty(friend.getUserName())) {
            viewHolder.mNameTv.setText(friend.getUserName());
        } else {
            viewHolder.mNameTv.setText(friend.getUserName());
        }

        String avatar = friend.getUserAvatar();
        if (!TextUtils.isEmpty(avatar)) {
            viewHolder.mAvatarSdv.setImageURI(Uri.parse(avatar));
        }

        return convertView;
    }

    @Override
    public User getItem(int position) {
        return mFriendList.get(position);
    }

    @Override
    public int getCount() {
        return mFriendList.size();
    }

    public void setData(List<User> friendList) {
        this.mFriendList = friendList;
    }

    class ViewHolder {
        SimpleDraweeView mAvatarSdv;
        TextView mNameTv;
        TextView mHeaderTv;
        View mTempView;
    }
}
