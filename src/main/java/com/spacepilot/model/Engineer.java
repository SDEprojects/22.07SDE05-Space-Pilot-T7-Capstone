package com.spacepilot.model;

public class Engineer extends Person {

  public Engineer(){
    super();
  }

  public Engineer(String name, Planet currentPlanet) {
    super(name, currentPlanet.getName());
  }

  public void repairSpacecraft(Spacecraft spacecraft) {
    spacecraft.setHealth(100);
  }

}
