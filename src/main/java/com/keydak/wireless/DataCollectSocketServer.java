package com.keydak.wireless;
import java.io.FileOutputStream;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.FileChannel;
import java.util.Set;
/**
 * Created by admin on 2017/3/21.
 * 收到一个
 */
public class DataCollectSocketServer {
    private int port=8000;
    private static ByteBuffer recvBuffer=ByteBuffer.allocate(1024);
    private static Selector selector;
    private static FileOutputStream fout;
    private static FileChannel ch;

    public DataCollectSocketServer(){
        try{
            init();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void init() throws Exception{
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket=serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(port));
        selector=Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server start on port:"+port);
        while(true){
            try{
                selector.select();//返回值为本次触发的事件
                Set<SelectionKey> selectionKeys=selector.selectedKeys();
                for (SelectionKey key:selectionKeys){
                    ServerSocketChannel server=null;
                    SocketChannel client=null;
                    int count=0;
                    if(key.isAcceptable()){
                        server=(ServerSocketChannel) key.channel();
                        System.out.println("New client coming");
                        client=server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        fout=new FileOutputStream("E:/MyEclipseEE/J02/resource/"+client.hashCode()+".jpg");
                        ch=fout.getChannel();

                    }else if(key.isReadable()){
                        client=(SocketChannel)key.channel();
                        recvBuffer.clear();
                        count=client.read(recvBuffer);
                        int k=0;
                        //循环读取缓存区的数据
                        while(count>0){
                            System.out.println("k="+(k++)+" 读取到数据量:"+count);
                            recvBuffer.flip();
                            ch.write(recvBuffer);
                            fout.flush();
                            recvBuffer.clear();
                            count=client.read(recvBuffer);

                        }
                        if(count==1){
                            client.close();
                            ch.close();
                            fout.close();
                        }
                    }else if(key.isWritable()){
                        System.out.println("SeclectionKey.isWritable()");
                    }
                }
                System.out.println("=============selectionKeys.clear()");
                selectionKeys.clear();
            }catch(Exception e){
                e.printStackTrace();
                break;
            }
        }

    }

    public static void main(String[] args){
        new DataCollectSocketServer();
    }
}
