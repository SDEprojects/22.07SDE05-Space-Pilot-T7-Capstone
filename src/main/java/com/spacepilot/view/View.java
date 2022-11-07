package com.spacepilot.view;


import com.google.gson.Gson;
import com.spacepilot.Main;
import com.spacepilot.model.GameText;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.List;
import java.util.Random;

public class View {

  public static GameText gameText;

  public static void consoleToGUI(Gui gui) {
    System.setOut(new PrintStream(new RedirectingOutputStream(gui), true));
  }

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

  public static void printHurryUp() {
    System.out.println("We have to move fast out there, my oxygen is limited on this rescue mission!\n\nTime to hop in and take off!");
  }

  public static void textSpacing() {
    System.out.println("\n\n\n\n");
  }

  public static void printLoadGameResult(boolean savedGameExists) {
    System.out.println();
    if (savedGameExists) {
      System.out.println("Previous game data successfully loaded");
    } else {
      System.out.println("Failed - previous game data does not exist");
    }
    System.out.println();
  }

  public static void printSaveGameMessage() {
    System.out.println();
    System.out.println("SAVED GAME DATA");
    System.out.println();
  }

  public static void printSamePlanetAlert() {
    System.out.println();
    System.out.println("System: You are already there");
    System.out.println();
  }

  public static void printNoEngineerAlert() {
    System.out.println();
    System.out.println(gameText.getNoEngineerOnBoardAlert());
    System.out.println();
  }

  public static void printEventAlert(String event) {
    System.out.println();
    System.out.println(
        "MISSION CONTROL: Your spacecraft has been damaged by " + event);
    System.out.println();
  }

  public static void printPlanetDamageConditionAlert(String damageCondition, String planet, String preReq){
    System.out.println(
        "MISSION CONTROL: When you landed on " + planet + ", you noticed a "
            + damageCondition + ". You need to find the " + preReq + " to get the astronauts out of here!"
    );
  }

  public static void printAlienDamageConditionAlert(String damageCondition, String planet, String preReq){
    System.out.println(
        "MISSION CONTROL: You have angered the "
            + damageCondition + "! You need to evacuate and find the " + preReq + " to get the astronauts out of here!"
    );
  }

  public static void printCantLoadWithoutPreReqAlert(String preReq){
    System.out.println();
    System.out.println(
        "MISSION CONTROL: It is unsafe to load passengers on this planet without a " + preReq
    );
  }

  public static void printAlienDestroyed(String preReq, String damageCondition){
    System.out.println();
    System.out.println("MISSION CONTROL: You used the " + preReq + " and destroyed the " + damageCondition + "! "
        + "You are clear to load the astronauts.");
  }

  public static void printAlienBabyReturned(String preReq, String damageCondition){
    System.out.println();
    System.out.println("MISSION CONTROL: You returned the " + preReq + " to the " + damageCondition + "! "
        + "You are clear to the astronauts..");
  }

  public static void newItemReceived(String item) {
    System.out.println("The astronauts had a " + item + " with them, maybe I can use this to help rescue the others!");
  }

  public static void printNPCDialogue() {
    Random random = new Random();
    int randomIntInArrayRange = random.nextInt(7);
    System.out.println("Passenger: " + gameText.getNpcDialogue()[randomIntInArrayRange]);
  }

  public static void printInvalidDestination() {
    System.out.println();
    System.out.println("Sorry, you cannot go there.");
    System.out.println();
  }

  public static void printNoAstronautsToLoad() {
    System.out.println("There aren't any astronauts to rescue on this planet.");
  }

  public static void printCannotRemovePeopleFromEarth() {
    System.out.println(
        "All passengers dropped off on Earth must remain there, as planet Earth is their final destination.");
  }

  public static void printYouCantUnloadPassengersIfCurrentPlanetNotEarth() {
    System.out.println("Passengers can only be dropped off on Earth.");
  }

  public static void printStationHasNoMoreFuelAvailable() {
    System.out.println("Oh no! The station is out of fuel... I hope I can still get everyone back!");
  }

  public static void printSpacecraftHasBeenFilled() {
    System.out.println("Your ship has been refueled! Time to go save more astronauts!"
        + "");
  }

  public static void printYourSpacecraftIsOutOfFuelAndYouLose() {
    System.out.println("You left the planet and ran out of fuel mid flight. You are now stranded in space and slowly starve to death.\n\nGAME OVER"
        + "");
  }

  public static void printYouCanOnlyRefuelAtTheStation() {
    System.out.println("You can only refuel your ship at the Station!");
  }
  public static void printYourFuelTankIsFullAlready() {
    System.out.println("Your spacecraft is already full.");
  }

  public static void printYourHealthisFullAlready() {
    System.out.println("Your health is already full.");
  }

  public static void printRepair() {
    System.out.println();
    System.out.println("Spacecraft repair was successful.");
    System.out.println();
  }

  public static void printRepairLimit() {
    System.out.println("You are out of repairs!.");
  }

  public static void tellUserToInteractToClearDamageCondition(String preReq, String damageCondition){
    System.out.println("Use " + preReq + " on " + damageCondition + " to clear the way for loading astronauts!");
  }

  public static void cantUnloadOnEarthWithoutAllAstronauts(int remainingAstronauts){
    System.out.println("You can't unload on Earth until you've rescued all astronauts! There are " + remainingAstronauts + " remaining.");
  }
}
