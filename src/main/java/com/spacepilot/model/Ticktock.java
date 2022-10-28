package com.spacepilot.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Ticktock {


  private static Timer timer;
  private static int seconds;
  private static int minutes;
  private static String doubleDigitSeconds;
  private static String doubleDigitMinutes;
  private static DecimalFormat dFormat = new DecimalFormat("00");
  private static JLabel oxygenTimeLeftLabel;

  public static void ticktock() {
    timer = new Timer(1000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        seconds--;
        doubleDigitSeconds = dFormat.format(seconds);
        doubleDigitMinutes = dFormat.format(minutes);
        oxygenTimeLeftLabel.setText("Oxygen Time Remaining: " +  doubleDigitMinutes + ":" + doubleDigitSeconds);

        if (seconds == -1){
          seconds = 59;
          minutes --;
          doubleDigitSeconds = dFormat.format(seconds);
          doubleDigitMinutes = dFormat.format(minutes);
          oxygenTimeLeftLabel.setText("Oxygen Time Remaining: " +  doubleDigitMinutes + ":" + doubleDigitSeconds);
        }
        if(minutes == 0 && seconds ==0){
          timer.stop();
        }

      }
    });
  }
  public static Timer getTimer() {
    return timer;
  }

  public static void setTimer(Timer timer) {
    Ticktock.timer = timer;
  }

  public static int getSeconds() {
    return seconds;
  }

  public static void setSeconds(int seconds) {
    Ticktock.seconds = seconds;
  }

  public static int getMinutes() {
    return minutes;
  }

  public static void setMinutes(int minutes) {
    Ticktock.minutes = minutes;
  }

  public static String getDoubleDigitSeconds() {
    return doubleDigitSeconds;
  }

  public static void setDoubleDigitSeconds(String doubleDigitSeconds) {
    Ticktock.doubleDigitSeconds = doubleDigitSeconds;
  }

  public static String getDoubleDigitMinutes() {
    return doubleDigitMinutes;
  }

  public static void setDoubleDigitMinutes(String doubleDigitMinutes) {
    Ticktock.doubleDigitMinutes = doubleDigitMinutes;
  }

  public static DecimalFormat getdFormat() {
    return dFormat;
  }

  public static void setdFormat(DecimalFormat dFormat) {
    Ticktock.dFormat = dFormat;
  }

  public static JLabel getOxygenTimeLeftLabel() {
    return oxygenTimeLeftLabel;
  }

  public static void setOxygenTimeLeftLabel(JLabel oxygenTimeLeftLabel) {
    Ticktock.oxygenTimeLeftLabel = oxygenTimeLeftLabel;
  }

}
