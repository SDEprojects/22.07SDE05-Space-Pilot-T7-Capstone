package model;

public class Game {

  private boolean isOver;
  private int remainingAstronauts;
  private int remainingDays;
  private int shipHealth;
  private Spacecraft spacecraft;
  private Planet earth;
  private Planet moon;
  private Planet mars;
  private Planet mercury;

  public Game() {
    isOver = false;
    remainingAstronauts = 5;
    remainingDays = 10;
    spacecraft = new Spacecraft();
    earth = new Planet("Earth", "WWIII", 9, 6300, 100);
    moon = new Planet("Moon", "asteroid", 2, 1000, 1);
    mars = new Planet("Mars", "sand storm", 4, 2100, 10);
    mercury = new Planet("Mercury", "solar flare", 4, 1500, 5);
    spacecraft.setCurrentPlanet(earth);
  }

  public boolean isOver() { // Getter for isOver
    return isOver;
  }

  public void setOver(boolean over) {
    isOver = over;
  }

  public int getRemainingAstronauts() {
    return remainingAstronauts;
  }

  public void setRemainingAstronauts(int remainingAstronauts) {
    this.remainingAstronauts = remainingAstronauts;
  }

  public int getRemainingDays() {
    return remainingDays;
  }

  public void setRemainingDays(int remainingDays) {
    this.remainingDays = remainingDays;
  }

  public int getShipHealth() {
    return shipHealth;
  }

  public void setShipHealth(int shipHealth) {
    this.shipHealth = shipHealth;
  }

  public Spacecraft getSpacecraft() {
    return spacecraft;
  }

  public Planet getEarth() {
    return earth;
  }

  public Planet getMoon() {
    return moon;
  }

  public Planet getMars() {
    return mars;
  }

  public Planet getMercury() {
    return mercury;
  }

}
