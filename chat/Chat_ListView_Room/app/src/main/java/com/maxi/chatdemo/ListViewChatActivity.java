package com.maxi.chatdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.AbsListView;

import com.maxi.chatdemo.adapter.ChatListViewAdapter;
import com.maxi.chatdemo.common.ChatConst;
import com.maxi.chatdemo.db.ChatMessageBean;
import com.maxi.chatdemo.utils.KeyBoardUtils;
import com.maxi.chatdemo.widget.AudioRecordButton;
import com.maxi.chatdemo.widget.pulltorefresh.PullToRefreshListView;
import com.maxi.chatdemo.widget.pulltorefresh.base.PullToRefreshView;

import java.lang.ref.WeakReference;

public class ListViewChatActivity extends BaseActivity {

    public PullToRefreshListView pullToRefreshListView;
    public ChatListViewAdapter chatListViewAdapter;
    private SendMessageHandler sendMessageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        chatMessageBeanList.clear();
        chatListViewAdapter.notifyDataSetChanged();
        pullToRefreshListView.setAdapter(null);
        sendMessageHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void findView() {
        super.findView();
        pullToRefreshLayout.setSlideView(new PullToRefreshView(this).getSlideView(PullToRefreshView.LISTVIEW));
        pullToRefreshListView = (PullToRefreshListView) pullToRefreshLayout.returnMylist();
    }

    @Override
    protected void init() {
        setTitle("List2View");
        sendMessageHandler  = new SendMessageHandler(this);
        chatListViewAdapter = new ChatListViewAdapter(this);
        chatListViewAdapter.setUserList(chatMessageBeanList);

        pullToRefreshListView.setAdapter(chatListViewAdapter);

        chatListViewAdapter.isPicRefresh = true;
        chatListViewAdapter.notifyDataSetChanged();
        chatListViewAdapter.setSendErrorListener(new ChatListViewAdapter.SendErrorListener() {

            @Override
            public void onClick(int position) {
                ChatMessageBean tbub = chatMessageBeanList.get(position);
                if (tbub.getType() == ChatListViewAdapter.TO_USER_VOICE) {
                    sendVoice(tbub.getUserVoiceTime(), tbub.getUserVoicePath());
                    chatMessageBeanList.remove(position);
                } else if (tbub.getType() == ChatListViewAdapter.TO_USER_IMG) {
                    sendImage(tbub.getImageLocal());
                    chatMessageBeanList.remove(position);
                }
            }

        });
        chatListViewAdapter.setVoiceIsReadListener(new ChatListViewAdapter.VoiceIsRead() {

            @Override
            public void voiceOnClick(int position) {
                // TODO Auto-generated method stub
                for (int i = 0; i < chatListViewAdapter.unReadPosition.size(); i++) {
                    if (chatListViewAdapter.unReadPosition.get(i).equals(position + "")) {
                        chatListViewAdapter.unReadPosition.remove(i);
                        break;
                    }
                }
            }

        });
        pullToRefreshListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        chatListViewAdapter.handler.removeCallbacksAndMessages(null);
                        chatListViewAdapter.setIsGif(true);
                        chatListViewAdapter.isPicRefresh = false;
                        chatListViewAdapter.notifyDataSetChanged();
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        chatListViewAdapter.handler.removeCallbacksAndMessages(null);
                        chatListViewAdapter.setIsGif(false);
                        chatListViewAdapter.isPicRefresh = true;
                        reset();
                        KeyBoardUtils.hideKeyBoard(ListViewChatActivity.this,
                                mEditTextContent);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        voiceBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {

            @Override
            public void onFinished(float seconds, String filePath) {
                // TODO Auto-generated method stub
                sendVoice(seconds, filePath);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                chatListViewAdapter.stopPlayVoice();
            }
        });
        super.init();
    }


    static class SendMessageHandler extends Handler {
        WeakReference<ListViewChatActivity> mActivity;

        SendMessageHandler(ListViewChatActivity activity) {
            mActivity = new WeakReference<ListViewChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            ListViewChatActivity theActivity = mActivity.get();
            if (theActivity != null) {
                switch (msg.what) {
                    case REFRESH:
                        theActivity.chatListViewAdapter.isPicRefresh = true;
                        theActivity.chatListViewAdapter.notifyDataSetChanged();
                        theActivity.pullToRefreshListView.setSelection(theActivity.chatMessageBeanList
                                .size() - 1);
                        break;
                    case SEND_OK:
                        theActivity.mEditTextContent.setText("");
                        theActivity.chatListViewAdapter.isPicRefresh = true;
                        theActivity.chatListViewAdapter.notifyDataSetChanged();
                        theActivity.pullToRefreshListView.setSelection(theActivity.chatMessageBeanList
                                .size() - 1);
                        break;
                    case RECERIVE_OK:
                        theActivity.chatListViewAdapter.isPicRefresh = true;
                        theActivity.chatListViewAdapter.notifyDataSetChanged();
                        theActivity.pullToRefreshListView.setSelection(theActivity.chatMessageBeanList
                                .size() - 1);
                        break;
                    case PULL_TO_REFRESH_DOWN:
                        theActivity.pullToRefreshLayout.refreshComplete();
                        theActivity.chatListViewAdapter.notifyDataSetChanged();
                        theActivity.pullToRefreshListView.setSelection(theActivity.position - 1);
                        theActivity.isDown = false;
                        break;
                    default:
                        break;
                }
            }
        }

    }

    @Override
    protected void loadRecords() {
        isDown = true;
        if (pagelist != null) {
            pagelist.clear();
        }
        pagelist = mChatDbManager.loadPages(page, number);
        position = pagelist.size();
        if (pagelist.size() != 0) {
            pagelist.addAll(chatMessageBeanList);
            chatMessageBeanList.clear();
            chatMessageBeanList.addAll(pagelist);
            if (imageList != null) {
                imageList.clear();
            }
            if (imagePosition != null) {
                imagePosition.clear();
            }
            int key = 0;
            int position = 0;
            for (ChatMessageBean cmb : chatMessageBeanList) {
                if (cmb.getType() == ChatListViewAdapter.FROM_USER_IMG || cmb.getType() == ChatListViewAdapter.TO_USER_IMG) {
                    imageList.add(cmb.getImageLocal());
                    imagePosition.put(key, position);
                    position++;
                }
                key++;
            }
            chatListViewAdapter.setImageList(imageList);
            chatListViewAdapter.setImagePosition(imagePosition);
            sendMessageHandler.sendEmptyMessage(PULL_TO_REFRESH_DOWN);
            if (page == 0) {
                pullToRefreshLayout.refreshComplete();
                pullToRefreshLayout.setPullGone();
            } else {
                page--;
            }
        } else {
            if (page == 0) {
                pullToRefreshLayout.refreshComplete();
                pullToRefreshLayout.setPullGone();
            }
        }
    }

    /**
     * 发送文字
     */
    @Override
    protected void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String content = mEditTextContent.getText().toString();
                chatMessageBeanList.add(getTbub(userName, ChatListViewAdapter.TO_USER_MSG, content, null, null,
                        null, null, null, 0f, ChatConst.COMPLETED));
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                ListViewChatActivity.this.content = content;
                receriveHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }).start();
    }

    /**
     * 接收文字
     */
    String content = "";

    private void receriveMsgText(final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message = "回复：" + content;
                ChatMessageBean tbub = new ChatMessageBean();
                tbub.setUserName(userName);
                String time = returnTime();
                tbub.setUserContent(message);
                tbub.setTime(time);
                tbub.setType(ChatListViewAdapter.FROM_USER_MSG);
                chatMessageBeanList.add(tbub);
                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
                mChatDbManager.insert(tbub);
            }
        }).start();
    }

    /**
     * 发送图片
     */
    int i = 0;

    @Override
    protected void sendImage(final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (i == 0) {
                    chatMessageBeanList.add(getTbub(userName, ChatListViewAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
                            0f, ChatConst.SENDING));
                } else if (i == 1) {
                    chatMessageBeanList.add(getTbub(userName, ChatListViewAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
                            0f, ChatConst.SENDERROR));
                } else if (i == 2) {
                    chatMessageBeanList.add(getTbub(userName, ChatListViewAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
                            0f, ChatConst.COMPLETED));
                    i = -1;
                }
                imageList.add(chatMessageBeanList.get(chatMessageBeanList.size() - 1).getImageLocal());
                imagePosition.put(chatMessageBeanList.size() - 1, imageList.size() - 1);
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                ListViewChatActivity.this.filePath = filePath;
                receriveHandler.sendEmptyMessageDelayed(1, 3000);
                i++;
            }
        }).start();
    }

    /**
     * 接收图片
     */
    String filePath = "";

    private void receriveImageText(final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatMessageBean tbub = new ChatMessageBean();
                tbub.setUserName(userName);
                String time = returnTime();
                tbub.setTime(time);
                tbub.setImageLocal(filePath);
                tbub.setType(ChatListViewAdapter.FROM_USER_IMG);
                chatMessageBeanList.add(tbub);
                imageList.add(chatMessageBeanList.get(chatMessageBeanList.size() - 1).getImageLocal());
                imagePosition.put(chatMessageBeanList.size() - 1, imageList.size() - 1);
                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
                mChatDbManager.insert(tbub);
            }
        }).start();
    }

    /**
     * 发送语音
     */
    @Override
    protected void sendVoice(final float seconds, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatMessageBeanList.add(getTbub(userName, ChatListViewAdapter.TO_USER_VOICE, null, null, null, null, filePath,
                        null, seconds, ChatConst.SENDING));
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                ListViewChatActivity.this.seconds = seconds;
                voiceFilePath = filePath;
                receriveHandler.sendEmptyMessageDelayed(2, 3000);
            }
        }).start();
    }

    /**
     * 接收语音
     */
    float seconds = 0.0f;
    String voiceFilePath = "";

    private void receriveVoiceText(final float seconds, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatMessageBean tbub = new ChatMessageBean();
                tbub.setUserName(userName);
                String time = returnTime();
                tbub.setTime(time);
                tbub.setUserVoiceTime(seconds);
                tbub.setUserVoicePath(filePath);
                chatListViewAdapter.unReadPosition.add(chatMessageBeanList.size() + "");
                tbub.setType(ChatListViewAdapter.FROM_USER_VOICE);
                chatMessageBeanList.add(tbub);
                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
                mChatDbManager.insert(tbub);
            }
        }).start();
    }

    /**
     * 为了模拟接收延迟
     */
    private Handler receriveHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    receriveMsgText(content);
                    break;
                case 1:
                    receriveImageText(filePath);
                    break;
                case 2:
                    receriveVoiceText(seconds, voiceFilePath);
                    break;
                default:
                    break;
            }
        }
    };
}
