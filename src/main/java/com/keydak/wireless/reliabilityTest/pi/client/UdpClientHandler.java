package com.keydak.wireless.reliabilityTest.pi.client;

import com.keydak.parser.Frame;
import com.keydak.util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * Created by admin on 2017/3/22.
 */
public class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket>
    {
        private Frame sendRecvFrame;
        public UdpClientHandler(Frame sendRecvFrame){
            this.sendRecvFrame=sendRecvFrame;
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg)
        throws Exception {
            UdpMThreadClient.endTime=System.currentTimeMillis();
            System.out.println("结束时间为:"+ UdpMThreadClient.endTime);
            System.out.println("耗时："+(UdpMThreadClient.endTime-UdpMThreadClient.startTime)+" ms");
            ByteBuf byteBuf=(ByteBuf) msg.content();
            int len = byteBuf.readableBytes();
            byte[] responseByte = new byte[len];
            byteBuf.getBytes(0, responseByte);
            String response= HexUtil.bytesToHexString(responseByte);
            String originData=sendRecvFrame.getRecvFrame();
            if(response.equals(originData)){
                System.out.println("True:"+sendRecvFrame.getSendFrame()+":"+response);
                System.out.println("误包数为:"+ UdpMThreadClient.errorReceiveCount+",总数为:"+UdpMThreadClient.sendCommandCount);
            }else{
                UdpMThreadClient.errorReceiveCount++;
                System.out.println("false:"+sendRecvFrame.getSendFrame()+":"+response);
                System.out.println("误包数为:"+ UdpMThreadClient.errorReceiveCount+",总数为:"+UdpMThreadClient.sendCommandCount);
            }
            UdpMThreadClient.fileWriter.write(sendRecvFrame.getSendFrame()+",");//发送数据
            UdpMThreadClient.fileWriter.flush();
            UdpMThreadClient.fileWriter.write(UdpMThreadClient.endTime+",");//接收数据时间戳
            UdpMThreadClient.fileWriter.write( response+",");//接收数据
            UdpMThreadClient.fileWriter.write(UdpMThreadClient.errorReceiveCount+",");//错误个数
            UdpMThreadClient.fileWriter.write(UdpMThreadClient.sendCommandCount+","); //发送总数
            UdpMThreadClient.fileWriter.write((UdpMThreadClient.endTime-UdpMThreadClient.startTime)+","); //响应时间

            UdpMThreadClient.fileWriter.write("2s\r\n"); //发送数据时间间隔
            UdpMThreadClient.fileWriter.flush();
            //UdpMThreadClient.fileWriter.close();
            ctx.close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception {
            cause.printStackTrace();
            ctx.close();
        }




}
