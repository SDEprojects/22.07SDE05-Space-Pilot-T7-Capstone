package com.spacepilot.model;

//import com.spacepilot.model.
public class Game {

  private boolean isOver;
  private int remainingDays;
  private Spacecraft spacecraft;

  private int totalNumberOfAstronauts;

  private Planet[] planets;

  public boolean isOver() { // Getter for isOver
    return isOver;
  }

  public void setOver(boolean over) {
    isOver = over;
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

  public int getTotalNumberOfAstronauts() {
    return totalNumberOfAstronauts;
  }

  public Planet[] getPlanets() {
    return planets;
  }

  public void setPlanets(Planet[] planets) {
    this.planets = planets;
  }

  public void countTotalNumberOfAstronautsOnPlanet() {
    for (Planet planet : getPlanets()) {
      totalNumberOfAstronauts += planet.getArrayOfAstronautsOnPlanet().size();
    }
  }

  public int calculateRemainingAstronautsViaTotalNumOfAstronauts() {
    int totalNumberOfAstronauts = getTotalNumberOfAstronauts();
    int numberOfAstronautsOnSc = spacecraft.getPassengers().size();
    int remainingNumberOfAstronautsToPickUp = totalNumberOfAstronauts - numberOfAstronautsOnSc;
    return remainingNumberOfAstronautsToPickUp;
  }

}
