package com.spacepilot.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacepilot.Main;
import com.spacepilot.model.Game;
import com.spacepilot.view.Gui;
import com.spacepilot.model.Planet;
import com.spacepilot.model.Spacecraft;
import com.spacepilot.view.View;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class Controller {

  public static Game game; // model, where the current state of the game is stored
  private BufferedReader reader; // buffered reader used to read in what user enters
  private Gui gui;
  private int repairCounter = 0;
  private int refuelCounter = 3;
  private String userInput;



  public Controller(Game game, BufferedReader reader, Gui gui) {
    this.reader = reader;
    this.gui = gui;
    this.game = game;
  }

  public void play()
      throws IOException, URISyntaxException, MidiUnavailableException, InvalidMidiDataException, InterruptedException {
    //creates all the different Gui components/sections
    gui.createSectionsOfGui();

    //starts Gui and shows titleScreen
    gui.showGuiStart();

    //Redirects all SOUT to gui
    View.consoleToGUI(gui);

    // create and set up game environment
    setUpGame();

    // display game's introduction with flash screen and story and prompt the user to continue
    gameIntro();



    displayCurrentPlanetStatus();
    displayGameStatusPanel();
    userInput ="";
    textParser(userInput);

  }


//KEEP DIS
  public void setUpGame() throws URISyntaxException, IOException {
    // create planets based on planets' json files and set them as the current game's planets
    game.setPlanets(createPlanets());
    // for each planet in the current game
    for (Planet planet : game.getPlanets()) {
      // place random number of astronauts on each planet
      planet.placeAstronauts(planet);
      // and increment the number of total astronauts by the number of astronauts on each planet
      game.setTotalNumberOfAstronauts(
          game.getTotalNumberOfAstronauts() + planet.getNumOfAstronautsOnPlanet());
    }
    // create a new spacecraft instance for the current game, using data from a .json file
    game.setSpacecraft(createSpacecraft());
    // set the current spacecraft's current planet to be Earth
    game.getSpacecraft().setCurrentPlanet(returnPlanet("earth"));

  }

  public void gameIntro() throws IOException, InterruptedException {
    View.getGameTextJson();

    // display introductory background story
    View.printIntro();

    // display game instructions (how-to-play)
    View.printInstructions();
  }

  public void textParser(String text) {
    String[] result = new String[2];
    String[] splitText = text.split(" ");
    String verb = splitText[0]; // First word
    String noun = splitText[splitText.length - 1]; // Last word
    result[0] = verb;
    result[1] = noun;
    try {
      nextMove(result);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void nextMove(String[] command) throws IOException, InterruptedException {
    Spacecraft spacecraft = game.getSpacecraft();

    if (command[0].equals("quit")) {
      game.setOver(true);

    } else if (command[0].equals("help")) {
      View.printInstructions();

    } else if (command[0].equals("save")) {
      saveGame(game);

    } else if (command[0].equals("continue")) {
      loadSavedGame();

    } else if (command[0].equals("go")) {

      if (spacecraft.getFuel() == 0) {
        View.printYourSpacecraftIsOutOfFuelAndYouLose();
        game.setOver(true);
      }
      // if the user is trying to go to the current planet
      if (command[1].equals(game.getSpacecraft().getCurrentPlanet().getName())) {
        View.printSamePlanetAlert();
      } else { //Check if planet exists or return null
        Planet destinationPlanet = returnPlanet(command[1]);
        //this method will decrement fuel by 10 on each move

        if (destinationPlanet == null) {
          View.printInvalidDestination();
        } else { //Check chances of random damage event
          String event = destinationPlanet.randomEncounter();

          if (event != null) {
            // decrement spacecraft health by 1.
            spacecraft.setHealth(spacecraft.getHealth() - 15);
            gui.getShipHealthBar().setValue(gui.getHealth() - 15);
            gui.setHealth(gui.getHealth() - 15);
            gui.getShipHealthBar().setString("Health" + gui.getHealth() + "%");
            // alert the user about the event
            View.printEventAlert(event);
          }
          //Check if planet has prereq/damageCondition that causes damage to ship
          String preReq = destinationPlanet.getPreReq();

          spacecraft.setCurrentPlanet(returnPlanet(command[1]));

          if (command[1].equals("orbit") && !gui.getTicktock().getHealthTickerBoolean()) {
            gui.getTicktock().getHealthTimer().stop();
            gui.getTicktock().setHealthTickerBoolean(true);
          } else if (preReq != null && !spacecraft.getInventory().contains(preReq)) {
            if (command[1].equals("venus")
                || command[1].equals("uranus")) {
              gui.getTicktock().healthTickTimer();
              gui.getTicktock().getHealthTimer().start();
              gui.getTicktock().setHealthTickerBoolean(false);
            }
            String damageCondition = destinationPlanet.getDamageCondition();
            View.printDamageConditionAlert(damageCondition, destinationPlanet.getName(), preReq);
          } else if (preReq != null && spacecraft.getInventory().contains(preReq)) {
            //Call string to show that you avoided damage by having correct preReq in inventory
            String damageCondition = destinationPlanet.getDamageCondition();
            View.printDamageConditionAvoidedAlert(preReq, damageCondition);
            //set planet preReq to null
            destinationPlanet.setPreReq(null);
            //set damageCondition to null
            destinationPlanet.setDamageCondition(null);
            //remove item from inventory as it's used.
            spacecraft.getInventory().remove(preReq);
          }
        }
      }

    } else if (command[0].equals("chat")) {
      View.printNPCDialogue();

    } else if (command[0].equals("repair")) {
      game.getSpacecraft().typeAndNumOfPassengersOnBoard();
      int engineerCount = game.getSpacecraft().getNumOfEngineersOnBoard();
      if (engineerCount == 0) {
        View.printNoEngineerAlert();
        return;
      }

      if (gui.getHealth() == 100) {
        View.printYourHealthisFullAlready();
      } else if (repairCounter < 3) {
        gui.getShipHealthBar().setValue(100);
        gui.setHealth(100);
        gui.getShipHealthBar().setString("Health: " + gui.getHealth() + "%");
        View.printRepair();

        repairCounter++;
      } else if (repairCounter >= 3) {
        View.printRepairLimit();
      }

    } else if (command[0].equals("load")) {

      loadNewPassengers();

    } else if (command[0].equals("unload")) {
      unloadPassengersOnEarth();

    } else if (command[0].equals("refuel")) {
      refuelShip();
    } else if (command[0].equals("god")){
        godMode();
    } else if (command[0].equals("interact")) {
      interactAlien();
    } else { // invalid command message
      View.printInvalidCommandAlert();
    }

    //update status and check if we won or lost
    displayCurrentPlanetStatus();
    displayGameStatusPanel();
    checkGameResult();
  }

  public void displayCurrentPlanetStatus() {
    //Calls gui method to display current status of planet user is on.
    gui.displayPlanetStatus(
        game.getSpacecraft().getCurrentPlanet().getItem(),
        game.getSpacecraft().getCurrentPlanet().getDamageCondition(),
        game.getSpacecraft().getCurrentPlanet().getNumOfAstronautsOnPlanet() );
  }
  private  void displayGameStatusPanel() {
    Collection<String> inventory = game.getSpacecraft().getInventory();
    Planet planet = game.getSpacecraft().getCurrentPlanet();
    int strandedAstos = game.calculateRemainingAstronautsViaTotalNumOfAstronauts()
        - returnPlanet("Earth").getNumOfAstronautsOnPlanet();
    gui.displayGameStatus(inventory, planet, repairCounter, strandedAstos);
  }


  public  void loadNewPassengers() {
    Collection<Object> arrayOfAstronautsOnCurrentPlanet = game.getSpacecraft().getCurrentPlanet()
        .getArrayOfAstronautsOnPlanet();
    if (arrayOfAstronautsOnCurrentPlanet.size() <= 0) {
      View.printNoAstronautsToLoad();
    }
    if (game.getSpacecraft().getCurrentPlanet().getName().equals("Earth")) {
      View.printCannotRemovePeopleFromEarth();
    }
    //conditional to prevent player from loading passengers while preReq is still active.
    String preReq = game.getSpacecraft().getCurrentPlanet().getPreReq();
    List<String> inventory = game.getSpacecraft().getInventory();
    if (preReq != null) {
      View.printCantLoadWithoutPreReqAlert(preReq);
      return;
    }
    if (arrayOfAstronautsOnCurrentPlanet.size() > 0 && !game.getSpacecraft().getCurrentPlanet()
        .getName().equals("Earth")) {
      //adds astronauts from planet to spacecraft
      game.getSpacecraft().addPassengers(arrayOfAstronautsOnCurrentPlanet);

      //gets item from planet, adds to spacecraft inventory
      String item = game.getSpacecraft().getCurrentPlanet().getItem();
      game.getSpacecraft().addToInventory(item);
      game.getSpacecraft().getCurrentPlanet().setItem(null);

      game.getSpacecraft().typeAndNumOfPassengersOnBoard();
      determineIfEngineerIsOnBoard();
    }
  }

  public void determineIfEngineerIsOnBoard() {
    if (game.getSpacecraft().getNumOfEngineersOnBoard() > 0) {
      View.printYouveGotAnEngineer();
    } else {
      View.printYouHaventGotAnEngineerOnBoard();
    }
  }

  public void unloadPassengersOnEarth() {
    Planet currentPlanet = game.getSpacecraft().getCurrentPlanet();
    Spacecraft spacecraft = game.getSpacecraft();

    if (currentPlanet.getName().equals("Earth")) {
      currentPlanet.getArrayOfAstronautsOnPlanet().addAll(game.getSpacecraft().getPassengers());
      spacecraft.getPassengers().clear();
      checkGameResult();

    } else {
      View.printYouCantUnloadPassengersIfCurrentPlanetNotEarth();
    }
  }

  public void refuelShip() {
    Planet currentPlanet = game.getSpacecraft().getCurrentPlanet();

    if (!currentPlanet.getName().equals("Station")) {
      View.printYouCanOnlyRefuelAtTheStation();
    } else if (gui.getFuel() == 100) {
      View.printYourFuelTankIsFullAlready();
    } else if (currentPlanet.getName().equals("Station") && refuelCounter == 0) {
      View.printStationHasNoMoreFuelAvailable();
    } else if (currentPlanet.getName().equals("Station") && refuelCounter > 0) {
      gui.getFuelLevelBar().setValue(100);
      gui.setFuel(100);
      gui.getFuelLevelBar().setString("Fuel: " + gui.getFuel() + "%");
      refuelCounter--;
      View.printSpacecraftHasBeenFilled();
    }
  }

  public void interactAlien() {
    Spacecraft spacecraft = game.getSpacecraft();
    Planet destinationPlanet = returnPlanet(spacecraft.getCurrentPlanet().getName());
    String preReq = destinationPlanet.getPreReq();
    if (preReq != null && !spacecraft.getInventory().contains(preReq)) {
      gui.getShipHealthBar().setValue(gui.getHealth() - 45);
      gui.setHealth(gui.getHealth() - 45);
      gui.getShipHealthBar().setString("Health" + gui.getHealth() + "%");
    } else if (preReq != null && spacecraft.getInventory().contains(preReq)) {
      //Call string to show that you avoided damage by having correct preReq in inventory
      String damageCondition = destinationPlanet.getDamageCondition();
      View.printDamageConditionAvoidedAlert(preReq, damageCondition);
      //set planet preReq to null
      destinationPlanet.setPreReq(null);
      //set damageCondition to null
      destinationPlanet.setDamageCondition(null);
      //remove item from inventory as it's used.
      spacecraft.getInventory().remove(preReq);
    }
  }

  //gives the user all the astronauts, items, and sets health and fuel to 100
  public void godMode(){
    for (Planet planet : game.getPlanets()) {
      Collection<Object> astronauts = planet.getArrayOfAstronautsOnPlanet();
      game.getSpacecraft().addPassengers(astronauts);
      planet.removeAllAstronauts();
      // adds item from each planet
      String item = planet.getItem();
      game.getSpacecraft().addToInventory(item);
      planet.setItem(null);
    }
    //full health and fuel set to 100
    game.getSpacecraft().setFuel(100);
    game.getSpacecraft().setHealth(100);
  }

//TODO: Expand checkGameResult to have all fail conditions
  public void checkGameResult() {
    int numRescuedPassengers = returnPlanet("earth").getNumOfAstronautsOnPlanet();
    int totalNumberOfPersonsCreatedInSolarSystem = game.getTotalNumberOfAstronauts();
    boolean userWon = (double) numRescuedPassengers / totalNumberOfPersonsCreatedInSolarSystem
        >= (double) 5 / 5;

    if (!userWon) {
      return;
    } else if (userWon) {
      game.setOver(true);
      View.printGameOverMessage(userWon);
    }
  }

  public static void loadSavedGame() {
    try (Reader reader = Files.newBufferedReader(Paths.get("./saved-game.json"))) {
      Game savedGame = new Gson().fromJson(reader, Game.class);
      if (savedGame != null) { // if there is a saved game data
        game = savedGame;
        View.printLoadGameResult(true);
      } else {
        View.printLoadGameResult(false);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void saveGame(Game game) throws IOException {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    FileWriter writer = new FileWriter("saved-game.json");
    writer.write(gson.toJson(game));
    writer.close();
    View.printSaveGameMessage();
  }




  /*
  HELPER METHODS
   */


  // Returns an instance of the desired planet when given a planet name
  // If the desired planet by the name does not exist, returns null
  public static Planet returnPlanet(String destination) {
    // capitalize the destination
    String planetName =
        destination.substring(0, 1).toUpperCase() + destination.substring(1).toLowerCase();
    for (Planet planet : game.getPlanets()) {
      if (planet.getName().equals(planetName)) {
        return planet;
      }
    }
    return null;
  }

  public static Spacecraft createSpacecraft() {
    // create a reader
    try (Reader reader = new InputStreamReader(
        Main.class.getResourceAsStream("/spacecraft.json"))) {
      // convert JSON file to Spacecraft
      return new Gson().fromJson(reader, Spacecraft.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<Planet> createPlanets() throws URISyntaxException, IOException {

    List<Planet> planets = new ArrayList<>();
    List<String> planetNames = new ArrayList<>();
    planetNames.add("/planets/earth.json");
    planetNames.add("/planets/moon.json");
    planetNames.add("/planets/mars.json");
    planetNames.add("/planets/mercury.json");
    planetNames.add("/planets/uranus.json");
    planetNames.add("/planets/venus.json");
    planetNames.add("/planets/jupiter.json");
    planetNames.add("/planets/saturn.json");
    planetNames.add("/planets/neptune.json");
    planetNames.add("/planets/station.json");
    planetNames.add("/planets/orbit.json");


    for (String planetPath : planetNames) {
      try (Reader reader = new InputStreamReader(
          Main.class.getResourceAsStream(planetPath))) {
        planets.add(new Gson().fromJson(reader, Planet.class));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return planets;
  }

}
