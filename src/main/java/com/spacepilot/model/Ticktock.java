package com.spacepilot.model;

import com.spacepilot.view.Gui;
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
  private Boolean healthTickerBoolean = true;

  public void ticktock() {
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

  public void healthTickTimer() {
    healthTimer = new Timer(250, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Gui.getShipHealthBar().setValue(Gui.getHealth() - 1);
        Gui.setHealth(Gui.getHealth() - 1);
        Gui.getShipHealthBar().setString("Health" + Gui.getHealth() + "%");

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

  public Timer getHealthTimer() {
    return healthTimer;
  }

  public void setHealthTimer(Timer healthTimer) {
    this.healthTimer = healthTimer;
  }

  public Boolean getHealthTickerBoolean() {
    return healthTickerBoolean;
  }

  public void setHealthTickerBoolean(Boolean healthTickerBoolean) {
    this.healthTickerBoolean = healthTickerBoolean;
  }

}
