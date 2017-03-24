package com.keydak.wireless.reliabilityTest.pi.server;

import com.keydak.parser.FrameUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import io.netty.util.CharsetUtil;
import java.util.HashMap;

/**
 * Created by admin on 2017/3/22.
 */
public class UdpServerHandler  extends SimpleChannelInboundHandler<DatagramPacket> {
    private static HashMap<String,String> sendRecvMap= FrameUtil.getSendRecvMap();
    @Override
    public void messageReceived(ChannelHandlerContext ctx, DatagramPacket packet)
            throws Exception {
        String recvString = packet.content().toString(CharsetUtil.UTF_8);
        System.out.println("收到序列:"+recvString);
        String sendString=sendRecvMap.get(recvString);
        if(sendString==null){
            sendString=sendRecvMap.get(recvString.toLowerCase().toString());
        }
        if(sendString !=null){
            String hexString=sendString.replaceAll(" ","").toUpperCase().toString();
            System.out.println("回应序列:"+hexString);
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    hexString, CharsetUtil.UTF_8), packet
                    .sender()));
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
