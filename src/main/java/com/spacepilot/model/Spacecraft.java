package com.spacepilot.model;

import static com.spacepilot.view.Gui.currentPlanetLabel;
import static com.spacepilot.view.Gui.fuelLevelLabel;
import static com.spacepilot.view.Gui.inventoryLabel;
import static com.spacepilot.view.Gui.shipHealthLabel;

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
    shipHealthLabel.setText("Ship Health: "  + health);
    return health;
  }

  public Planet getCurrentPlanet() {
    currentPlanetLabel.setText("Current Planet: " + currentPlanet.getName());
    return currentPlanet;
  }

  public Collection<Object> getPassengers() {
    return passengers;
  }

  public void setHealth(int health) {
    shipHealthLabel.setText("Ship Health: "  + health);
    this.health = health;
  }

  public double getFuel() {
    fuelLevelLabel.setText("Fuel Level: " + fuel);
    return fuel;
  }

  public void setFuel(double fuel) {
    this.fuel = fuel;
    fuelLevelLabel.setText("Fuel Level: " + fuel);
  }

  public void setCurrentPlanet(Planet currentPlanet) {
    this.currentPlanet = currentPlanet;
    currentPlanetLabel.setText("Current Planet: " + currentPlanet.getName());
  }

  public void setPassengers(Collection<Object> passengers) {
    this.passengers = passengers;
  }

  public int getNumOfEngineersOnBoard() {
    return numOfEngineersOnBoard;
  }

  public List<String> getInventory() {
    inventoryLabel.setText("Inventory: " + inventory);
    return inventory;
  }

  public void setInventory(List<String> inventory) {
    this.inventory = inventory;
    inventoryLabel.setText("Inventory: " + inventory);
  }



  public void addPassengers(Collection<Object> newPassengers) {
    int passengersAdded = 0;
    Object[] boogity = newPassengers.toArray();
    for(Object passenger: boogity){
      if(++passengersAdded >  spacecraftCapacity){
        break;
      }else{
        passengers.add(passenger);
        currentPlanet.removeAstronaut(passenger);
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