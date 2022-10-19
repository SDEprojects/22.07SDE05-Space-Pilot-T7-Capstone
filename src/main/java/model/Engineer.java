package model;

public class Engineer extends Person {


  public Engineer(){

  }
  public Engineer(String name, Planet currentPlanet) {
    super(name, currentPlanet);
  }

  public void repairSpacecraft(Spacecraft spacecraft) {
    int currentScHealth = spacecraft.getHealth();
    spacecraft.setHealth(currentScHealth + 1);
  }

}
