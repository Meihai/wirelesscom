package com.keydak.wireless.reliabilityTest.pi.client;

import com.keydak.parser.Frame;
import com.keydak.util.HexUtil;
import com.keydak.wireless.udp.UdpMultiThreadTest;
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
           // String response = msg.content().toString(CharsetUtil.UTF_8);
            ByteBuf byteBuf=(ByteBuf) msg.content();
            int len = byteBuf.readableBytes();
            byte[] responseByte = new byte[len];
            byteBuf.getBytes(0, responseByte);
            String response= HexUtil.bytesToHexString(responseByte);
           // System.out.println("收到数据:"+response);
            String originData=sendRecvFrame.getRecvFrame();
            if(response.equals(originData)){
                System.out.println("True:"+sendRecvFrame.getSendFrame()+":"+response);
                System.out.println("误包数为:"+ UdpMultiThreadTest.errorReceiveCount+",总数为:"+UdpMultiThreadTest.sendCommandCount);
            }else{
                UdpMultiThreadTest.errorReceiveCount++;
                System.out.println("false:"+sendRecvFrame.getSendFrame()+":"+response);
                System.out.println("误包数为:"+ UdpMultiThreadTest.errorReceiveCount+",总数为:"+UdpMultiThreadTest.sendCommandCount);
            }
            ctx.close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception {
            cause.printStackTrace();
            ctx.close();
        }




}
