package model;

public class Person {

  private String name;
  private Planet currentPlanet;

  Person(String name, Planet currentPlanet) {
    this.name = name;
    this.currentPlanet = currentPlanet;
  }

  public String getName() {
    return name;
  }

  public Planet getCurrentPlanet() {
    return currentPlanet;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCurrentPlanet(Planet currentPlanet) {
    this.currentPlanet = currentPlanet;
  }

}
