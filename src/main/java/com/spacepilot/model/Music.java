package com.spacepilot.model;

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
  private static String volume = "5";
  private static Boolean musicMute = true;
  private static Boolean fxMute = true;
  private static URL audio;

  public static void playAudioFX(String soundFile) {
    if (fxMute) {

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
  public static Void musicPlay() {
    if (!musicMute) {
      clip.start();
      musicMute = true;
    }
    return null;
  }
  public static Void musicPause() {
    if (musicMute) {
      clip.stop();
      musicMute = false;
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
  public static void startAudio() {
    clip2.start();
    clip2.loop(0);
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
  public static float currentVolume(){
    float currentVolume = 0;
    return currentVolume;
  }
  public static FloatControl gainControl(){
    gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    return gainControl;
  }
  public static Boolean getMusicMute() {
    return musicMute;
  }
  public static void setMusicMute(Boolean musicMute) {
    Music.musicMute = musicMute;
  }
}
