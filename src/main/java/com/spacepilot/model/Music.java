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
  public static void track3 (String wavFile) {
    clip.stop();
    playAudioMusic(wavFile);
    gainControl.setValue(-9.0f);
  }
  public static void track4 (String wavFile) {
    clip.stop();
    playAudioMusic(wavFile);
    gainControl.setValue(-9.0f);
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
