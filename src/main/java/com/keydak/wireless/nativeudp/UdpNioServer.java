package com.keydak.wireless.nativeudp;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
/**
 * Created by admin on 2017/3/23.
 */
public class UdpNioServer {
    public final static int DEFAULT_PORT = 37;

    public static void main(String[] args) throws IOException {
        int port = 37;
        ByteBuffer in = ByteBuffer.allocate(8192);

        ByteBuffer out = ByteBuffer.allocate(8);
        out.order(ByteOrder.BIG_ENDIAN);
        SocketAddress address = new InetSocketAddress("127.0.0.1",port);
        DatagramChannel channel = DatagramChannel.open();
        DatagramSocket socket = channel.socket();
        socket.bind(address);
        System.err.println("bound to " + address);
        while (true) {
            try {
                in.clear();
                SocketAddress client = channel.receive(in);
                System.err.println(client);
                long secondsSince1970 = System.currentTimeMillis();
                out.clear();
                out.putLong(secondsSince1970);
                out.flip();

                out.position(4);
                channel.send(out, client);
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }
}
