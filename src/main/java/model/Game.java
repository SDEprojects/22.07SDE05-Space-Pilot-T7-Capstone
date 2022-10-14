package model;

public class Game {

  private boolean isOver;
  private int remainingAstro;
  private int remainingDays;
  private int shipHealth;
  private Spacecraft spacecraft;

  public Game() {
    isOver = false;
    remainingAstro = 5;
    remainingDays = 10;
    spacecraft = new Spacecraft();
  }

  public boolean isOver() { // Getter for isOver
    return isOver;
  }

  public void setOver(boolean over) {
    isOver = over;
  }

  public int getRemainingAstro() {
    return remainingAstro;
  }

  public void setRemainingAstro(int remainingAstro) {
    this.remainingAstro = remainingAstro;
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

}

