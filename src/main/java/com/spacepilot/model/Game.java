package com.spacepilot.model;

import java.util.List;

public class Game {

  //FIELDS
  private boolean isOver;
  private Spacecraft spacecraft;
  private int totalNumberOfAstronauts;
  private List<Planet> planets;
  private int startingRefuels;
  private int remainingRefuels;
  private int startingRepairs;
  private int remainingRepairs;


  //METHODS
  public int calculateRemainingAstronautsViaTotalNumOfAstronauts() {
    int totalNumberOfAstronauts = getTotalNumberOfAstronauts()-spacecraft.getPassengers().size();
    return totalNumberOfAstronauts;
  }


  //GETTERS AND SETTERS
  public boolean isOver() { // Getter for isOver
    return isOver;
  }

  public void setOver(boolean over) {
    isOver = over;
  }

  public Spacecraft getSpacecraft() {
    return spacecraft;
  }

  public void setSpacecraft(Spacecraft spacecraft) {
    this.spacecraft = spacecraft;
  }

  public int getTotalNumberOfAstronauts() {
    return totalNumberOfAstronauts;
  }

  public void setTotalNumberOfAstronauts(int totalNumberOfAstronauts) {
    this.totalNumberOfAstronauts = totalNumberOfAstronauts;
  }

  public List<Planet> getPlanets() {
    return planets;
  }

  public void setPlanets(List<Planet> planets) {
    this.planets = planets;
  }

  public int getStartingRefuels() {
    return startingRefuels;
  }

  public void setStartingRefuels(int startingRefuels) {
    this.startingRefuels = startingRefuels;
  }

  public int getRemainingRefuels() {
    return remainingRefuels;
  }

  public void setRemainingRefuels(int remainingRefuels) {
    this.remainingRefuels = remainingRefuels;
  }

  public int getStartingRepairs() {
    return startingRepairs;
  }

  public void setStartingRepairs(int startingRepairs) {
    this.startingRepairs = startingRepairs;
  }

  public int getRemainingRepairs() {
    return remainingRepairs;
  }

  public void setRemainingRepairs(int remainingRepairs) {
    this.remainingRepairs = remainingRepairs;
  }
}
