package com.jieniuwuliu.jieniu.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.messageEvent.LuntanMsgEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


/**
 * socket服务
 * */
public class SocketService extends Service {
    /**
     * 心跳检测时间
     */
    private static final long HEART_BEAT_RATE = 15 * 1000;//每隔15秒进行一次对长连接的心跳检测
    private static String WEBSOCKET_HOST_AND_PORT = "ws://62.234.155.41:1323/ws?";//可替换为自己的主机名和端口号
    private WebSocket mWebSocket;
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new InitSocketThread().start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebSocket != null) {
            mWebSocket.close(1000, null);
        }
    }

    private class InitSocketThread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                initSocket();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initSocket() throws UnknownHostException, IOException {
        WEBSOCKET_HOST_AND_PORT = WEBSOCKET_HOST_AND_PORT+"token="+SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0,TimeUnit.MILLISECONDS).build();
        Request request = new Request.Builder().url(WEBSOCKET_HOST_AND_PORT).build();
        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(okhttp3.WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                mWebSocket = webSocket;
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                //收到服务器传来的消息
                Log.i("socket","接收到websocket的消息为："+text);
                if (!text.equals("hello")){
                    //发送广播
                    Intent intent = new Intent();
                    intent.putExtra("msg", text);
                    intent.setAction("jieniu.msg");
                    sendBroadcast(intent);
                }else{
                    Log.i("socket","接收到初始服务器推送的消息");
                }
            }


            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {//长连接连接失败的回调
                super.onFailure(webSocket, t, response);
            }

        });
        client.dispatcher().executorService().shutdown();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
    }

    private long sendTime = 0L;
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                boolean isSuccess = mWebSocket.send("{\"heart\":\"heart\"}");//主动发送一个消息给服务器，通过发送消息的成功失败来判断长连接的连接状态
                if (!isSuccess) {//长连接已断开
                    mHandler.removeCallbacks(heartBeatRunnable);
                    mWebSocket.cancel();//取消掉以前的长连接
                    new InitSocketThread().start();//创建一个新的连接
                } else {//长连接处于连接状态
                    Log.i("socket","socket is connect");
                }
                sendTime = System.currentTimeMillis();
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);//每隔一定的时间，对长连接进行一次心跳检测
        }
    };
}
