package model;

import java.util.Random;

public class Planet {

  private String name;
  private String event;
  private int gravity;
  private int radius;
  private int mass;

  Planet(String name, String event, int gravity, int radius, int mass) {
    this.name = name;
    this.event = event;
    this.gravity = gravity;
    this.radius = radius;
    this.mass = mass;
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

  public String randomEncounter() {
    Random rnd = new Random();
    int randomInt = rnd.nextInt(10);
    if (randomInt > 5) {
      return event;
    }
    return null;
  }
}
