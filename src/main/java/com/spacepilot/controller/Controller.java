package com.spacepilot.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacepilot.Main;
import com.spacepilot.model.Engineer;
import com.spacepilot.model.Game;
import com.spacepilot.model.Music;
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
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class Controller {

  private Game game; // model, where the current state of the game is stored
  private final BufferedReader reader; // buffered reader used to read in what user enters
  private String userInput; // variable used to save user input
  private int repairCounter = 0;

  public Controller(Game game, BufferedReader reader) {
    this.game = game;
    this.reader = reader;
    this.userInput = "";
  }



  public void play()
      throws IOException, URISyntaxException, MidiUnavailableException, InvalidMidiDataException {
    // create and set up game environment
    setUpGame();
    // display game's introduction with flash screen and story and prompt the user to continue
    gameIntro();
    // play music
    Music.playMusic();
    // while game is not over, allow the user to continue to play
    while (!game.isOver()) {
      // print current game info
      displayGameState();
      // prompt the user to enter their next command (saved as userInput)
      getUserInput("Please enter your next command");
      // parse the user input to get their command (verb and noun)
      String[] userCommand = textParser(userInput);
      // execute their command and/or display information (e.g., list of commands, invalid command, etc.)
      nextMove(userCommand);
    }
    checkGameResult();

    Music.stopMusic(); // Close sequencer so that the program can terminate
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

  public void gameIntro() throws IOException {
    View.getGameTextJson();
    // display title
    View.printTitle();
    // prompt the user to press "y" to continue
    do {
      getUserInput("Enter y to continue");
    } while (!userInput.equals("y"));
    View.clearConsole(); // Note: clear console does not work on IntelliJ console
    // display introductory background story
    View.printIntro();
    // prompt the user to press "y" to continue
    do {
      getUserInput("Enter y to continue");
    } while (!userInput.equals("y"));
    View.clearConsole(); // Note: clear console does not work on IntelliJ console
    // display game instructions (how-to-play)
    View.printInstructions();
  }

  public void nextMove(String[] command) throws IOException {
    if (command[0].equals("quit")) {
      game.setOver(true);

    } else if (command[0].equals("help")) {
      View.printInstructions();

    } else if (command[0].equals("save")) {
      saveGame(game);

    } else if (command[0].equals("continue")) {
      loadSavedGame();

    } else if (command[0].equals("go")) {
      // if the user is trying to go to the current planet
      if (command[1].equals(game.getSpacecraft().getCurrentPlanet().getName())) {
        View.printSamePlanetAlert();
      } else {
        Planet destinationPlanet = returnPlanet(command[1]);
        if (destinationPlanet == null) {
          View.printInvalidDestination();
        } else {
          String event = destinationPlanet.randomEncounter();
          Spacecraft spacecraft = game.getSpacecraft();
          if (event != null) {
            // decrement spacecraft health by 1.
            spacecraft.setHealth(spacecraft.getHealth() - 1);
            // alert the user about the event
            View.printEventAlert(event);
          }
          spacecraft.setCurrentPlanet(returnPlanet(command[1]));
          // decrement remaining days by 1 when user goes somewhere
          game.setRemainingDays(game.getRemainingDays() - 1);
          // check if the number of remaining days is less than 1
          // or if the spacecraft's health is less than 1
          if (game.getRemainingDays() < 1 || spacecraft.getHealth() < 1) {
            // if so, set the game as over
            game.setOver(true);
          }
        }
      }

    } else if (command[0].equals("chat")) {
      userInput = "";
      while (userInput.length() < 1) {
        View.printNPCDialoguePrompt();
        // display line below until user inputs at least one char
        getUserInput("What would you like to say to them?");
      }
      View.printNPCDialogue();

    } else if (command[0].equals("repair")) {
      game.getSpacecraft().typeAndNumOfPassengersOnBoard();
      int engineerCount = game.getSpacecraft().getNumOfEngineersOnBoard();
      if (engineerCount == 0) {
        View.printNoEngineerAlert();
        return;
      }
      if (repairCounter < 2) {
        Engineer.repairSpacecraft(game.getSpacecraft());
        View.printRepair();

        repairCounter++;
      } else if (repairCounter >= 2) {
        View.printRepairLimit();
      }

    } else if (command[0].equals("load")) {
      loadNewPassengers();

    } else if (command[0].equals("unload")) {
      unloadPassengersOnEarth();

    } else { // invalid command message
      View.printInvalidCommandAlert();
    }
  }

  public void loadNewPassengers() {
    Collection<Object> arrayOfAstronautsOnCurrentPlanet = game.getSpacecraft().getCurrentPlanet()
        .getArrayOfAstronautsOnPlanet();
    if (arrayOfAstronautsOnCurrentPlanet.size() <= 0) {
      View.printNoAstronautsToLoad();
    }
    if (game.getSpacecraft().getCurrentPlanet().getName().equals("Earth")) {
      View.printCannotRemovePeopleFromEarth();
    }
    if (arrayOfAstronautsOnCurrentPlanet.size() > 0 && !game.getSpacecraft().getCurrentPlanet()
        .getName().equals("Earth")) {
      game.getSpacecraft().addPassengers(arrayOfAstronautsOnCurrentPlanet);
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

//      game.setOver(true);
    } else {
      View.printYouCantUnloadPassengersIfCurrentPlanetNotEarth();
    }
  }

  public void checkGameResult() {
    int numRescuedPassengers = returnPlanet("earth").getNumOfAstronautsOnPlanet();
    int totalNumberOfPersonsCreatedInSolarSystem = game.getTotalNumberOfAstronauts();
    boolean userWon = (double) numRescuedPassengers / totalNumberOfPersonsCreatedInSolarSystem
        >= (double) 4 / 5;

    if (!userWon) {
      return;
    } else if (userWon) {
      game.setOver(true);
      View.printGameOverMessage(userWon);
    }
  }

  public void displayGameState() {
    View.printGameState(
        game.getSpacecraft().getCurrentPlanet().getName(),
        game.getSpacecraft().getCurrentPlanet().getNumOfAstronautsOnPlanet(),
        game.getSpacecraft().getHealth(),
        game.calculateRemainingAstronautsViaTotalNumOfAstronauts() - returnPlanet("earth").getNumOfAstronautsOnPlanet() ,
        game.getRemainingDays(),
        game.getSpacecraft().getPassengers().size(),
        returnPlanet("earth").getNumOfAstronautsOnPlanet());
  }

  public void loadSavedGame() {
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

  public void saveGame(Game game) throws IOException {
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
  private static String[] textParser(String text) {
    String[] result = new String[2];
    String[] splitText = text.split(" ");
    String verb = splitText[0]; // First word
    String noun = splitText[splitText.length - 1]; // Last word
    result[0] = verb;
    result[1] = noun;
    return result;
  }

  public void getUserInput(String prompt) throws IOException {
    // clear previous user input
    userInput = "";
    // print the prompt message
    View.printUserInputPrompt(prompt);
    // sanitize user response (turn it into lower-case and trim whitespaces) and save it to userInput
    userInput = reader.readLine().trim().toLowerCase();
  }

  // Returns an instance of the desired planet when given a planet name
  // If the desired planet by the name does not exist, returns null
  public Planet returnPlanet(String destination) {
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
