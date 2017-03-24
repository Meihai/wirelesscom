package com.keydak.wireless.udp;

import com.keydak.parser.Frame;
import com.keydak.parser.FrameUtil;
import com.keydak.wireless.reliabilityTest.pi.client.UdpClient;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2017/3/22.
 */
public class UdpMultiThreadTest {
    //添加多线程共享变量
    public static Timer sendCommandTimer;
    public static List<Frame> frameList;
    public static int frameIndex;
    public static long sendCommandCount;//发送命令计数
    public static long errorReceiveCount;//错误接收命令计数

    public static void main(String[] args){
        frameIndex=0;
        sendCommandCount=0;
        errorReceiveCount=0;
        sendCommandTimer=new Timer();
        frameList= FrameUtil.getSendRecvData();
        initWireLessModule("192.168.1.165",6001);
        sendCommandTimer.scheduleAtFixedRate(getSendCommandTask("192.168.1.165",6001),5000,2000);//安排指定的任务  AT+CIPSEND=,"192.168.1.165",5  55

    }

    public static void initWireLessModule(String hostName,int port){
        try{
            Frame frame=new Frame();
            frame.setDescribeId("192.168.1.165_6001");
            frame.setSendFrame("AT+CIPSEND=,\"192.168.1.165\",5");
            frame.setRecvFrame("hahaha");
            if(frameIndex>=frameList.size())
                frameIndex=0;
            new UdpClient().run(hostName,port,frame);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static TimerTask getSendCommandTask(String hostName,int port){
        final String ipAddress=hostName;
        final int ipPort=port;
        TimerTask task=new TimerTask(){
            @Override
            public void run(){
                //一条指定的指令
                try{
                    final Frame frame=frameList.get(frameIndex);
                    frameIndex++;
                    if(frameIndex>=frameList.size())
                        frameIndex=0;
                    new UdpClient().run(ipAddress,ipPort,frame);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        };
        return task;
    }
}
