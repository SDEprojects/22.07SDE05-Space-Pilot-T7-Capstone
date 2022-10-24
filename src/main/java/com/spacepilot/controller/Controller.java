package com.spacepilot.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacepilot.Main;
import com.spacepilot.model.Engineer;
import com.spacepilot.model.Game;
import com.spacepilot.model.Person;
import com.spacepilot.model.Planet;
import com.spacepilot.model.Spacecraft;
import com.spacepilot.view.View;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  public void play() throws IOException, URISyntaxException {
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
  }

  public void setUpGame() throws URISyntaxException, IOException {
    game.setPlanets(createPlanets());
    // place random number of astronauts on each planet
    for (Planet planet : game.getPlanets()) {
      planet.placeAstronauts(planet);
    }
    game.countTotalNumberOfAstronautsOnPlanet();
    // create a new spacecraft instance for the current game, using appropriate .json file
    game.setSpacecraft(createNewSpacecraft());
    // set the current spacecraft's current planet to be Earth
    game.getSpacecraft().setCurrentPlanet(returnPlanet("earth"));
  }

  public void gameIntro() throws IOException {
    view.getGameTextJson();
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
      if (command[1].equals(game.getSpacecraft().getCurrentPlanet().getName())) {
        View.printSamePlanetAlert();
      } else {
        Planet destinationPlanet = returnPlanet(command[1]);
        String event = destinationPlanet.randomEncounter();
        Spacecraft spacecraft = game.getSpacecraft();
        if (event != null) {
          // decrement spacecraft health by 1.
          spacecraft.setHealth(spacecraft.getHealth() - 1);
          // alert the user about the event
          view.printEventAlert(event);
        }
        spacecraft.setCurrentPlanet(returnPlanet(command[1]));
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
        View.printNoEngineerAlert();
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
    Collection<Person> arrayOfAstronautsOnCurrentPlanet = game.getSpacecraft().getCurrentPlanet()
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
      arrayOfAstronautsOnCurrentPlanet.clear();
    }
  }

  public void unloadPassengersOnEarth() {
    Planet currentPlanet = game.getSpacecraft().getCurrentPlanet();
    Spacecraft spacecraft = game.getSpacecraft();

    if (currentPlanet.getName().equals("Earth")) {
      currentPlanet.getArrayOfAstronautsOnPlanet().addAll(game.getSpacecraft().getPassengers());
      spacecraft.getPassengers().clear();
      determineIfUserWinsOrLoses();
      game.setOver(true);
    } else {
      View.printYouCantUnloadPassengersIfCurrentPlanetNotEarth();
    }
  }

  public void determineIfUserWinsOrLoses() {
    int numRescuedPassengers = returnPlanet("earth").getArrayOfAstronautsOnPlanet().size();
    int totalNumberOfPersonsCreatedInSolarSystem = game.getTotalNumberOfAstronauts();
    if ((double) numRescuedPassengers / totalNumberOfPersonsCreatedInSolarSystem
        >= (double) 4 / 5) {
      View.printGameOverMessage(true);
    } else {
      View.printGameOverMessage(false);
    }
  }

  public void getUserInput(String prompt) throws IOException {
    // clear previous user input
    userInput = "";
    // print the prompt message
    View.printUserInputPrompt(prompt);
    // sanitize user response (turn it into lower-case and trim whitespaces) and save it to userInput
    userInput = reader.readLine().trim().toLowerCase();
  }

  public void displayGameState() {
    view.printGameState(game.calculateRemainingAstronautsViaTotalNumOfAstronauts(),
        game.getRemainingDays(), game.getSpacecraft().getHealth(),
        game.getSpacecraft().getCurrentPlanet().getName(),
        game.getSpacecraft().getPassengers().size());
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

  public static Spacecraft createNewSpacecraft() {
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
    URL url = Main.class.getClassLoader().getResource("planets");
    Path dirPath = Paths.get(url.toURI());
    Stream<Path> paths = Files.list(dirPath);
    List<String> filenames = paths
        .map(p -> p.getFileName().toString())
        .collect(Collectors.toList());
    for (String filename : filenames) {
      try (Reader reader = new InputStreamReader(
          Main.class.getResourceAsStream("/planets/" + filename))) {
        planets.add(new Gson().fromJson(reader, Planet.class));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return planets;
  }

}
