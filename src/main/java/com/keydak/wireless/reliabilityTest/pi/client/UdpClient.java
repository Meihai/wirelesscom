package com.keydak.wireless.reliabilityTest.pi.client;

import com.keydak.parser.Frame;
import com.keydak.util.HexUtil;
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
            //startTime
            UdpMThreadClient.startTime=System.currentTimeMillis();
            UdpMThreadClient.fileWriter.write(UdpMThreadClient.startTime+",");
            System.out.println("起始时间为:"+ UdpMThreadClient.startTime);
            ch.writeAndFlush(
                    new DatagramPacket(Unpooled.copiedBuffer(hexByte), new InetSocketAddress(
                                hostName, port))).sync();
            UdpMThreadClient.sendCommandCount++;
            if (!ch.closeFuture().await(500)) {  //超时时间设置为500ms
                UdpMThreadClient.errorReceiveCount++;
                System.out.println("等待超时");
                System.out.println("目前的误包数为:"+ UdpMThreadClient.errorReceiveCount);
                UdpMThreadClient.endTime=System.currentTimeMillis();
                UdpMThreadClient.fileWriter.write(sendRecvFrame.getSendFrame()+",");//发送数据
                UdpMThreadClient.fileWriter.write(UdpMThreadClient.endTime+",");//接收数据时间戳
                UdpMThreadClient.fileWriter.write("timeout"+",");//接收数据
                UdpMThreadClient.fileWriter.write(UdpMThreadClient.errorReceiveCount+",");//错误个数
                UdpMThreadClient.fileWriter.write(UdpMThreadClient.sendCommandCount+","); //发送总数
                UdpMThreadClient.fileWriter.write((UdpMThreadClient.endTime-UdpMThreadClient.startTime)+","); //响应时间
                UdpMThreadClient.fileWriter.write("2s\r\n"); //发送数据时间间隔
                UdpMThreadClient.fileWriter.flush();
            }
        } finally {
            group.shutdownGracefully();
        }
    }

}
