package com.keydak.wireless.udp;

import com.keydak.log.LogUtil;

/**
 * Created by admin on 2017/3/23.
 */
public class SocketListener {
    public void onReceive(String msg) {
        LogUtil.debug(getClass(), msg == null ? "null" : msg);
    }
}
