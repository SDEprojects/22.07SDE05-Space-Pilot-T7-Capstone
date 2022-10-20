package com.spacepilot.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Planet {

  private String name;
  private String event;
  private int gravity;
  private int radius;
  private int mass;
  private String[] names;
  private Collection<Person> arrayOfAstronautsOnPlanet;

  Planet(String name, String event, int gravity, int radius, int mass) {
    this.name = name;
    this.event = event;
    this.gravity = gravity;
    this.radius = radius;
    this.mass = mass;
    this.names = new String[]{"Bob", "Michelle", "Chad", "Aria"};
    this.arrayOfAstronautsOnPlanet = new ArrayList<>();
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

  public void placeAstronauts(Planet currentPlanet) {
    Random rand = new Random();
    // random total number of astronauts on a planet
    int totalNumOfAstronautsOnPlanet = rand.nextInt(4);
    // check to make sure the current planet is not Earth
    if (!name.equals("Earth")) {
      // place stranded astronauts to the planet
      for (int i = 0; i <= totalNumOfAstronautsOnPlanet; i++) {
        // create a random boolean
        boolean randBool = rand.nextBoolean();
        // placeholder for a person (either engineer or non-engineer)
        Person person;
        // choose a random name
        String randomName = names[rand.nextInt(4)];
        // if the random boolean was true
        if (randBool) {
          // create an engineer
          person = new Engineer(randomName, currentPlanet);
        } else { // if the random boolean was false
          // create a non-engineer astronaut
          person = new Person(randomName, currentPlanet.getName());
        }
        // add either engineer or non-engineer to the array of astronauts on this planet
        arrayOfAstronautsOnPlanet.add(person);
      }
    }
  }

}
