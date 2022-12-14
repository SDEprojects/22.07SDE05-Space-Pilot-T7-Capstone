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

  private String item;

  private String[] names;
  private String preReq;
  private String damageCondition;

  public String getPreReq() {
    return preReq;
  }

  public void setPreReq(String preReq) {
    this.preReq = preReq;
  }

  public String getDamageCondition() {
    return damageCondition;
  }

  public void setDamageCondition(String damageCondition) {
    this.damageCondition = damageCondition;
  }

  private Collection<Object> arrayOfAstronautsOnPlanet = new ArrayList<Object>();
  Planet(String name, String event, int gravity, int radius, int mass) {
    this.name = name;
    this.event = event;
    this.gravity = gravity;
    this.radius = radius;
    this.mass = mass;
    this.item = item;
    int totalNumOfAstronautsOnPlanet = new Random().nextInt(5);

    for (int i = 0; i <= totalNumOfAstronautsOnPlanet; i++) {
      if (!name.equals("Earth")) {
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

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = item;
  }

  public int getNumOfAstronautsOnPlanet() {
    return arrayOfAstronautsOnPlanet.size();
  }

  public Collection<Object> getArrayOfAstronautsOnPlanet() {
    return arrayOfAstronautsOnPlanet;
  }

  public void removeAstronaut(Object passengerRemoved) {
      arrayOfAstronautsOnPlanet.remove(passengerRemoved);
  }
  public void removeAllAstronauts(){
    arrayOfAstronautsOnPlanet.removeAll(arrayOfAstronautsOnPlanet);
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
    int totalNumOfAstronautsOnPlanet = rand.nextInt(5);
    // check to make sure the current planet is not Earth
    if (!name.equals("Earth") && !name.equals("Orbit")) {
      // place stranded astronauts to the planet
      for (int i = 0; i <= totalNumOfAstronautsOnPlanet; i++) {
        // create a random boolean
        boolean randBool = rand.nextBoolean();
        // placeholder for a person (either engineer or non-engineer)
        Person person;
        // if the random boolean was true
        if (randBool) {
          // grab a random names
          String randomName = names[rand.nextInt(4)];
          // create an engineer
          person = new Engineer(randomName, currentPlanet);
        } else { // if the random boolean was false
          // create a non-engineer astronaut
          person = new Person();
        }
        // add either engineer or non-engineer to the array of astronauts on this planet
        arrayOfAstronautsOnPlanet.add(person);
        ;

      }
    }
  }
  }


