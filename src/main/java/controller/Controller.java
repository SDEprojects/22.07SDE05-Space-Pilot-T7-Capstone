package controller;

import java.io.BufferedReader;
import java.io.IOException;
import model.Engineer;
import model.Game;
import view.GameText;


public class Controller {

  private String userInput; // variable used to save user input
  private final Game game; // model, where the current state of the game is stored
  private final GameText view; // view, which is in charge of displaying (printing) game info
  private final BufferedReader reader; // buffered reader used to read in what user enters

  public Controller(Game game, GameText view, BufferedReader reader) {
    this.userInput = "";
    this.game = game;
    this.view = view;
    this.reader = reader;
  }

  public void play() throws IOException {
    view.printTitle();
    while (!userInput.equals("y")) {
      getUserInput("Press Y to continue");
    }
    userInput = "";
    GameText.clearConsole(); // Clear console does not work on IntelliJ console
    view.printIntro();
    while (!userInput.equals("y")) {
      getUserInput("Press Y to continue");
    }
    userInput = "";
    GameText.clearConsole();
    view.printHowToPlay();

    while (!game.isOver()) { // While game is not over
      // print current game info
      updateView();
      // Prompt the user to enter their next command (saved as userInput)
      getUserInput("Please enter your next command");
      // Parse their command (verb and noun)
      String[] userCommand = textParser(userInput);
      nextMove(userCommand);
    }
  }

  public void nextMove(String[] command) throws IOException {
    if (command[0].equals("quit")) {
      game.setOver(true);
    }

    if (command[0].equals("go")) {
      moveSpacecraft(command[1]);
    }

    if (command[0].equals("help")) {
      view.displayCommands();
    }

    if(command[0].equals("chat")){
      userInput="";
      while(userInput.length()<1) {
        System.out.println("The passengers aren't doing well...");
        //display line below until user inputs at least one char
        getUserInput("What would you like to say to them?");
      }
      view.printNPCLine();
    }
//    if(command[0].equals("load")) {
//      model.Planet.
//    }
    //add load command

    if (command[0].equals("repair")) {
      game.getSpacecraft().typeAndNumOfPassengersOnBoard();
      int engineerCount = game.getSpacecraft().getNumOfEngineersOnBoard();
      if (engineerCount == 0) {
        view.noEngineerToRepair();
        return;
      }
        Engineer engineer = new Engineer();
        engineer.repairSpacecraft(game.getSpacecraft());
    }
  }

  public void moveSpacecraft(String destination) {
    switch (destination) {
      case "moon":
        game.getSpacecraft().setCurrentPlanet(game.getMoon());
        break;
      case "mars":
        game.getSpacecraft().setCurrentPlanet(game.getMars());
        break;
      case "mercury":
        game.getSpacecraft().setCurrentPlanet(game.getMercury());
        break;
      case "earth":
        game.getSpacecraft().setCurrentPlanet(game.getEarth());
        break;
    }
  }

  public void getUserInput(String prompt) throws IOException {
    // print the prompt message
    System.out.println(prompt);
    // sanitize user response (turn it into lower-case and trim whitespaces) and save it to userInput
    userInput = reader.readLine().trim().toLowerCase();
  }

  public void updateView() {
    view.displayGameState(game.getRemainingAstro(), game.getRemainingDays(), game.getShipHealth(),
        game.getSpacecraft().getCurrentPlanet().getName());
  }

  private static String[] textParser(String text) {
    String[] result = new String[2];
    String[] splitText = text.split(" ");
    String verb = splitText[0]; // First word
    String noun = splitText[splitText.length - 1]; // Last word
    result[0] = verb;
    result[1] = noun;
    return result;
  }

}
