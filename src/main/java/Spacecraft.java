import java.util.ArrayList;
import java.util.Collection;

public class Spacecraft {

  private static Spacecraft spacecraft = null;
  private String name = "Bermoona Triangle";
  private int health = 5;
  private Planet currentPlanet = Earth;
  private Collection<Person> passengers = new ArrayList<Person>();

  Spacecraft(){
    if(spacecraft == null){
      spacecraft = new Spacecraft();
    }
  }

  public String getName() {
    return name;
  }

  public int getHealth() {
    return health;
  }

  public Planet getCurrentPlanet() {
    return currentPlanet;
  }

  public Collection<Person> getPassengers() {
    return passengers;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public void setCurrentPlanet(Planet currentPlanet) {
    this.currentPlanet = currentPlanet;
  }

  public void setPassengers(Collection<Person> passengers) {
    this.passengers = passengers;
  }


}
