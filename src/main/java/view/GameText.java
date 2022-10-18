package view;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;


public class GameText {

  public static model.GameText gameText;
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";

  public void getGameTextJson() {
    // create a reader
    try (Reader reader = new InputStreamReader(
        this.getClass().getResourceAsStream("/game-text.json"))
    ) {
      // convert JSON file to model.GameText
      gameText = new Gson().fromJson(reader, model.GameText.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public static void printTitle() {
    System.out.println(ANSI_BLUE);
    for (String line : gameText.getTitle()) {
      System.out.println(line);
    }
    System.out.println(ANSI_RESET);
    System.out.println();
  }

  public static void printIntro() {
    System.out.println();
    for (String line : gameText.getIntroduction()) {
      System.out.println(line);
    }
    System.out.println();
  }

  public static void printInstructions() {
    System.out.println();
    for (String line : gameText.getInstructions()) {
      System.out.println(line);
    }
    System.out.println();
  }

  public void printGameState(int remainingAstro, int remainingDays, int shipHealth,
      String planetName) {
    System.out.println();
    System.out.println("Current Planet: " + planetName);
    System.out.println("Ship's Condition: " + shipHealth);
    System.out.println("Number of Remaining Astronauts: " + remainingAstro);
    System.out.println("Number of Remaining Days: " + remainingDays);
    System.out.println();
  }

  public static void printUserInputPrompt(String prompt) {
    System.out.println(prompt);
  }

  public static void printNoEngineerAlert() {
    System.out.println();
    System.out.println(ANSI_RED + gameText.getNoEngineerOnBoardAlert() + ANSI_RESET);
    System.out.println();
  }

  public static void printNPCDialogue() {
    Random random = new Random();
    int randomIntInArrayRange = random.nextInt(7);
    System.out.println("Passenger: " + gameText.getNpcDialogue()[randomIntInArrayRange]);
  }

  public static void printGameOverMessage(boolean userWon) {
    System.out.println();
    if (userWon) {
      System.out.println(ANSI_GREEN + gameText.getUserWon() + ANSI_RESET);
    } else {
      System.out.println(ANSI_RED + gameText.getUserLost() + ANSI_RED);
    }
  }

}
