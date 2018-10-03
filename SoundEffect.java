import java.io.*;
import javax.sound.sampled.*;

public class SoundEffect {
    //the source for audio data
    private AudioInputStream sample;
    private String name;

    //sound clip property is read-only here
    private Clip clip;
    public Clip getClip() { return clip; }

    //looping property for continuous playback
    private boolean looping = false;
    public void setLooping(boolean _looping) { looping = _looping; }

    public boolean getLooping() { return looping; }

    //repeat property used to play sound multiple times
    private int repeat = 0;
    public void setRepeat(int _repeat) { repeat = _repeat; }

    public int getRepeat() { return repeat; }

    private java.net.URL getURL(String filename) {
        java.net.URL url = null;
        try {
            url = this.getClass().getResource(filename);
        } catch (Exception e) { }
        return url;
    }

    public String getName()
    {
        return name;
    }

    public SoundEffect(String filename) {
        try {
            name = filename;
            sample = AudioSystem.getAudioInputStream(getURL(filename));
            clip = AudioSystem.getClip();
            clip.open(sample);
        } catch (Exception e) { }
    }

    public void play() {
        //exit if the sample hasn't been loaded
        if (!isLoaded())
        {
            System.out.println("The file is not worthy");
            return;
        }

        //reset the sound clip
        clip.setFramePosition(0);

        //play sample with optional looping
        if (looping)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        else
            clip.loop(repeat);
    }

    public void changeVolume(float amount)
    {
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue(amount);
    }

    public void stop() {
        clip.stop();
    }

    public boolean isLoaded() {
        return (boolean)(sample != null);
    }    
}