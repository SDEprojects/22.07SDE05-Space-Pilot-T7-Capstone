package com.spacepilot.model;

public class Game {

  private boolean isOver;
  private int remainingAstronauts;
  private int remainingDays;
  private int shipHealth; // TODO: Spacecraft has its own health so this is redundant
  private Spacecraft spacecraft;
  private Planet earth;
  private Planet moon;
  private Planet mars;
  private Planet mercury;

  public boolean isOver() { // Getter for isOver
    return isOver;
  }

  public void setOver(boolean over) {
    isOver = over;
  }

  public int getRemainingAstronauts() {
    return remainingAstronauts;
  }

  public void setRemainingAstronauts(int remainingAstronauts) {
    this.remainingAstronauts = remainingAstronauts;
  }

  public int getRemainingDays() {
    return remainingDays;
  }

  public void setRemainingDays(int remainingDays) {
    this.remainingDays = remainingDays;
  }

  public int getShipHealth() {
    return shipHealth;
  }

  public void setShipHealth(int shipHealth) {
    this.shipHealth = shipHealth;
  }

  public Spacecraft getSpacecraft() {
    return spacecraft;
  }

  public Planet getEarth() {
    return earth;
  }

  public Planet getMoon() {
    return moon;
  }

  public Planet getMars() {
    return mars;
  }

  public Planet getMercury() {
    return mercury;
  }

}
