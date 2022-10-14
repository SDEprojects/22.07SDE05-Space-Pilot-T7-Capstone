package controller;

import java.io.BufferedReader;
import java.io.IOException;
import model.Game;
import view.GameText;

public class Controller {

  private String userInput; // variable used to save user input
  private final Game game; // The model, where the current state of the game is stored
  private final GameText view; // The view, which is in charge of displaying (printing) info to user
  private final BufferedReader reader; // Just a buffered reader used to read in what user enters

  public Controller(Game game, GameText view, BufferedReader reader) {
    this.userInput = "";
    this.game = game;
    this.view = view;
    this.reader = reader;
  }

  public void play() throws IOException {
    view.printTitle();
    System.out.println();
    while (!userInput.equals("y")) {
      getUserInput("Press Y to continue");
    }
    userInput = "";
    GameText.clearConsole();
    view.printIntro();
    System.out.println();
    while (!userInput.equals("y")) {
      getUserInput("Press Y to continue");
    }
    userInput = "";
    GameText.clearConsole();
    view.printHowToPlay();
    System.out.println();

    while (!game.isOver()) { // While game is not over
      // print current game info
      updateView();
      // Prompt the user to enter their next command (saved as userInput)
      getUserInput("Please enter your next command");
      // Parse their command (verb and noun)
      String[] userCommand = commandParser(userInput);
      nextMove(userCommand);
    }
  }

  public void nextMove(String[] command) {
    if (command[0].equals("quit")) {
      game.setOver(true);
    }

    if (command[0].equals("go")) {
      if (command[1].equals("moon")) {
        game.getSpacecraft().setCurrentPlanet(game.getMoon());
      }
      if (command[1].equals("mars")) {
        game.getSpacecraft().setCurrentPlanet(game.getMars());
      }
      if (command[1].equals("mercury")) {
        game.getSpacecraft().setCurrentPlanet(game.getMercury());
      }
      if (command[1].equals("earth")) {
        game.getSpacecraft().setCurrentPlanet(game.getEarth());
      }
    }

    if (command[0].equals("help")) {
      view.displayCommands();
    }

    if (command[0].equals("repair")) {
      // check if at least 1 engineer is on board
      // if so, repair the spacecraft
      // if not, display appropriate message
    }
  }

  public void getUserInput(String prompt) throws IOException {
    // print the prompt message
    System.out.println(prompt);
    // sanitize user response (turn it into lower-case and trim whitespaces) and save it to userInput
    userInput = reader.readLine().trim().toLowerCase();
  }

  public void moveSpacecraft() {
    // Look at user input and do necessary operations
    // Hey, model, update the current state of the game because something's changed
    // Hey, view, display new information cuz the game information in model just changed
  }

  public void updateView() {
    view.displayGameState(game.getRemainingAstro(), game.getRemainingDays(), game.getShipHealth(),
        game.getSpacecraft().getCurrentPlanet().getName());
  }

  private static String[] commandParser(String text) {
    String[] result = new String[2];
    String[] splitText = text.split(" ");
    String verb = splitText[0]; // First word
    String noun = splitText[splitText.length - 1]; // Last word
    result[0] = verb;
    result[1] = noun;
    return result;
  }

}
