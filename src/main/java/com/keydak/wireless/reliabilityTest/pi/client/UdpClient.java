package com.keydak.wireless.reliabilityTest.pi.client;

import com.keydak.parser.Frame;
import com.keydak.util.HexUtil;
import com.keydak.wireless.udp.UdpMultiThreadTest;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;


/**
 * Created by admin on 2017/3/22.
 */
public class UdpClient {
    public void run(String hostName,int port,Frame sendRecvFrame) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UdpClientHandler(sendRecvFrame));
            //绑定端口 若为0 可为任意端口 9990
            Channel ch = b.bind(9990).sync().channel();
            byte[] hexByte= HexUtil.hexStringToByte(sendRecvFrame.getSendFrame());
            ch.writeAndFlush(
                    new DatagramPacket(Unpooled.copiedBuffer(hexByte), new InetSocketAddress(
                                hostName, port))).sync();
//                    new DatagramPacket(Unpooled.copiedBuffer("AT+CIPSEND=,\"192.168.1.165\",5",
//                            CharsetUtil.UTF_8), new InetSocketAddress(
//                            hostName, port))).sync();
            UdpMultiThreadTest.sendCommandCount++;
            if (!ch.closeFuture().await(15000)) {  //等待15秒
                    UdpMultiThreadTest.errorReceiveCount++;
                    System.out.println("等待超时");
                    System.out.println("目前的误包数为:"+ UdpMultiThreadTest.errorReceiveCount);
                }
        } finally {
            group.shutdownGracefully();
        }
    }

}
