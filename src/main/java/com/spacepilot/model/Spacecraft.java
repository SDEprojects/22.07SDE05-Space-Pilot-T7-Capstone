package com.spacepilot.model;

import java.util.ArrayList;
import java.util.Collection;

public class Spacecraft {

  private String name;
  private int health;
  private Planet currentPlanet;
  private Collection<Person> passengers = new ArrayList<Person>();
  private int numOfEngineersOnBoard;
  private int numOfNonEngineersOnBoard;
  private int totalPassengers;

  public String getName() {
    return name;
  }

  public int getHealth() {
    return health;
  }

  public Planet getCurrentPlanet() {
    return currentPlanet;
  }

  public Collection<Person> getPassengers() {
    return passengers;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public void setCurrentPlanet(Planet currentPlanet) {
    this.currentPlanet = currentPlanet;
  }

  public void setPassengers(Collection<Person> passengers) {
    this.passengers = passengers;
  }

  public int getNumOfEngineersOnBoard() {
    return numOfEngineersOnBoard;
  }

  public void addPassengers(Collection<Person> newPassengers) {
    getPassengers().addAll(newPassengers);
  }

  public void typeAndNumOfPassengersOnBoard() {
    for (Person passenger : passengers) {
      totalPassengers++;
      if (passenger.getClass().getSimpleName().equals("Engineer")) {
        numOfEngineersOnBoard++;
      }
      numOfNonEngineersOnBoard = totalPassengers - numOfEngineersOnBoard;
    }
  }

}