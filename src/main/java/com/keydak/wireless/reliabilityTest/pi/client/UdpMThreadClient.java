package com.keydak.wireless.reliabilityTest.pi.client;

import com.keydak.parser.Frame;
import com.keydak.parser.FrameUtil;

import java.io.FileWriter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2017/3/24.
 */
public class UdpMThreadClient {
    //添加多线程共享变量
    public static Timer sendCommandTimer;
    public static List<Frame> frameList;
    public static int frameIndex;
    public static long sendCommandCount;//发送命令计数
    public static long errorReceiveCount;//错误接收命令计数
    public static String dsthostName; //udp的目标ip地址
    public static int dstPort; //udp目标端口
    public static String textFilePath;
    public static long startTime;
    public static long endTime;
    public static FileWriter fileWriter;
    public static long fileIndex;
    public static void main(String[] args){
        frameIndex=0;
        sendCommandCount=0;
        errorReceiveCount=0;
        parseArgs(args);
        sendCommandTimer=new Timer();
        frameList= FrameUtil.getSendRecvData();
        //sendCommandTimer.scheduleAtFixedRate(getSendCommandTask("192.168.1.166",6001),5000,2000); //指定目标ip地址,源地址
        sendCommandTimer.schedule(getSendCommandTask(dsthostName,dstPort),5000,2000); //指定目标ip地址,源地址，延迟时间，发送间隔
    }
    public static void parseArgs(String[] args){
        if(args.length<3){
            System.out.println("Usage:java -jar ...  hostName port data_record_text_path");
            //使用默认参数
            dsthostName="192.168.1.166";
            dstPort=6001;
            textFilePath="E:/IdeaProjects/wirelesscom/data/";
        }else{
            dsthostName=args[0];
            dstPort=Integer.valueOf(args[1]);
            textFilePath=args[2];
        }
        fileIndex=sendCommandCount/200000;//20万条记录一个文本文件
        fileWriter=createFileWriter(textFilePath,  fileIndex);
    }

    public static TimerTask getSendCommandTask(String hostName, int port){
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
                    long index=sendCommandCount/10;
                    if(index>fileIndex){
                        fileIndex=index;
                        if(fileWriter!=null){
                            fileWriter.flush();
                            fileWriter.close();
                        }
                        fileWriter=createFileWriter(textFilePath,  fileIndex);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        };
        return task;
    }

    public static FileWriter createFileWriter(String filePath, long fileIndex){
         String fileName=filePath+fileIndex+".txt";
         FileWriter fileWriter=null;
         try{
             fileWriter=new FileWriter(fileName);
             fileWriter.write("#");
             fileWriter.write("开始发送时间,发送数据,开始接收数据时间,接收数据,接收数据包错误个数,发送数据包总数,响应时间(ms),发送数据时间间隔(s)\r\n");
         }catch(Exception e){
             e.printStackTrace();
         }
         return fileWriter;
    }


}
