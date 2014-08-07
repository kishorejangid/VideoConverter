package com.ilupilu.ecm.test;

import com.ilupilu.ecm.ConvertVideo;
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
            System.out.println("Convertion stating..");
            File inFile=new File("C:\\Users\\Kishore\\Downloads\\Video\\7333990.mp4");
            File outFile = new File("D:\\Kishore\\Sample.mp4");
            outFile.createNewFile();
            ConvertVideo convertVideo=new ConvertVideo(inFile,outFile);
            convertVideo.run();
            System.out.println("Convertion completed...");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
