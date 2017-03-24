package com.keydak.util;

import com.keydak.log.LogUtil;

/**
 * Created by admin on 2017/3/22.
 */
public class HexUtil {
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    public static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static String bytesToHexString(byte[] hexByteArray){
        StringBuilder stringBuilder=new StringBuilder("");
        if(hexByteArray==null || hexByteArray.length<=0){
            return null;
        }
        for(int i=0;i<hexByteArray.length;i++){
            int vByte=hexByteArray[i] & 0xFF;
            String hv=Integer.toHexString(vByte).toUpperCase();
            if(hv.length()<2){
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    /**
     * 比较两个byte数组是否相同，相同返回true
     */
    public static  boolean isByteArrayEqual(byte[] srcByteArray,byte[] dstByteArray){
        if (srcByteArray==null  && dstByteArray==null){
            return true;
        }
        if(srcByteArray==null || dstByteArray==null){
            return false;
        }
        if(srcByteArray==dstByteArray){
            return true;
        }
        if(srcByteArray.length!=dstByteArray.length){
            return false;
        }
        boolean bEquals=true;
        int i;
        for(i=0;i<srcByteArray.length;i++){
           if(srcByteArray[i]!=dstByteArray[i]){
               bEquals=false;
               break;
           }
        }
        return bEquals;
    }

    /**
     * 计算误码率，一个byte不等说明错了一个码字,错一个byte表示错一个码字，误码率=错误的byte数目/总的byte数量
     * @param srcByteArray
     * @param dstByteArray
     * @return
     */
    public static double getErrorByteRate(byte[] srcByteArray, byte[] dstByteArray){
        int error_num=0;
        double errorRate=1.0;
        if(srcByteArray==null || dstByteArray==null){
            LogUtil.info(HexUtil.class,"byteArray is null,please check!");
            return errorRate;
        }
        if(srcByteArray.length!=dstByteArray.length){
            return errorRate;
        }
        int i;
        for(i=0;i<srcByteArray.length;i++){
            if(srcByteArray[i]!=dstByteArray[i]){
                error_num++;
                break;
            }
        }
        errorRate=error_num*1.0/srcByteArray.length;
        return errorRate;
    }
}
