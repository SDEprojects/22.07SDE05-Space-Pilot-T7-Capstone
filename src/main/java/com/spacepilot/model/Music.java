package com.spacepilot.model;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;
import jm.JMC;

public final class Music implements JMC {

//  private static Sequencer sequencer;
//
//  public static void playMusic()
//      throws MidiUnavailableException, InvalidMidiDataException, IOException {
//
//    try {
//      sequencer = MidiSystem.getSequencer();
//      sequencer.open();
//      InputStream inputStream = Main.class.getResourceAsStream("/black-hole-audio.mp3.mid");
//      sequencer.setSequence(inputStream);
//      sequencer.start();
//    } catch (MidiUnavailableException e) {
//      throw new RuntimeException(e);
//    }
//  }
//
//  public static void stopMusic() {
//    sequencer.close();
//  }

  private static Clip clip;
  private static Clip clip2;
  static int musicOnOff = 1;

  static int fxOnOff = 1;

  private static String volume = "5";


  public static void playAudioFX(String soundFile) {
    if (fxOnOff == 1) {

      try {
        URL audio = Music.class.getResource("/" + soundFile);

        AudioInputStream audioInput = AudioSystem.getAudioInputStream(audio);
        clip2 = AudioSystem.getClip();
        clip2.open(audioInput);
        startAudio();

      } catch (UnsupportedAudioFileException e) {
      } catch (Exception e) {
      }
    }
  }

  public static void playAudioMusic(String soundFile) {

    try {
      URL audio = Music.class.getResource("/" + soundFile);
      AudioInputStream audioInput = AudioSystem.getAudioInputStream(audio);
      clip = AudioSystem.getClip();
      clip.open(audioInput);
      startBackgroundAudio();
    } catch (UnsupportedAudioFileException e) {
    } catch (Exception e) {
    }
  }

  public static void musicOnOff(String command) {
    if (command.equals("off")) {
      clip.stop();
      clip.flush();
    }
    if (command.equals("on")) {
      clip.stop();
      clip.flush();
      startBackgroundMusic();
    }
  }

  public static void FXOnOff(String command) {
    if (command.equals("off")) {
     setFxOnOff(0);
    }
    if (command.equals("on")) {
      setFxOnOff(1);
    }
  }

  public static void startAudio() throws InterruptedException {
    clip2.start();
    clip2.loop(0);
    clip2.wait();
  }

  public static void stopAudio() {
    clip.stop();
    clip.flush();
  }

  public static void startBackgroundAudio() throws InterruptedException {
    clip.start();
    clip.loop(Clip.LOOP_CONTINUOUSLY);
    clip.wait();
  }

  public static void playMove() {
    playAudioFX("Spacecraft_Move.wav");

  }

  public static void startBackgroundMusic() {
    if (musicOnOff == 0) {
      clip.stop();
      playAudioMusic("Space_Chill.wav");
      setMusicOnOff(1);
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-9.0f);
    } else {
      playAudioMusic("Space_Chill.wav");
      setMusicOnOff(0);
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-9.0f);
    }


  }

  public static void volumeUpDown(String command) {
    if (command.equals("0")) {
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(gainControl.getMinimum());
      clip.start();
    } else if (command.equals("1")) {
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-21.0f);
      clip.start();
    } else if (command.equals("2")) {
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-18.0f);
      clip.start();
    } else if (command.equals("3")) {
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-15.0f);
      clip.start();
    } else if (command.equals("4")) {
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-12.0f);
      clip.start();
    } else if (command.equals("5")) {
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-9.0f);
      clip.start();
    } else if (command.equals("6")) {
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-6.0f);
      clip.start();
    } else if (command.equals("7")) {
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-3.0f);
      clip.start();
    } else if (command.equals("8")) {
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(0.0f);
      clip.start();
    } else if (command.equals("9")) {
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(3.0f);
      clip.start();
    } else if (command.equals("10")) {
      FloatControl gainControl =
          (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(gainControl.getMaximum());
      clip.start();
    }
  }

  public static int getMusicOnOff() {
    return musicOnOff;
  }

  public static void setMusicOnOff(int musicOnOff) {
    Music.musicOnOff = musicOnOff;
  }

  public static String getVolume() {
    return volume;
  }

  public static void setVolume(String vol) {
    volume = vol;
  }

  public static int getFxOnOff() {
    return fxOnOff;
  }

  public static void setFxOnOff(int fxOnOff) {
    Music.fxOnOff = fxOnOff;
  }

}
