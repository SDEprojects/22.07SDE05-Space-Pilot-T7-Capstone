package com.spacepilot.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Spacecraft {

  private String name;
  private int health;
  private Planet currentPlanet;
  private Collection<Object> passengers = new ArrayList<Object>();
  private int numOfEngineersOnBoard;
  private int numOfNonEngineersOnBoard;
  private int totalPassengers;

  private List<String> inventory = new ArrayList<>();

  private int spacecraftCapacity = 50;
  private double fuel;

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

  public double getFuel() {
    return fuel;
  }

  public void setFuel(double fuel) {
    this.fuel = fuel;
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

  public List<String> getInventory() {
    return inventory;
  }

  public void setInventory(List<String> inventory) {
    this.inventory = inventory;
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

  public void addToInventory(String item) {
    if(item != null){
      inventory.add(item);
    }
  }
}