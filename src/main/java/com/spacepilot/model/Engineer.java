package com.spacepilot.model;

public class Engineer extends Person {

  public Engineer(String name, Planet currentPlanet) {
    super(name, currentPlanet.getName());
  }

  public static void repairSpacecraft(Spacecraft spacecraft) {
    int currentScHealth = spacecraft.getHealth();
    spacecraft.setHealth(currentScHealth + 1);
  }

}
