/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.org.makerbox.stopgo;

import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static com.xuggle.xuggler.Global.DEFAULT_TIME_UNIT;
import java.io.IOException;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static nz.org.makerbox.stopgo.CreateProject.dir_images;

 /**
 *
 * @author Seth Kenlon
 */
public class CreateVideo {
    public static void main(String[] args) {
        // frame being recorded
        long nextFrameTime=0;
        // video parameters
        final int videoStreamIndex = 0;
        final int videoStreamId = 0;
        final long frameRate = DEFAULT_TIME_UNIT.convert(500,MILLISECONDS);
        final int width = 720;
        final int height = 1280;
        // audio parameters
        final int audioStreamIndex = 1;
        final int audioStreamId = 0;
        final int channelCount = 1;
        final int sampleRate = 44100; // Hz
        final int sampleCount = 1000;
        
        try {
            final IMediaWriter writer = ToolFactory.makeWriter("stopgo.mp4");
            writer.addListener(ToolFactory.makeViewer(
                    IMediaViewer.Mode.VIDEO_ONLY, true,
                    javax.swing.WindowConstants.EXIT_ON_CLOSE));
            
            writer.addVideoStream(videoStreamIndex, videoStreamId, width, height);
            //writer.addAudioStream(audioStreamIndex, audioStreamId, channelCount, sampleRate);
            
            for (File f : dir_images.listFiles()) {
                BufferedImage frame = ImageIO.read(f);
                writer.encodeVideo(videoStreamIndex, frame, nextFrameTime, DEFAULT_TIME_UNIT);
                nextFrameTime += frameRate;
                
                /* audio 
                short[] samples = new short[];
                writer.encodeAudio(audioStreamIndex, samples, clock, DEFAULT_TIME_UNIT);
                totalSampleCount += sampleCount;
                */
            }
            
            writer.close();         
        } catch (IOException e) {
        }
    }
}
