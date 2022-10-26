package com.spacepilot.model;

import java.util.ArrayList;
import java.util.Collection;

public class Spacecraft {

  private String name;
  private int health;
  private Planet currentPlanet;
  private Collection<Object> passengers = new ArrayList<Object>();
  private int numOfEngineersOnBoard;
  private int numOfNonEngineersOnBoard;
  private int totalPassengers;
  private int spacecraftCapacity = 50;

  public String getName() {
    return name;
  }

  public int getHealth() {
    return health;
  }

  public Planet getCurrentPlanet() {
    return currentPlanet;
  }

  public Collection<Object> getPassengers() {
    return passengers;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public void setCurrentPlanet(Planet currentPlanet) {
    this.currentPlanet = currentPlanet;
  }

  public void setPassengers(Collection<Object> passengers) {
    this.passengers = passengers;
  }

  public int getNumOfEngineersOnBoard() {
    return numOfEngineersOnBoard;
  }

  public void addPassengers(Collection<Object> newPassengers) {
    int passengersAdded = 0;
    Object[] boogity = newPassengers.toArray();
    for(Object passenger: boogity){
      if(++passengersAdded >  spacecraftCapacity){
        break;
      }else{
        passengers.add(passenger);
        currentPlanet.removeAstronauts(passenger);
      }
    }
  }

  public void typeAndNumOfPassengersOnBoard() {
    for (Object passenger : passengers) {
      totalPassengers++;
      if (passenger.getClass().getSimpleName().equals("Engineer")) {
        numOfEngineersOnBoard++;
      }
      numOfNonEngineersOnBoard = totalPassengers - numOfEngineersOnBoard;
    }
  }

}