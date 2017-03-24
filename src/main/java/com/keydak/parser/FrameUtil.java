package com.keydak.parser;

import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2017/3/22.
 */
public class FrameUtil {
    public static List<Frame> getSendRecvData(){
        List<Frame> frameList=null;
        try{
            SAXFrameService service=new SAXFrameService();
            frameList=service.getFrameList();
        }catch(Throwable t){
            t.printStackTrace();
        }
        return frameList;
    }

    public static HashMap<String,String> getSendRecvMap(){
        HashMap<String,String> sendRecvMap=new HashMap<String,String>();
        List<Frame> frameList=getSendRecvData();
        for(Frame insFrame:frameList){
            sendRecvMap.put(insFrame.getSendFrame().replaceAll(" ",""),insFrame.getRecvFrame().replaceAll(" ",""));
        }
        return sendRecvMap;
    }
}
