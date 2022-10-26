package com.spacepilot.view;

import com.google.gson.Gson;
import com.spacepilot.Main;
import com.spacepilot.controller.Controller;
import com.spacepilot.model.GameText;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Random;

public class View {

  public static GameText gameText;
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";



  public static void getGameTextJson() {
    // create a reader
    try (Reader reader = new InputStreamReader(
        Main.class.getResourceAsStream("/game-text.json"))
    ) {
      // convert JSON file to model.GameText
      gameText = new Gson().fromJson(reader, GameText.class);
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

  public static void printGameState(String planetName, int astrosOnPlanet, String itemOnPlanet, int shipHealth, int remainingAstros, int remainingDays
,int numOfPassengersOnboard, int astrosOnEarth, List<String> currentInventory, int fuelLevel, int refuelsLeft) {
    if(itemOnPlanet == null){
      itemOnPlanet = "No Item";
    }
    System.out.println();
    System.out.println("Current Planet: " + planetName);
    System.out.println("Astronauts on " + planetName + ": " + astrosOnPlanet);
    System.out.println(planetName + "'s Inventory: " + itemOnPlanet);
    System.out.println("Ship's Condition: " + shipHealth);
    System.out.println("Astronauts Lost in Space: " + remainingAstros);
    System.out.println("Number of Remaining Days: " + remainingDays);
    System.out.println("Fuel: " + fuelLevel);
    System.out.println("Refuels left: " + refuelsLeft);
    System.out.println("Number of Passengers Onboard: " + numOfPassengersOnboard);
    System.out.println("Passengers Returned Home: " + astrosOnEarth);
    System.out.println("Current Inventory: " + currentInventory);
    System.out.println();
  }

  public static void printUserInputPrompt(String prompt) {
    System.out.println(prompt);
  }

  public static void printLoadGameResult(boolean savedGameExists) {
    System.out.println();
    if (savedGameExists) {
      System.out.println(ANSI_GREEN + "Previous game data successfully loaded" + ANSI_RESET);
    } else {
      System.out.println(ANSI_RED + "Failed - previous game data does not exist" + ANSI_RESET);
    }
    System.out.println();
  }

  public static void printSaveGameMessage() {
    System.out.println();
    System.out.println(ANSI_BLUE + "SAVED GAME DATA" + ANSI_RESET);
    System.out.println();
  }

  public static void printSamePlanetAlert() {
    System.out.println();
    System.out.println(ANSI_RED + "System: You are already there");
    System.out.println();
  }

  public static void printNoEngineerAlert() {
    System.out.println();
    System.out.println(ANSI_RED + gameText.getNoEngineerOnBoardAlert() + ANSI_RESET);
    System.out.println();
  }

  public static void printEventAlert(String event) {
    System.out.println();
    System.out.println(
        ANSI_RED + "MISSION CONTROL: Your spacecraft has been damaged by " + event + ANSI_RESET);
    System.out.println();
  }

  public static void printDamageConditionAlert(String damageCondition, String planet, String preReq){
    System.out.println();
    System.out.println(
        ANSI_RED + "MISSION CONTROL: When you landed on " + planet + ", your spacecraft was damaged by "
            + damageCondition + ". You need to find the " + preReq + " to safely load the passengers here." + ANSI_RESET
    );
  }

  public static void printCantLoadWithoutPreReqAlert(String preReq){
    System.out.println();
    System.out.println(
        ANSI_RED + "MISSION CONTROL: It is unsafe to load passengers on this planet without " + preReq + ANSI_RESET
    );
  }

  public static void printDamageConditionAvoidedAlert(String preReq, String damageCondition){
    System.out.println();
    System.out.println(ANSI_RED + "MISSION CONTROL: You used " + preReq + " to avoid damage from " + damageCondition + "! "
        + "You are clear to load passengers." + ANSI_RESET);
  }

  public static void printYouveGotAnEngineer() {
    System.out.println(ANSI_GREEN + "You have got at least 1 engineer on board...\n"
        + "and they've got the ability to repair the spacecraft!" + ANSI_RESET);
  }

  public static void printYouHaventGotAnEngineerOnBoard() {
    System.out.println(ANSI_RED + "You don't have any engineers on board...\n"
        + "thus, you cannot repair the spacecraft." + ANSI_RESET);
  }

  public static void printNPCDialoguePrompt() {
    System.out.println();
    System.out.println(ANSI_RED + "The passengers don't seem to be doing well..." + ANSI_RESET);
  }

  public static void printNPCDialogue() {
    Random random = new Random();
    int randomIntInArrayRange = random.nextInt(7);
    System.out.println("Passenger: " + gameText.getNpcDialogue()[randomIntInArrayRange]);
  }

  public static void printGameOverMessage(boolean userWon) {
    System.out.println();
    if (userWon) {
      System.out.println();
      System.out.println(ANSI_GREEN + gameText.getUserWon() + ANSI_RESET);
    } else {
      System.out.println();
      System.out.println(ANSI_RED + gameText.getUserLost() + ANSI_RESET);
    }
  }

  public static void printInvalidCommandAlert() {
    System.out.println();
    System.out.println(
        ANSI_RED + "Invalid Command! Please use HELP command to see available commands"
            + ANSI_RESET);
    System.out.println();
  }

  public static void printInvalidDestination() {
    System.out.println();
    System.out.println(ANSI_RED + "Sorry, you cannot go there." + ANSI_RESET);
    System.out.println();
  }

  public static void printNoAstronautsToLoad() {
    System.out.println(ANSI_RED + "There aren't any astronauts to rescue on this planet." + ANSI_RESET);
  }

  public static void printCannotRemovePeopleFromEarth() {
    System.out.println(
        ANSI_RED + "All passengers dropped off on Earth must remain there, as planet Earth is their final destination." + ANSI_RESET);
  }

  public static void printYouCantUnloadPassengersIfCurrentPlanetNotEarth() {
    System.out.println(ANSI_RED + "Passengers can only be dropped off on Earth." + ANSI_RESET);
  }

  public static void printStationHasNoMoreFuelAvailable() {
    System.out.println(ANSI_RED + "Oh no! The station is out of fuel... I hope I can get back!" + ANSI_RESET);
  }

  public static void printYouCanOnlyRefuelAtTheStation() {
    System.out.println(ANSI_RED + "You can only refuel your ship at the Station!" + ANSI_RESET);
  }
  public static void printYourFuelTankIsFullAlready() {
    System.out.println(ANSI_RED + "Your fuel tank is already full." + ANSI_RESET);
  }
  public static void printRepair() {
    System.out.println();
    System.out.println(ANSI_GREEN + "Spacecraft repair was successful." + ANSI_RESET);
    System.out.println();
  }

  public static void printRepairLimit() {
    System.out.println(ANSI_RED + "Sorry, you cannot use the repair command\n"
        + "more than twice per round of the game." + ANSI_RESET);
  }

}
