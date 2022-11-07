package com.spacepilot.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacepilot.Main;
import com.spacepilot.model.Game;
import com.spacepilot.model.Music;
import com.spacepilot.model.Planet;
import com.spacepilot.model.Spacecraft;
import com.spacepilot.view.Gui;
import com.spacepilot.view.View;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.Timer;

public class Controller {

  public static Game game; // model, where the current state of the game is stored
  private BufferedReader reader; // buffered reader used to read in what user enters
  private Gui gui;
  private int repairCounter = 2;
  private int refuelCounter = 3;
  private String userInput;

  private Timer healthTimer, alienTimer;
  private Boolean healthTimerBoolean = true;
  private Boolean alienTimerBoolean = true;


  public Controller(Game game, BufferedReader reader, Gui gui) {
    this.reader = reader;
    this.gui = gui;
    this.game = game;
  }

  //method to create a new game
  public static Game createNewGame() {
    // create a reader
    try (Reader reader = new InputStreamReader(Main.class.getResourceAsStream("/game.json"))) {
      // convert JSON file to Game and return the Game instance
      return new Gson().fromJson(reader, Game.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
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

//METHOD TO CREATE A NEW GAME OR CONTINUE GAME OR QUIT GAME

  public static void saveGame(Game game) throws IOException {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    FileWriter writer = new FileWriter("saved-game.json");
    writer.write(gson.toJson(game));
    writer.close();
    View.printSaveGameMessage();
  }

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

  public void play()
      throws IOException, URISyntaxException, MidiUnavailableException, InvalidMidiDataException, InterruptedException {

    //CONSUMER TIPS

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
    getStatusUpdateForBackgrounds();
    gui.getTicktock().setRunDis(new Runnable() {
      @Override
      public void run() {
        checkGameResult();
      }
    });
    userInput = "";
    textParser(userInput);

  }

  public void replay() throws URISyntaxException, IOException, InterruptedException {
    // create and set up game environment
    setUpGame();
    gui.showNewGameReplay();

    // display game's introduction with flash screen and story and prompt the user to continue
    gameIntro();

    //Update status panels to empty
    displayCurrentPlanetStatus();
    displayGameStatusPanel();

    //Reset fuel and health
    Spacecraft spacecraft = game.getSpacecraft();
    gui.getFuelLevelBar().setValue(spacecraft.getFuel());
    gui.getFuelLevelBar().setString("Fuel: " + spacecraft.getFuel() + "%");
    gui.getShipHealthBar().setValue(spacecraft.getHealth());
    gui.getShipHealthBar().setString("Health: " + spacecraft.getHealth() + "%");
    //needs to connect health and fuel to spacecraft model

//    resetting refuels and repairs
    repairCounter = 2;
    refuelCounter = 3;
    gui.imageUiReset();
    //Reset timer to 3 minutes
    gui.getTicktock().setMinutes(8);
    gui.getTicktock().setSeconds(1);
    gui.getTicktock().getTimer().start();
    gui.getTicktock().setRunDis(new Runnable() {
      @Override
      public void run() {
        checkGameResult();
      }
    });
  }

  public void newGameInitialization() {
    try (Reader input =
        new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input)) {
      //Creates new game from model
      game = createNewGame();
      //Sets up gui and controller again
      replay();
      //Shows correct gui panels
      gui.showGameScreenPanels();//Takes you back to earth
    } catch (IOException | URISyntaxException e) {
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
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

    // display user to hurry
    View.printHurryUp();
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
    View.textSpacing();
    if (command[0].equals("quit")) {
      System.exit(0);
    } else if (command[0].equals("help")) {
//      View.printInstructions();

    } else if (command[0].equals("new")) {
      newGameInitialization();//creates new game?

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
            gui.getShipHealthBar().setValue(spacecraft.getHealth() - 15);
            spacecraft.setHealth(spacecraft.getHealth() - 15);
            gui.getShipHealthBar().setString("Health: " + spacecraft.getHealth() + "%");
            // alert the user about the event
            View.printEventAlert(event);
          }
          //Check if planet has prereq/damageCondition that causes damage to ship
          String preReq = destinationPlanet.getPreReq();

          spacecraft.setCurrentPlanet(returnPlanet(command[1]));
          Music.playAudioFX("sounds/Rocket_Ship.wav");
//deducts fuels from planets after moving
          if (!command[1].equals("orbit")) {
            gui.getFuelLevelBar().setValue((spacecraft.getFuel() - 10));
            spacecraft.setFuel((spacecraft.getFuel() - 10));
            gui.getFuelLevelBar().setString("Fuel: " + spacecraft.getFuel() + "%");
          }
          if (command[1].equals("orbit") && (!healthTimerBoolean || !alienTimerBoolean)) {
            if (!healthTimerBoolean) {
              healthTimer.stop();
              healthTimerBoolean = true;
              gui.removeWarningMessage();
            } else if (!alienTimerBoolean) {
              alienTimer.stop();
              alienTimerBoolean = true;
              gui.removeWarningMessage();
            }

          } else if (preReq != null && !spacecraft.getInventory().contains(preReq)) {
            if (command[1].equals("venus")
                || command[1].equals("uranus")) {
              if (command[1].equals("venus")) {
                gui.getWarningLabel().setForeground(Color.black);
                gui.getWarningLabel().setText(" NO GAS MASK! EVACUATE IMMEDIATELY!  ");
                Music.playAudioFX("sounds/Alarm.wav");//alarm
              } else {
                gui.getWarningLabel().setForeground(Color.GREEN);
                gui.getWarningLabel().setText("NO COLD SHIELD! EVACUATE IMMEDIATELY!");
                Music.playAudioFX("sounds/Alarm.wav");//alarm
              }
              healthTickTimer();
              healthTimer.start();
              healthTimerBoolean = false;
            }
            if (command[1].equals("venus")
                || command[1].equals("uranus")) {
              String damageCondition = destinationPlanet.getDamageCondition();
              View.printPlanetDamageConditionAlert(damageCondition, destinationPlanet.getName(),
                  preReq);
            }

          } else if (preReq != null && spacecraft.getInventory().contains(preReq)) {
            if (command[1].equals("venus")) {
              Music.playAudioFX("sounds/Gas_Mask.wav"); //gas mask
            }
            if (command[1].equals("uranus")) {
              Music.playAudioFX("sounds/Snow_Steps.wav"); //snow steps
            }
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

      if (spacecraft.getHealth() == 100) {
        View.printYourHealthisFullAlready();
      } else if (repairCounter > 0) {
        gui.getShipHealthBar().setValue(100);
        spacecraft.setHealth(100);
        gui.getShipHealthBar().setString("Health: " + spacecraft.getHealth() + "%");
        View.printRepair();
        Music.playAudioFX("sounds/Repair.wav");
        repairCounter--;
      } else if (repairCounter == 0) {
        View.printRepairLimit();
      }

    } else if (command[0].equals("load")) {

      loadNewPassengers();

    } else if (command[0].equals("unload")) {
      unloadPassengersOnEarth();

    } else if (command[0].equals("refuel")) {
      refuelShip();
    } else if (command[0].equals("god")) {
      godMode();
    } else if (command[0].equals("interact")) {
      if (spacecraft.getCurrentPlanet().getName().equals("Uranus") && spacecraft.getInventory().contains("Cold Shield")) {
        Music.playAudioFX("sounds/Ice_Break.wav"); //ice breaking
      } else if (spacecraft.getCurrentPlanet().getName().equals("Venus") && spacecraft.getInventory().contains("Gas Mask")) {
        Music.playAudioFX("sounds/Gas.wav"); //gas leak
      }
      if (alienTimerBoolean && healthTimerBoolean) {
        if (spacecraft.getCurrentPlanet().getName().equals("Saturn")) {
          gui.getWarningLabel().setForeground(Color.GREEN);
          gui.getWarningLabel().setText("NO ALIEN BABY! EVACUATE IMMEDIATELY! ");
          Music.playAudioFX("sounds/Growl.wav"); //growl
        } else if (spacecraft.getCurrentPlanet().getName().equals("Neptune")) {
          gui.getWarningLabel().setForeground(Color.green);
          gui.getWarningLabel().setText("   NO WEAPON! EVACUATE IMMEDIATELY   ");
          Music.playAudioFX("sounds/Growl.wav"); //growl
        }
        interactAlien();
      }
    } else { // invalid command message
//      View.printInvalidCommandAlert();
      return;
    }

    //update status and check if we won or lost
    displayGameStatusPanel();
    displayCurrentPlanetStatus();
    getStatusUpdateForBackgrounds();
    checkGameResult();
  }

  public void getStatusUpdateForBackgrounds() {
    gui.planetBackgroundUpdate(
        game.getSpacecraft().getCurrentPlanet().getItem(),
        game.getSpacecraft().getCurrentPlanet().getDamageCondition(),
        game.getSpacecraft().getCurrentPlanet().getNumOfAstronautsOnPlanet(),
        game.getSpacecraft().getCurrentPlanet().getName(),
        game.getSpacecraft().getInventory());
  }

  public void displayCurrentPlanetStatus() {
    //Calls gui method to display current status of planet user is on.
    gui.displayPlanetStatus(
        game.getSpacecraft().getCurrentPlanet().getItem(),
        game.getSpacecraft().getCurrentPlanet().getDamageCondition(),
        game.getSpacecraft().getCurrentPlanet().getNumOfAstronautsOnPlanet());
  }

  /*
  HELPER METHODS
   */

  private void displayGameStatusPanel() {
    Collection<String> inventory = game.getSpacecraft().getInventory();
    Planet planet = game.getSpacecraft().getCurrentPlanet();
    int strandedAstos = game.calculateRemainingAstronautsViaTotalNumOfAstronauts()
        - returnPlanet("Earth").getNumOfAstronautsOnPlanet();
    gui.displayGameStatus(inventory, planet, repairCounter, strandedAstos, refuelCounter);
  }

  public void loadNewPassengers() {
    Collection<Object> arrayOfAstronautsOnCurrentPlanet = game.getSpacecraft().getCurrentPlanet()
        .getArrayOfAstronautsOnPlanet();
    //If no astronauts, no load
    if (arrayOfAstronautsOnCurrentPlanet.size() <= 0) {
      View.printNoAstronautsToLoad();
    }
    //If on earth, no load
    if (game.getSpacecraft().getCurrentPlanet().getName().equals("Earth")) {
      View.printCannotRemovePeopleFromEarth();
    }
    //conditional to prevent player from loading passengers while preReq is still active.
    String preReq = game.getSpacecraft().getCurrentPlanet().getPreReq();
    List<String> inventory = game.getSpacecraft().getInventory();
    if (preReq != null && !game.getSpacecraft().getInventory().contains(preReq)) {
      View.printCantLoadWithoutPreReqAlert(preReq);
      return;
    }
    if (preReq != null && game.getSpacecraft().getInventory().contains(preReq)) {
      View.tellUserToInteractToClearDamageCondition(preReq,
          game.getSpacecraft().getCurrentPlanet().getDamageCondition());
      return;
    }

    if (arrayOfAstronautsOnCurrentPlanet.size() > 0 && !game.getSpacecraft().getCurrentPlanet()
        .getName().equals("Earth")) {
      //adds astronauts from planet to spacecraft
      game.getSpacecraft().addPassengers(arrayOfAstronautsOnCurrentPlanet);
      Music.playAudioFX("sounds/Success_Ding.wav"); //success

      //gets item from planet, adds to spacecraft inventory
      String item = game.getSpacecraft().getCurrentPlanet().getItem();
      game.getSpacecraft().addToInventory(item);
      if (item != null) {
        View.newItemReceived(item);
      }
      game.getSpacecraft().getCurrentPlanet().setItem(null);

      game.getSpacecraft().typeAndNumOfPassengersOnBoard();

      //set planet preReq to null
      game.getSpacecraft().getCurrentPlanet().setPreReq(null);
      //set damageCondition to null
      game.getSpacecraft().getCurrentPlanet().setDamageCondition(null);
      //remove item from inventory as it's used.
      game.getSpacecraft().getInventory().remove(preReq);
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
    Spacecraft spacecraft = game.getSpacecraft();

    if (!currentPlanet.getName().equals("Station")) {
      View.printYouCanOnlyRefuelAtTheStation();
    } else if (spacecraft.getFuel() == 100) {
      View.printYourFuelTankIsFullAlready();
    } else if (currentPlanet.getName().equals("Station") && refuelCounter == 0) {
      View.printStationHasNoMoreFuelAvailable();
    } else if (currentPlanet.getName().equals("Station") && refuelCounter > 0) {
      gui.getFuelLevelBar().setValue(100);
      spacecraft.setFuel(100);
      gui.getFuelLevelBar().setString("Fuel: " + spacecraft.getFuel() + "%");
      refuelCounter--;
      Music.playAudioFX("sounds/Fuel.wav");// success
      View.printSpacecraftHasBeenFilled();
    }
  }

  public void interactAlien() {
    Spacecraft spacecraft = game.getSpacecraft();
    Planet destinationPlanet = returnPlanet(spacecraft.getCurrentPlanet().getName());
    String preReq = destinationPlanet.getPreReq();
    if (preReq != null && !spacecraft.getInventory().contains(preReq)) {
      String damageCondition = destinationPlanet.getDamageCondition();
      gui.getShipHealthBar().setValue(spacecraft.getHealth() - 45);
      spacecraft.setHealth(spacecraft.getHealth() - 45);
      gui.getShipHealthBar().setString("Health: " + spacecraft.getHealth() + "%");
      View.printAlienDamageConditionAlert(damageCondition, destinationPlanet.getName(), preReq);
      alienInteractionTimer();
      alienTimer.start();
      alienTimerBoolean = false;
      Music.playAudioFX("sounds/Alarm.wav");//alarm
    } else if (preReq != null && spacecraft.getInventory().contains(preReq)) {
      //Call string to show that you avoided damage by having correct preReq in inventory
      String damageCondition = destinationPlanet.getDamageCondition();
      if (preReq.equals("Blaster")) {
        View.printAlienDestroyed(preReq, damageCondition);
        Music.playAudioFX("sounds/Blaster.wav");//blaster sound

      } else if (preReq.equals("Alien Baby")) {
        View.printAlienBabyReturned(preReq, damageCondition);
        Music.playAudioFX("sounds/Baby.wav");//alien baby
      }
      //set planet preReq to null
      destinationPlanet.setPreReq(null);
      //set damageCondition to null
      destinationPlanet.setDamageCondition(null);
      //remove item from inventory as it's used.
      spacecraft.getInventory().remove(preReq);

    }
  }

//METHOD TO CREATE A NEW GAME OR CONTINUE GAME OR QUIT GAME

  //gives the user all the astronauts, items, and sets health and fuel to 100
  public void godMode() {
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
    gui.getFuelLevelBar().setValue(game.getSpacecraft().getFuel());
    gui.getFuelLevelBar().setString("Fuel: " + 100 + "%");
    game.getSpacecraft().setHealth(100);
    gui.getShipHealthBar().setValue(game.getSpacecraft().getHealth());
    gui.getShipHealthBar().setString("Health: " + 100 + "%");

//    set timer to 8:00
    gui.getTicktock().setMinutes(8);
    gui.getTicktock().setSeconds(1);
  }

  public void checkGameResult() {
    int numRescuedPassengers = returnPlanet("earth").getNumOfAstronautsOnPlanet();
    int totalNumberOfPersonsCreatedInSolarSystem = game.getTotalNumberOfAstronauts();
    boolean userWon = (double) numRescuedPassengers / totalNumberOfPersonsCreatedInSolarSystem
        >= (double) 5 / 5;

    if (!userWon) {
      if (game.getSpacecraft().getFuel() < 0) {
        gui.getFuelLevelBar().setString("Fuel: " + 0 + "%");
//        game.setOver(true);
        gui.showGameOverLoseScreen();
      } else if (gui.getShipHealthBar().getValue() < 1) {
        gui.showGameOverLoseScreen();
        if (!healthTimerBoolean) {
          healthTimer.stop();
        } else if (!alienTimerBoolean) {
          alienTimer.stop();
        }

      } else if (gui.getTicktock().getOxygenTickerLose()) {
        gui.showGameOverLoseScreen();

      } else {
        return;
      }
    } else if (userWon) {
      gui.showGameOverWinScreen();
    }
  }

  public void healthTickTimer() {
    Spacecraft spacecraft = game.getSpacecraft();
    healthTimer = new Timer(250, new ActionListener() {//health timer should be 250
      @Override
      public void actionPerformed(ActionEvent e) {
        gui.getShipHealthBar().setValue((spacecraft.getHealth() - 1));
        spacecraft.setHealth((spacecraft.getHealth() - 1));
        gui.getShipHealthBar().setString("Health: " + spacecraft.getHealth() + "%");
        gui.warningMessage();
        if (spacecraft.getHealth() < 1) {
          healthTimer.stop();
          gui.getShipHealthBar().setValue(0);
          gui.getShipHealthBar().setString("Health: " + 0 + "%");
          spacecraft.setHealth(0);
          checkGameResult();
        }
      }
    });
  }

  public void alienInteractionTimer() {
    Spacecraft spacecraft = game.getSpacecraft();
    alienTimer = new Timer(250, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        gui.warningMessage();
      }

    });
  }

  public void updateFuel() {
    Spacecraft spacecraft = game.getSpacecraft();
    gui.getFuelLevelBar().setValue((spacecraft.getFuel() - 10));
    spacecraft.setFuel((spacecraft.getFuel() - 10));
    gui.getFuelLevelBar().setString("Fuel: " + spacecraft.getFuel() + "%");
  }
}
