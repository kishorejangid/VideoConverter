package com.ilupilu.ecm;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.*;
import com.xuggle.xuggler.*;

import java.io.File;

/**
 * Created by Kishore on 08-08-2014.
 */

public class ConvertVideo extends MediaToolAdapter implements Runnable {

    public static void main(String[] args)
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

    private int VIDEO_WIDTH = 712;
    private int VIDEO_HEIGHT = 429;

    private IMediaWriter writer;
    private IMediaReader reader;
    private File outputFile;

    public ConvertVideo(File inputFile, File outputFile) {
        this.outputFile = outputFile;
        reader = ToolFactory.makeReader(inputFile.getAbsolutePath());
        reader.addListener(this);
    }

    private IVideoResampler videoResampler = null;
    private IAudioResampler audioResampler = null;

    @Override
    public void onAddStream(IAddStreamEvent event) {
        int streamIndex = event.getStreamIndex();
        IStreamCoder streamCoder = event.getSource().getContainer().getStream(streamIndex).getStreamCoder();
        if (streamCoder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
            writer.addAudioStream(streamIndex, streamIndex, 2, 44100);
        } else if (streamCoder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
            //streamCoder.setWidth(VIDEO_WIDTH);
            //streamCoder.setHeight(VIDEO_HEIGHT);
            writer.addVideoStream(streamIndex, streamIndex, VIDEO_WIDTH, VIDEO_HEIGHT);
        }
        super.onAddStream(event);
    }

    @Override
    public void onVideoPicture(IVideoPictureEvent event) {
        IVideoPicture pic = event.getPicture();
        if (videoResampler == null) {
            videoResampler = IVideoResampler.make(VIDEO_WIDTH, VIDEO_HEIGHT, pic.getPixelType(), pic.getWidth(), pic.getHeight(), pic.getPixelType());
        }
        IVideoPicture out = IVideoPicture.make(pic.getPixelType(), VIDEO_WIDTH, VIDEO_HEIGHT);
        videoResampler.resample(out, pic);

        IVideoPictureEvent asc = new VideoPictureEvent(event.getSource(), out, event.getStreamIndex());
        super.onVideoPicture(asc);
        out.delete();
    }

    @Override
    public void onAudioSamples(IAudioSamplesEvent event) {
        IAudioSamples samples = event.getAudioSamples();
        if (audioResampler == null) {
            audioResampler = IAudioResampler.make(2, samples.getChannels(), 44100, samples.getSampleRate());
        }
        if (event.getAudioSamples().getNumSamples() > 0) {
            IAudioSamples out = IAudioSamples.make(samples.getNumSamples(), samples.getChannels());
            audioResampler.resample(out, samples, samples.getNumSamples());

            AudioSamplesEvent asc = new AudioSamplesEvent(event.getSource(), out, event.getStreamIndex());
            super.onAudioSamples(asc);
            out.delete();
        }
    }

    @Override
    public void run() {
        writer = ToolFactory.makeWriter(outputFile.getAbsolutePath(), reader);
        this.addListener(writer);
        while (reader.readPacket() == null) {
        }
    }

}
