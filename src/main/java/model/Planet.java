package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import model.Game;

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

    int totalNumOfAstronautsOnPlanet = new Random().nextInt(4);

    for (int i = 0; i <= totalNumOfAstronautsOnPlanet; i++) {
      if(!name.equals("Earth")){
        if (i == 3) {
          Person engineerOnThisPlanet = new Engineer();
          arrayOfAstronautsOnPlanet.add(engineerOnThisPlanet);
        } else {
          Person nonEngineerOnThisPlanet = new Person();
          arrayOfAstronautsOnPlanet.add(nonEngineerOnThisPlanet);
        }
      }
    }
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

//  public int getTotalNumOfAstronautsOnPlanet() {
//    return totalNumOfAstronautsOnPlanet;
//  }
  //array length

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

}
