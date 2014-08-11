package com.ilupilu.ecm.test;

import com.ilupilu.ecm.ConvertVideo;
import com.ilupilu.ecm.utils.PropertyUtils;
import org.junit.Test;

import java.io.File;

/**
 * Created by Kishore on 08-08-2014.
 */

public class ConvertionTest {
    @Test
    public void Test()
    {
        try {            
            System.out.println("Convertion starting..");
            File inFile=new File("C:\\Users\\Kishore\\Downloads\\Video\\003.mpg");
            //VideoInfo info = new VideoInfo();
            //info.getInfo(inFile.getAbsolutePath());
            File outFile = new File(String.format("D:\\Kishore\\%s.%s",inFile.getName(), PropertyUtils.get("video.OutFormat")));
            outFile.createNewFile();
            ConvertVideo convertVideo=new ConvertVideo(inFile,outFile);
            convertVideo.setVideoHeight(576);
            convertVideo.setVideoWidth(1024);
            convertVideo.run();
            System.out.println("Convertion completed...");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
