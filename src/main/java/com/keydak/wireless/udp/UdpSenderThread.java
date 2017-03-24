package com.keydak.wireless.udp;

import com.keydak.parser.Frame;
import com.keydak.wireless.reliabilityTest.pi.client.UdpClient;

/**
 * Created by admin on 2017/3/22.
 */
public class UdpSenderThread implements Runnable {
    private int port;
    private String hostName;
    private Frame frame;

    public UdpSenderThread(String hostName,int port,Frame frame){
        this.hostName=hostName;
        this.port=port;
        this.frame=frame;
    }
    public void run(){
        try{
            new UdpClient().run(hostName,port,frame);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }



}
