package com.spacepilot.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacepilot.model.Planet;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import com.spacepilot.model.Engineer;
import com.spacepilot.model.Game;
import com.spacepilot.model.Person;
import com.spacepilot.model.Spacecraft;
import com.spacepilot.view.View;

public class Controller {

  private Game game; // model, where the current state of the game is stored
  private final View view; // view, which is in charge of displaying (printing) game info
  private final BufferedReader reader; // buffered reader used to read in what user enters
  private String userInput; // variable used to save user input

  public Controller(Game game, View view, BufferedReader reader) {
    this.game = game;
    this.view = view;
    this.reader = reader;
    this.userInput = "";
  }

  public void play() throws IOException {
    // set up game environment (placing random number of astronauts, etc)
    setUpGame();
    // display game's introduction with flash screen and story and prompt the user to continue
    gameIntro();
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
    // display game over message
    // TODO: Right now, the game is set to display the lose message only.
    //  Change this when the game's win / lose logic is implemented.
    view.printGameOverMessage(false);
  }

  public void setUpGame() {
    // place random number of astronauts on each planet
    for (Planet planet : game.getPlanets()) {
      planet.placeAstronauts(planet);
    }
    game.setSpacecraft(new Spacecraft());
    game.getSpacecraft().setCurrentPlanet(returnPlanet("earth"));
  }

  public void gameIntro() throws IOException {
    view.getGameTextJson();
    // display title
    view.printTitle();
    // prompt the user to press "y" to continue
    do {
      getUserInput("Enter y to continue");
    } while (!userInput.equals("y"));
    View.clearConsole(); // Note: clear console does not work on IntelliJ console
    // display introductory background story
    view.printIntro();
    // prompt the user to press "y" to continue
    do {
      getUserInput("Enter y to continue");
    } while (!userInput.equals("y"));
    View.clearConsole(); // Note: clear console does not work on IntelliJ console
    // display game instructions (how-to-play)
    view.printInstructions();
  }

  public void nextMove(String[] command) throws IOException {
    if (command[0].equals("quit")) {
      game.setOver(true);

    } else if (command[0].equals("help")) {
      view.printInstructions();

    } else if (command[0].equals("save")) {
      saveGame(game);

    } else if (command[0].equals("continue")) {
      loadSavedGame();

    } else if (command[0].equals("go")) {
      // if the user is trying to go to the current planet
      if (command[1].equals(game.getSpacecraft().getCurrentPlanet())) {
        view.printSamePlanetAlert();
      } else {
        game.getSpacecraft().setCurrentPlanet(returnPlanet(command[1]));
        // decrement remaining days by 1 when user goes somewhere
        game.setRemainingDays(game.getRemainingDays() - 1);
      }

    } else if (command[0].equals("chat")) {
      userInput = "";
      while (userInput.length() < 1) {
        System.out.println("The passengers aren't doing well...");
        // display line below until user inputs at least one char
        getUserInput("What would you like to say to them?");
      }

    } else if (command[0].equals("repair")) {
      game.getSpacecraft().typeAndNumOfPassengersOnBoard();
      int engineerCount = game.getSpacecraft().getNumOfEngineersOnBoard();
      if (engineerCount == 0) {
        view.printNoEngineerAlert();
        return;
      }
      Engineer.repairSpacecraft(game.getSpacecraft());

    } else if (command[0].equals("load")) {
      loadNewPassengers();

    } else if (command[0].equals("unload")) {
      unloadPassengersOnEarth();

    } else { // invalid command message
      System.out.println("Invalid Command! Please use the command HELP for the ship's command log");
    }
  }

  public void loadNewPassengers() {
    // TODO: Move these print line statements to View after debugging
    Collection<Person> arrayOfAstronautsOnCurrentPlanet = game.getSpacecraft().getCurrentPlanet()
        .getArrayOfAstronautsOnPlanet();
    if (arrayOfAstronautsOnCurrentPlanet.size() > 0) {
      System.out.println("size of array of astros on current planet before loading: "
          + arrayOfAstronautsOnCurrentPlanet.size());
      System.out.println(
          "size of array of astros on spacecraft before loading: " + game.getSpacecraft()
              .getPassengers().size());
      game.getSpacecraft().addPassengers(arrayOfAstronautsOnCurrentPlanet);
      System.out.println(
          "size of array of astros on spacecraft AFTER loading: " + game.getSpacecraft()
              .getPassengers().size());

      arrayOfAstronautsOnCurrentPlanet.clear();
      System.out.println(
          "array of astros on current planet after loading onto SC (should be empty): "
              + arrayOfAstronautsOnCurrentPlanet);
    } else {
      System.out.println("Sorry, there are no astronauts to rescue on this planet.");
      //stop user from trying to load passengers back onto planet
    }
    //collections are immutable?
  }

  public void unloadPassengersOnEarth() {
    Planet currentPlanet = game.getSpacecraft().getCurrentPlanet();
    Spacecraft spacecraft = game.getSpacecraft();

    if (currentPlanet.getName().equals("Earth")) {
      currentPlanet.getArrayOfAstronautsOnPlanet().addAll(game.getSpacecraft().getPassengers());
      spacecraft.getPassengers().clear();
    } else {
      System.out.println("Passengers can only be dropped off on Earth.");
    }
  }

  public void getUserInput(String prompt) throws IOException {
    // clear previous user input
    userInput = "";
    // print the prompt message
    view.printUserInputPrompt(prompt);
    // sanitize user response (turn it into lower-case and trim whitespaces) and save it to userInput
    userInput = reader.readLine().trim().toLowerCase();
  }

  public void displayGameState() {
    view.printGameState(game.getRemainingAstronauts(), game.getRemainingDays(),
        game.getSpacecraft().getHealth(), game.getSpacecraft().getCurrentPlanet().getName());
  }

  public void loadSavedGame() {
    try (Reader reader = Files.newBufferedReader(Paths.get("./game-data.json"))) {
      Game savedGame = new Gson().fromJson(reader, Game.class);
      if (savedGame != null) { // if there is a saved game data
        game = savedGame;
        view.printLoadGameResult(true);
      } else {
        view.printLoadGameResult(false);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void saveGame(Game game) throws IOException {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    FileWriter writer = new FileWriter("game-data.json");
    writer.write(gson.toJson(game));
    writer.close();
    view.printSaveGameMessage();
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

  public Planet returnPlanet(String destination) {
    // capitalize the destination
    String planetName =
        destination.substring(0, 1).toUpperCase() + destination.substring(1).toLowerCase();
    return Arrays.stream(game.getPlanets())
        .filter(planet -> planet.getName().equals(planetName))
        .findFirst()
        .orElse(null);
  }

}
