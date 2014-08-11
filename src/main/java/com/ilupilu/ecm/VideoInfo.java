package com.ilupilu.ecm;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Kishore on 12-08-2014.
 */
public class VideoInfo {

    public static Logger log = LoggerFactory.getLogger(VideoInfo.class);

    private long numStreams;
    private long duration;
    private long fileSize;
    private long bitRate;
    private String audioCodec;
    private String videoCodec;
    private long startTime;
    private int sampleRate;
    private int channels;
    private String audioFormat;
    private String videoFormat;
    private int width;
    private int height;
    private double frameRate;

    public static VideoInfo getInfo(String fileName) {
        log.info("Getting VideoInfo object..");
        log.debug("File Name: {}",fileName);
        VideoInfo videoInfo = new VideoInfo();

        log.debug("Create a Xuggler container object.");
        IContainer container = IContainer.make();

        log.trace("Attempt to open up the container.");
        int result = container.open(fileName, IContainer.Type.READ, null);

        // check if the operation was successful
        if (result < 0)
            throw new RuntimeException("Failed to open media file");

        // query how many streams the call to open found
        int numStreams = container.getNumStreams();
        log.info("No of streams: {}", numStreams);
        videoInfo.setNumStreams(numStreams);

        // query for the total duration
        long duration = container.getDuration();
        log.info("Duration (ms): {}", duration);
        videoInfo.setDuration(duration);

        // query for the file size
        long fileSize = container.getFileSize();
        log.info("File Size (bytes): {}", fileSize);
        videoInfo.setFileSize(fileSize);

        // query for the bit rate
        long bitRate = container.getBitRate();
        log.info("Bit Rate: {}", bitRate);

        // iterate through the streams to print their meta data
        log.debug("Iterating all streams.");
        for (int i = 0; i < numStreams; i++) {

            // find the stream object
            IStream stream = container.getStream(i);

            // get the pre-configured decoder that can decode this stream;
            IStreamCoder coder = stream.getStreamCoder();

            log.trace("*** Start of Stream Info ***");

            ICodec.Type type = coder.getCodecType();
            switch (type) {
                case CODEC_TYPE_AUDIO:
                    videoInfo.setAudioCodec(coder.getCodecID().toString().replaceFirst("CODEC_ID_", ""));
                    log.debug("Audio Codec: {}",videoInfo.getAudioCodec());
                    videoInfo.setSampleRate(coder.getSampleRate());
                    log.debug("Sample Rate: {}",videoInfo.getSampleRate());
                    videoInfo.setChannels(coder.getChannels());
                    log.debug("Channels: {}",videoInfo.getChannels());
                    videoInfo.setAudioFormat(coder.getSampleFormat().toString().replaceFirst("FMT_", ""));
                    log.debug("Audio Format: {}",videoInfo.getAudioFormat());
                    break;
                case CODEC_TYPE_VIDEO:
                    videoInfo.setVideoCodec(coder.getCodecID().toString().replaceFirst("CODEC_ID_", ""));
                    log.debug("Video Codec: {}",videoInfo.getVideoCodec());
                    videoInfo.setWidth(coder.getWidth());
                    log.debug("Width: {}",videoInfo.getWidth());
                    videoInfo.setHeight(coder.getHeight());
                    log.debug("Height: {}",videoInfo.getHeight());
                    videoInfo.setVideoFormat(coder.getPixelType().toString());
                    log.debug("Video Format: {}",videoInfo.getVideoFormat());
                    videoInfo.setFrameRate(coder.getFrameRate().getDouble());
                    log.debug("Frame Rate: {}",videoInfo.getFrameRate());
                    break;
            }

            log.debug("Type: {}", coder.getCodecType());
            videoInfo.setStartTime(container.getStartTime());
            log.info("Start Time: {}", videoInfo.getStartTime());

            log.trace("*** End of Stream Info ***");
        }

        return videoInfo;
    }

    public long getNumStreams() {
        return numStreams;
    }

    private void setNumStreams(long numStreams) {
        this.numStreams = numStreams;
    }

    public long getDuration() {
        return duration;
    }

    private void setDuration(long duration) {
        this.duration = duration;
    }

    public long getFileSize() {
        return fileSize;
    }

    private void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getBitRate() {
        return bitRate;
    }

    private void setBitRate(long bitRate) {
        this.bitRate = bitRate;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    private void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public long getStartTime() {
        return startTime;
    }

    private void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    private void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getChannels() {
        return channels;
    }

    private void setChannels(int channels) {
        this.channels = channels;
    }

    public String getAudioFormat() {
        return audioFormat;
    }

    private void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public double getFrameRate() {
        return frameRate;
    }

    private void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    private void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public String getVideoFormat() {
        return videoFormat;
    }

    private void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }
}
