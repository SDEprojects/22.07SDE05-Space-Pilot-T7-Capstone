package com.spacepilot.model;

public class Game {

  private boolean isOver;
  private int remainingAstronauts;
  private int remainingDays;
  private Spacecraft spacecraft;
  private Planet[] planets;

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

  public Spacecraft getSpacecraft() {
    return spacecraft;
  }

  public void setSpacecraft(Spacecraft spacecraft) {
    this.spacecraft = spacecraft;
  }

  public Planet[] getPlanets() {
    return planets;
  }

  public void setPlanets(Planet[] planets) {
    this.planets = planets;
  }

}
