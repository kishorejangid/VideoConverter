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

    private int videoWidth = 768;
    private int videoHeight = 576;

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
            /*streamCoder.setFlag(IStreamCoder.Flags.FLAG2_SHOW_ALL,false);
            streamCoder.setBitRate(200000);
            streamCoder.setTimeBase(IRational.make(1,25));*/
            writer.addVideoStream(streamIndex, streamIndex, getVideoWidth(), getVideoHeight());
        }
        super.onAddStream(event);
    }

    @Override
    public void onVideoPicture(IVideoPictureEvent event) {
        IVideoPicture pic = event.getPicture();
        if (videoResampler == null) {
            videoResampler = IVideoResampler.make(getVideoWidth(), getVideoHeight(), pic.getPixelType(), pic.getWidth(), pic.getHeight(), pic.getPixelType());
        }
        IVideoPicture out = IVideoPicture.make(pic.getPixelType(), getVideoWidth(), getVideoHeight());
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

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

}