package com.spacepilot.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Ticktock {


  private Timer timer, healthTimer;
  private int seconds;
  private int minutes;
  private String doubleDigitSeconds;
  private String doubleDigitMinutes;
  private DecimalFormat dFormat = new DecimalFormat("00");
  private JLabel oxygenTimeLeftLabel;
  private Boolean oxygenTickerLose = false;

  Runnable runDis;


  public void setRunDis(Runnable runDis) {
    this.runDis = runDis;
  }


  public void ticktock() {
    timer = new Timer(1000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        seconds--;
        doubleDigitSeconds = dFormat.format(seconds);
        doubleDigitMinutes = dFormat.format(minutes);
        oxygenTimeLeftLabel.setText(String.format("<html>Oxygen Remaining: <font color=#990000>%s:%2s</font></html>", doubleDigitMinutes, doubleDigitSeconds));

        if (seconds == -1){
          seconds = 59;
          minutes --;
          doubleDigitSeconds = dFormat.format(seconds);
          doubleDigitMinutes = dFormat.format(minutes);
          oxygenTimeLeftLabel.setText(String.format("<html>Oxygen Remaining: <font color=#990000>%s:%2s</font></html>", doubleDigitMinutes, doubleDigitSeconds));
        }
        if(minutes == 0 && seconds ==0){
          oxygenTickerLose = true;
          runDis.run();

        }
//        if(minutes == 2 && seconds == 55){
//          oxygenTickerLose = true;
//          runDis.run();
//
//        }

      }
    });
  }

  public Timer getTimer() {
    return timer;
  }

  public void setTimer(Timer timer) {
    this.timer = timer;
  }

  public int getSeconds() {
    return seconds;
  }

  public void setSeconds(int seconds) {
    this.seconds = seconds;
  }

  public int getMinutes() {
    return minutes;
  }

  public void setMinutes(int minutes) {
    this.minutes = minutes;
  }

  public String getDoubleDigitSeconds() {
    return doubleDigitSeconds;
  }

  public void setDoubleDigitSeconds(String doubleDigitSeconds) {
    this.doubleDigitSeconds = doubleDigitSeconds;
  }

  public String getDoubleDigitMinutes() {
    return doubleDigitMinutes;
  }

  public void setDoubleDigitMinutes(String doubleDigitMinutes) {
    this.doubleDigitMinutes = doubleDigitMinutes;
  }

  public DecimalFormat getdFormat() {
    return dFormat;
  }

  public void setdFormat(DecimalFormat dFormat) {
    this.dFormat = dFormat;
  }

  public JLabel getOxygenTimeLeftLabel() {
    return oxygenTimeLeftLabel;
  }

  public void setOxygenTimeLeftLabel(JLabel oxygenTimeLeftLabel) {
    this.oxygenTimeLeftLabel = oxygenTimeLeftLabel;
  }
  public Boolean getOxygenTickerLose() {
    return oxygenTickerLose;
  }
}
