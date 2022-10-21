package com.spacepilot.model;

import java.util.ArrayList;
import java.util.Collection;

public class Spacecraft {

  private String name = "Bermoona Triangle";
  private int health = 2;
  private Planet currentPlanet;
  private Collection<Person> passengers = new ArrayList<Person>();
  private int numOfEngineersOnBoard = 0;
  private int numOfNonEngineersOnBoard = 0;
  private int totalPassengers = 0;

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