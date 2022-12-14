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
  //FIELDS
  public static Game game; // model, where the current state of the game is stored
  private BufferedReader reader; // buffered reader used to read in what user enters
  private Gui gui;
  private String userInput;
  private Timer healthTimer, alienTimer;
  private Boolean healthTimerBoolean = true;
  private Boolean alienTimerBoolean = true;


  //CONSTRUCTOR
  public Controller(Game game, BufferedReader reader, Gui gui) {
    this.reader = reader;
    this.gui = gui;
    this.game = game;
  }


//THESE METHODS HELP START AND SET UP THE GAME
  public static Game createNewGame() {
    // create a reader
    try (Reader reader = new InputStreamReader(Main.class.getResourceAsStream("/game.json"))) {
      // convert JSON file to Game and return the Game instance
      return new Gson().fromJson(reader, Game.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void loadSavedGame() {
    try (Reader reader = Files.newBufferedReader(Paths.get("./saved-game.json"))) {
      Game savedGame = new Gson().fromJson(reader, Game.class);
      if (savedGame != null) { // if there is a saved game data
        game = savedGame;
        View.printLoadGameResult(true);

        //Add checkmarks back for loaded game
        gui.addCheckMarksFromLoadGame(game.getPlanets());

        //Update status panels to empty
        displayCurrentPlanetStatus();
        displayGameStatusPanel();
        getStatusUpdateForBackgrounds();

        //Reset fuel and health
        Spacecraft spacecraft = game.getSpacecraft();
        gui.getFuelLevelBar().setValue(spacecraft.getFuel());
        gui.getFuelLevelBar().setString("Fuel: " + spacecraft.getFuel() + "%");
        gui.getShipHealthBar().setValue(spacecraft.getHealth());
        gui.getShipHealthBar().setString("Health: " + spacecraft.getHealth() + "%");

        //resets images in inventory
        gui.inventoryImagesReset();


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

        //Gets correct background screen
        gui.showGameScreenPanelsForContinuedGame(game.getSpacecraft().getCurrentPlanet().getName());

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


  //METHODS FOR MAIN GAME PLAY
  public void play()
      throws IOException, URISyntaxException, MidiUnavailableException, InvalidMidiDataException, InterruptedException {

    //CONSUMER TIPS
    //creates all the different Gui components/sections

    gui.createSectionsOfGui();

    //starts Gui and shows titleScreen
    gui.showGuiStart();

    //Set gui field to true to represent start of game
    gui.setInitialGame(true);

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
    //hide game over panel to show new game panel
    gui.showNewGameReplay();

    //reset completion checkmarks in map
    gui.removeCheckMarks();

    // display game's introduction with flash screen and story and prompt the user to continue
    gameIntro();

    //Update status panels to empty
    displayCurrentPlanetStatus();
    displayGameStatusPanel();
    getStatusUpdateForBackgrounds();

    //Reset fuel and health
    Spacecraft spacecraft = game.getSpacecraft();
    gui.getFuelLevelBar().setValue(spacecraft.getFuel());
    gui.getFuelLevelBar().setString("Fuel: " + spacecraft.getFuel() + "%");
    gui.getShipHealthBar().setValue(spacecraft.getHealth());
    gui.getShipHealthBar().setString("Health: " + spacecraft.getHealth() + "%");

    //resets images in inventory
    gui.inventoryImagesReset();
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
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
  public void nextMove(String[] command) throws IOException, InterruptedException {
    Spacecraft spacecraft = game.getSpacecraft();
    View.textSpacing();
    if (command[0].equals("quit")) {
      System.exit(0);
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
          //set current planet
          spacecraft.setCurrentPlanet(returnPlanet(command[1]));

          //Print string for arriving at planet or station
          if(command[1].equals("station")){
            View.printStationArrivalMessage(game.getStartingRefuels(), game.getRemainingRefuels());
          }else if(!command[1].equals("orbit")){
            //Send correct string if astronauts are present on planet
            String astronautsPresent = game.getSpacecraft().getCurrentPlanet().getNumOfAstronautsOnPlanet() >0 ? "some" : "no";
            //Send correct string if danger
            String dangerOnPlanet = game.getSpacecraft().getCurrentPlanet().getDamageCondition() != null ? game.getSpacecraft().getCurrentPlanet().getDamageCondition() : "no danger";
            View.printPlanetArrivalMessage(game.getSpacecraft().getCurrentPlanet().getName(), astronautsPresent,dangerOnPlanet );
          }
          Music.playAudioFX("sounds/Rocket_Ship.wav");
          //deducts fuels from planets after moving
          if (!command[1].equals("orbit")) {
            gui.getFuelLevelBar().setValue((spacecraft.getFuel() - 10));
            spacecraft.setFuel((spacecraft.getFuel() - 10));
            gui.getFuelLevelBar().setString("Fuel: " + spacecraft.getFuel() + "%");
          }
          if (command[1].equals("orbit") && (!healthTimerBoolean || !alienTimerBoolean)) {
            stopTimer();
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
      } else if (game.getRemainingRepairs() > 0) {
        gui.getShipHealthBar().setValue(100);
        spacecraft.setHealth(100);
        gui.getShipHealthBar().setString("Health: " + spacecraft.getHealth() + "%");
        View.printRepair();
        Music.playAudioFX("sounds/Repair.wav");
        game.setRemainingRepairs(game.getStartingRepairs()-1);
      } else if (game.getRemainingRepairs() == 0) {
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
    } else {
      return;
    }

    //update status and check if we won or lost
    displayGameStatusPanel();
    displayCurrentPlanetStatus();
    getStatusUpdateForBackgrounds();
    checkGameResult();
  }


  //METHODS FOR VIEW UPDATES
  public void getStatusUpdateForBackgrounds() {
    gui.planetBackgroundUpdate(
        game.getSpacecraft().getCurrentPlanet().getItem(),
        game.getSpacecraft().getCurrentPlanet().getDamageCondition(),
        game.getSpacecraft().getCurrentPlanet().getNumOfAstronautsOnPlanet(),
        game.getSpacecraft().getCurrentPlanet().getName(),
        game.getSpacecraft().getInventory(),
        game.getStartingRefuels(),
        game.getRemainingRefuels());
  }
  public void displayCurrentPlanetStatus() {
    //Calls gui method to display current status of planet user is on.
    gui.displayPlanetStatus(
        game.getSpacecraft().getCurrentPlanet().getItem(),
        game.getSpacecraft().getCurrentPlanet().getDamageCondition(),
        game.getSpacecraft().getCurrentPlanet().getNumOfAstronautsOnPlanet());
  }
  private void displayGameStatusPanel() {
    Collection<String> inventory = game.getSpacecraft().getInventory();
    Planet planet = game.getSpacecraft().getCurrentPlanet();
    int strandedAstos = game.calculateRemainingAstronautsViaTotalNumOfAstronauts()
        - returnPlanet("Earth").getNumOfAstronautsOnPlanet();
    int startingStrandedAstronauts = game.getTotalNumberOfAstronauts();
    int rescuedAstronauts = game.getSpacecraft().getPassengers().size();

    gui.displayGameStatus(inventory, planet, game.getRemainingRepairs(), game.getStartingRepairs(), startingStrandedAstronauts, rescuedAstronauts);
  }


  //TIMER STUFF
  public void alienInteractionTimer() {
    Spacecraft spacecraft = game.getSpacecraft();
    alienTimer = new Timer(250, e -> gui.warningMessage());
  }
  public void healthTickTimer() {
    Spacecraft spacecraft = game.getSpacecraft();
    //health timer should be 250
    healthTimer = new Timer(250, e -> {
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
    });
  }
  public void stopTimer() {
    if (!healthTimerBoolean) {
      healthTimer.stop();
      healthTimerBoolean = true;
      gui.removeWarningMessage();
    } else if (!alienTimerBoolean) {
      alienTimer.stop();
      alienTimerBoolean = true;
      gui.removeWarningMessage();
    }

  }


  /*
  HELPER METHODS
   */
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

    //planet is earth and you've collected all astronauts
    if (currentPlanet.getName().equals("Earth") && (game.getSpacecraft().getPassengers().size() == game.getTotalNumberOfAstronauts())){
      currentPlanet.getArrayOfAstronautsOnPlanet().addAll(game.getSpacecraft().getPassengers());
      spacecraft.getPassengers().clear();
      checkGameResult();

    }else if(currentPlanet.getName().equals("Earth") && !(game.getSpacecraft().getPassengers().size() == game.getTotalNumberOfAstronauts())){
      View.cantUnloadOnEarthWithoutAllAstronauts(game.calculateRemainingAstronautsViaTotalNumOfAstronauts());
    }else {
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
    } else if (currentPlanet.getName().equals("Station") && game.getRemainingRefuels() == 0) {
      View.printStationHasNoMoreFuelAvailable();
    } else if (currentPlanet.getName().equals("Station") && game.getRemainingRefuels() > 0) {
      gui.getFuelLevelBar().setValue(100);
      spacecraft.setFuel(100);
      gui.getFuelLevelBar().setString("Fuel: " + spacecraft.getFuel() + "%");
      game.setRemainingRefuels(game.getRemainingRefuels()-1);
      Music.playAudioFX("sounds/Fuel.wav");// success
      View.printSpacecraftHasBeenFilled();
    }
  }
  public void interactAlien() {
    Spacecraft spacecraft = game.getSpacecraft();
    Planet destinationPlanet = returnPlanet(spacecraft.getCurrentPlanet().getName());
    String preReq = null;
    if (destinationPlanet != null) {
      preReq = destinationPlanet.getPreReq();
    }
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
  public void godMode() {
    //gives the user all the astronauts, items, and sets health and fuel to 100

    Music.playAudioFX("sounds/God_Mode.wav");
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

    //set timer to 8:00
    gui.getTicktock().setMinutes(8);
    gui.getTicktock().setSeconds(1);
  }
  public void checkGameResult() {
    //Checks if planet complete to add checkmark
    gui.addCheckMarkToCompletedPlanets(game.getSpacecraft().getCurrentPlanet());

    int numRescuedPassengers = returnPlanet("earth").getNumOfAstronautsOnPlanet();
    int totalNumberOfPersonsCreatedInSolarSystem = game.getTotalNumberOfAstronauts();
    boolean userWon = (double) numRescuedPassengers / totalNumberOfPersonsCreatedInSolarSystem
        >= (double) 5 / 5;

    if (!userWon) {

      if (game.getSpacecraft().getHealth() < 21) {
        Music.playAudioFX("sounds/Low_Health.wav");
      } else if (game.getSpacecraft().getFuel() == 20) {
        Music.playAudioFX("sounds/Low_Fuel.wav");
      }
      if (game.getSpacecraft().getFuel() < 0) {
        gui.getFuelLevelBar().setString("Fuel: " + 0 + "%");
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
  public static Planet returnPlanet(String destination) {
    // Returns an instance of the desired planet when given a planet name
    // If the desired planet by the name does not exist, returns null
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
  public void updateFuel() {
    Spacecraft spacecraft = game.getSpacecraft();
    gui.getFuelLevelBar().setValue((spacecraft.getFuel() - 10));
    spacecraft.setFuel((spacecraft.getFuel() - 10));
    gui.getFuelLevelBar().setString("Fuel: " + spacecraft.getFuel() + "%");
  }
}
