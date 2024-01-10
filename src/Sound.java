import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class Sound {
    private Clip clip;
    public Sound(String fileName) {
        // specify the sound to play
        // (assuming the sound can be played by the audio system)
        // from a wave File
        try (InputStream inStream = Sound.class.getClassLoader().getResourceAsStream(fileName+".wav")) {

            ClassLoader systemCL = Sound.class.getClassLoader();
            assert systemCL != null;

            InputStream bufferedIn = new BufferedInputStream(inStream);

            AudioInputStream sound = AudioSystem.getAudioInputStream(bufferedIn);
            // load the sound into memory (a Clip)
            clip = AudioSystem.getClip();
            clip.open(sound);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Malformed URL: " + e);
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Unsupported Audio File: " + e);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Input/Output Error: " + e);
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Line Unavailable Exception Error: " + e);
        }

        // play, stop, loop the sound clip
        /*clip.addLineListener(e -> {
            if(e.getType() == LineEvent.Type.STOP){
                e.getLine().close();
            }
        });*/
    }
    public void play() {
        clip.setFramePosition(0);  // Must always rewind!
        clip.start();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
    }
}