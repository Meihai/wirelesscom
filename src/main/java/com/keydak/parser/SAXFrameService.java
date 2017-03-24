package com.keydak.parser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2017/3/22.
 */
public class SAXFrameService {
    public List<Frame> getFrameList( ) throws Exception{
        InputStream inStream=this.getClass().getClassLoader().getResourceAsStream("MockSendRecvDefine_01.xml"); //MockSendRecvDefine_ups.xml、MockSendRecvDefine-ma01c.xml 发送命令0103004A0046E5EE有重复，但接收命令不一样
        SAXParserFactory factory=SAXParserFactory.newInstance();
        SAXParser parser=factory.newSAXParser();
        FrameHandler handler=new FrameHandler();
        parser.parse(inStream, handler);
        List<Frame> frameList=handler.getFrames();
        inStream.close();
        return frameList;
    }

    public HashMap<String,String> getSendRecvMap( ){
        HashMap<String,String> sendRecvMap=new HashMap<String,String>();
        try{
            List<Frame> frameList=getFrameList();
            for(Frame insFrame:frameList){
                //去掉所有空格再做对比
                sendRecvMap.put(insFrame.getSendFrame(),insFrame.getRecvFrame());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sendRecvMap;
    }


}
