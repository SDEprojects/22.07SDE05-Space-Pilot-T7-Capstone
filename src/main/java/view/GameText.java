package view;

import java.util.HashMap;

public class GameText {

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_GREEN = "\u001B[32m";

  public static void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public void printTitle() {
    System.out.println(
      "░██████╗██████╗░░█████╗░░█████╗░███████╗  ██████╗░██╗██╗░░░░░░█████╗░████████╗██╗\n" +
      "██╔════╝██╔══██╗██╔══██╗██╔══██╗██╔════╝  ██╔══██╗██║██║░░░░░██╔══██╗╚══██╔══╝╚═╝\n" +
      "╚█████╗░██████╔╝███████║██║░░╚═╝█████╗░░  ██████╔╝██║██║░░░░░██║░░██║░░░██║░░░░░░\n" +
      "░╚═══██╗██╔═══╝░██╔══██║██║░░██╗██╔══╝░░  ██╔═══╝░██║██║░░░░░██║░░██║░░░██║░░░░░░\n" +
      "██████╔╝██║░░░░░██║░░██║╚█████╔╝███████╗  ██║░░░░░██║███████╗╚█████╔╝░░░██║░░░██╗\n" +
      "╚═════╝░╚═╝░░░░░╚═╝░░╚═╝░╚════╝░╚══════╝  ╚═╝░░░░░╚═╝╚══════╝░╚════╝░░░░╚═╝░░░╚═╝\n" +
      "\n" +
      "██╗░░██╗░█████╗░███╗░░░███╗███████╗██████╗░░█████╗░██╗░░░██╗███╗░░██╗██████╗░\n" +
      "██║░░██║██╔══██╗████╗░████║██╔════╝██╔══██╗██╔══██╗██║░░░██║████╗░██║██╔══██╗\n" +
      "███████║██║░░██║██╔████╔██║█████╗░░██████╦╝██║░░██║██║░░░██║██╔██╗██║██║░░██║\n" +
      "██╔══██║██║░░██║██║╚██╔╝██║██╔══╝░░██╔══██╗██║░░██║██║░░░██║██║╚████║██║░░██║\n" +
      "██║░░██║╚█████╔╝██║░╚═╝░██║███████╗██████╦╝╚█████╔╝╚██████╔╝██║░╚███║██████╔╝\n" +
      "╚═╝░░╚═╝░╚════╝░╚═╝░░░░░╚═╝╚══════╝╚═════╝░░╚════╝░░╚═════╝░╚═╝░░╚══╝╚═════╝░\n"
    );
  }

  public void displayGameState(int remainingAstro, int remainingDays, int shipHealth) {
    System.out.println("Number of Remaining Astronauts: " + remainingAstro);
    System.out.println("Number of Remaining Days: " + remainingDays);
    System.out.println("Ship's Condition: " + shipHealth);
  }

  public void displayCommands() {
    System.out.println(
        "Help: Displays available commands\n" +
            "go: Move the spacecraft when followed by a location (e.g., go mars)\n" +
            "repair: Repair spacecraft (only possible if an engineer is onboard)\n" +
            "load: Load a passenger to the spacecraft\n" +
            "unload: Unload a passenger from the spacecraft"
    );
  }

}
