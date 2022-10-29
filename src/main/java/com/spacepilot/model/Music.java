package com.spacepilot.model;

import com.spacepilot.view.View;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class Music {
  private static Clip clip;
  private static Clip clip2;
  public static FloatControl gainControl;
  private static int musicOnOff = 1;
  private static int fxOnOff = 1;
  private static String volume = "5";

  private static Boolean mute = false;
  private static Boolean musicMute = true;
  private static Boolean fxMute = true;
  private static URL audio;


  public static void playAudioFX(String soundFile) {
    if (fxOnOff == 1 || fxMute) {

      try {
        audio = com.spacepilot.model.Music.class.getResource("/" + soundFile);

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
      audio = com.spacepilot.model.Music.class.getResource("/" + soundFile);
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
    } else {
      View.printMusicOnlyOnAndOffCommandsAreAllowed();
    }


  }

  public static Void musicMute() {
    if (musicMute) {
      clip.stop();
      musicMute = false;
    } else if (!musicMute) {
      clip.start();
      musicMute = true;
    }
    return null;
  }

  public static void fxMute() {
    if (fxMute) {
      fxMute = false;
    } else if (!fxMute) {
      fxMute = true;
    }
  }

  public static void FXOnOff(String command) {
    if (command.equals("off")) {
      setFxOnOff(0);
    }
    if (command.equals("on")) {
      setFxOnOff(1);
    } else {
      View.printFXOnlyOnAndOffCommandsAreAllowed();
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

  public static String track1 (String wavFile) {
    clip.stop();
    playAudioMusic(wavFile);
    gainControl.setValue(-9.0f);
    return null;
  }
  public static String track2 (String wavFile) {
    clip.stop();
    playAudioMusic(wavFile);
    gainControl.setValue(-9.0f);
    return null;
  }
  public static void track3 () {
    clip.stop();
    playAudioMusic("Space_Cinematic.wav");
    gainControl.setValue(-9.0f);
  }
  public static void track4 () {
    clip.stop();
    playAudioMusic("Space_Cyber.wav");
    gainControl.setValue(-9.0f);
  }
  public static void trackChange(String command) {
    gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    if (command.equals("1")) {
      clip.stop();
      playAudioMusic("Space_Chill.wav");
      gainControl.setValue(-9.0f);
    } else if (command.equals("2")) {
      clip.stop();
      playAudioMusic("Space_Ambient.wav");
      gainControl.setValue(-9.0f);
    } else if (command.equals("3")) {
      clip.stop();
      playAudioMusic("Space_Cinematic.wav");
      gainControl.setValue(-9.0f);
    } else if (command.equals("4")) {
      clip.stop();
      playAudioMusic("Space_Cyber.wav");
      gainControl.setValue(-9.0f);
    } else {
      View.printTheTrackNumberDoesNotExist();
    }
  }

  public static void startBackgroundMusic() {

    if (musicOnOff == 0) {
      clip.stop();
      playAudioMusic("Space_Chill.wav");
      setMusicOnOff(1);
      gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-9.0f);
    } else {
      playAudioMusic("Space_Chill.wav");
      setMusicOnOff(0);
      gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-9.0f);
    }
  }

  public static void volumeUpDown(String command) {
    gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    if (command.equals("0")) {
      gainControl.setValue(gainControl.getMinimum());
      clip.start();
    } else if (command.equals("1")) {
      gainControl.setValue(-21.0f);
      clip.start();
    } else if (command.equals("2")) {
      gainControl.setValue(-18.0f);
      clip.start();
    } else if (command.equals("3")) {
      gainControl.setValue(-15.0f);
      clip.start();
    } else if (command.equals("4")) {
      gainControl.setValue(-12.0f);
      clip.start();
    } else if (command.equals("5")) {
      gainControl.setValue(-9.0f);
      clip.start();
    } else if (command.equals("6")) {
      gainControl.setValue(-6.0f);
      clip.start();
    } else if (command.equals("7")) {
      gainControl.setValue(-3.0f);
      clip.start();
    } else if (command.equals("8")) {
      gainControl.setValue(0.0f);
      clip.start();
    } else if (command.equals("9")) {
      gainControl.setValue(3.0f);
      clip.start();
    } else if (command.equals("10")) {
      gainControl.setValue(gainControl.getMaximum());
      clip.start();
    } else {
      View.printTheVolumeNumberDoesNotExist();
    }
  }

  public static float currentVolume(){
    float currentVolume = 0;
    return currentVolume;
  }

  public static FloatControl gainControl(){
    gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    return gainControl;
  }


  public static int getMusicOnOff() {
    return musicOnOff;
  }
  public static void setMusicOnOff(int musicOnOff) {
    com.spacepilot.model.Music.musicOnOff = musicOnOff;
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
    com.spacepilot.model.Music.fxOnOff = fxOnOff;
  }

}
