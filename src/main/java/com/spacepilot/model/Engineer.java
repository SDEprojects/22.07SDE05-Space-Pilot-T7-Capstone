package com.spacepilot.model;

public class Engineer extends Person {

  public Engineer(){}
  public Engineer(String name, Planet currentPlanet) {
    super(name, currentPlanet);
  }

  public static void repairSpacecraft(Spacecraft spacecraft) {
    int currentScHealth = spacecraft.getHealth();
    spacecraft.setHealth(currentScHealth + 1);
  }

}
