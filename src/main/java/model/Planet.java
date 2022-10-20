package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import model.Engineer;
import model.Game;
import model.Person;
import model.Spacecraft;

public class Planet {

  private String name;
  private String event;
  private int gravity;
  private int radius;
  private int mass;
  private final Collection<Person> arrayOfAstronautsOnPlanet = new ArrayList<>();


  Planet(String name, String event, int gravity, int radius, int mass) {
    this.name = name;
    this.event = event;
    this.gravity = gravity;
    this.radius = radius;
    this.mass = mass;
  }


  public String getName() {
    return name;
  }

  public int getGravity() {
    return gravity;
  }

  public int getRadius() {
    return radius;
  }

  public int getMass() {
    return mass;
  }

  public Collection<Person> getArrayOfAstronautsOnPlanet() {
    return arrayOfAstronautsOnPlanet;
  }

  public String randomEncounter() {
    Random rnd = new Random();
    int randomInt = rnd.nextInt(10);
    if (randomInt > 5) {
      return event;
    }
    return null;
  }

  public void createEngineer(Planet currentPlanet) {
    int totalNumOfAstronautsOnPlanet = new Random().nextInt(4);
    for (int i = 0; i <= totalNumOfAstronautsOnPlanet; i++) {
      if (!name.equals("Earth")) {
        if (i == 3) {
          String[] engineerNames = {"Bob", "Michelle", "Chad", "Aria"};
          int indexOfEngineerArrayForSettingName = new Random().nextInt(4);
          String engineerName = engineerNames[indexOfEngineerArrayForSettingName];
          Engineer engineerOnThisPlanet = new Engineer(engineerName, currentPlanet);
          arrayOfAstronautsOnPlanet.add(engineerOnThisPlanet);
        } else {
          Person nonEngineerOnThisPlanet = new Person();
          arrayOfAstronautsOnPlanet.add(nonEngineerOnThisPlanet);
        }
      }
    }
  }
}
