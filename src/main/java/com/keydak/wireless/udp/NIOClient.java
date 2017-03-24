package com.keydak.wireless.udp;

/**
 * Created by admin on 2017/3/23.
 */

import com.keydak.log.LogUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sammy Leung on 2016/7/27.
 */
public class NIOClient {
    //通道管理器
    private Selector selector;

    /**
     * 获得一个Socket通道，并对该通道做一些初始化的工作
     * @param ip 连接的服务器的ip
     * @param port  连接的服务器的端口号
     * @throws IOException
     */

    public void initClient(String ip,int port) throws IOException {
        // 获得一个Socket通道
        SocketChannel channel = SocketChannel.open();
        // 设置通道为非阻塞
        channel.configureBlocking(false);
        // 获得一个通道管理器
        this.selector = Selector.open();

        // 客户端连接服务器,其实方法执行并没有实现连接，需要在listen（）方法中调
        //用channel.finishConnect();才能完成连接
        channel.connect(new InetSocketAddress(ip,port));
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件。
        channel.register(selector, SelectionKey.OP_CONNECT);
    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen(SocketListener listener) throws IOException {
        // 轮询访问selector
        while (true) {
            selector.select();
            // 获得selector中选中的项的迭代器
            Iterator ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
                // 删除已选的key,以防重复处理
                ite.remove();
                // 连接事件发生
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key
                            .channel();
                    // 如果正在连接，则完成连接
                    if(channel.isConnectionPending()){
                        channel.finishConnect();
                    }
                    // 设置成非阻塞
                    channel.configureBlocking(false);

                    //在这里可以给服务端发送信息哦

                    byte[] tmp = new String("我是客户端").getBytes("UTF-8");

                    byte[] bytes = new byte[tmp.length + 1];
                    System.arraycopy(tmp, 0, bytes, 0, tmp.length);
                    channel.write(ByteBuffer.wrap(bytes));
                    //在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。
                    channel.register(this.selector, SelectionKey.OP_READ);

                    // 获得了可读的事件
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    String msg = read(channel);
                    if (listener != null) {
                        listener.onReceive(msg);
                    }
                }

            }

        }
    }

    /**
     * 处理读取服务端发来的信息 的事件
     * @throws IOException
     */

    public String read(SocketChannel channel) throws IOException{
        ByteBuffer buffer = ByteBuffer.allocate(6);
        String msg = "";
        try {
            List<Byte> byteList = new ArrayList<Byte>();

            while (true){
                buffer.clear();
                int len = channel.read(buffer);
                if (len == 0 || len == -1){
                    break;
                }
                byte[] data = buffer.array();
                for (int i = 0; i < len; i++) {
                    byteList.add(data[i]);
                }
            }

            byte[] bytes = new byte[byteList.size()];
            for (int i=0; i<byteList.size(); i++){
                bytes[i] = byteList.get(i);
            }

            msg = new String(bytes, "UTF-8");
            System.out.println(msg);
        } catch (IOException e) {
            LogUtil.error(getClass(),"red client channel error", e);
            closeChannel(channel);
        }
        return msg;
    }

    /**
     * 关闭socket
     * @param channel
     */
    protected void closeChannel(SocketChannel channel){
        if (channel != null){
            try {
                channel.close();
            } catch (IOException e) {
                LogUtil.error(this.getClass(),"can not close channel", e);
            }
        }
    }

}
