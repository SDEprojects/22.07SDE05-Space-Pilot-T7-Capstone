package com.spacepilot.model;

public class Person {

  private String name;
  private String currentLocation;

  Person() {
  }

  Person(String name, String planetName) {
    this.name = name;
    this.currentLocation = planetName;
  }

  public String getName() {
    return name;
  }

  public String getCurrentLocation() {
    return currentLocation;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCurrentLocation(String planetName) {
    this.currentLocation = planetName;
  }

}
