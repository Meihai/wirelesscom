package com.keydak.wireless.reliabilityTest.pi.client;

import java.io.FileWriter;

/**
 * Created by admin on 2017/3/27.
 */
public class FileWriterTest {
    public static void main(String args[]){
        FileWriter fileWriter=null;
        try{
            fileWriter=new FileWriter("E:/IdeaProjects/wirelesscom/data/0.txt");
            fileWriter.write("hahha");
            fileWriter.write("\r\n new line");
            fileWriter.flush();
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }
}
