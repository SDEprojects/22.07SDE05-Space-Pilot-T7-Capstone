package view;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

public class GameText {

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final Gson gson = new Gson();

  public void printGameText() {

    try (Reader reader = new InputStreamReader(this.getClass()
        .getResourceAsStream("/game-text.json"))) {
      // convert JSON file to map
      Map<?, ?> map = gson.fromJson(reader, Map.class);

      // print map entries
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        System.out.println(entry.getKey() + "=" + entry.getValue());
      }

      // close reader
      reader.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public static void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public void printTitle() {
    System.out.println(
        ANSI_BLUE +
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
            + ANSI_RESET
    );
  }

  public void printIntro() {
    // TODO: Implement GSON instead of using plain string
    System.out.println(
        "A week ago, an unusual solar activity destroyed space navigation systems\n" +
            "and life-support devices on multiple spaceships, stranding astronauts\n" +
            "on the Moon, Mars, and Mercury.\n" +
            "A former Space Force Astronaut, Colonel Walker, receives a call from NASA.\n" +
            "He is asked to do his one last dance: a Search and Rescue mission.\n" +
            "Given the urgency of the situation, the international community\n" +
            "manages to cooperate and quickly construct one special spacecraft.\n" +
            "But time is not on Walker’s side.\n" +
            "Colonel Walker must rescue every stranded astronaut\n" +
            "and safely bring them home in just one trip.\n" +
            "NASA expects Colonel Walker to bring back at least 4/5 (four-fifths)\n" +
            "of the stranded astronauts (overpopulation, sorry)."
    );
    System.out.println();
  }

  public void printHowToPlay() {
    System.out.println(
        "                   HOW TO PLAY\n\n" +
            "To go to a different planet, use the \"go\" command.\n" +
            "    Go Command Usage Example:\n" +
            "        - go to the moon\n" +
            "        - go to Mars\n" +
            "\n" +
            "If the spacecraft is damaged, you can use the \"repair\" command.\n" +
            "To use \"repair\", there must be at least one engineer onboard.\n" +
            "If the spacecraft's health turns 0, it will explode.\n" +
            "    Repair Command Usage Example:\n" +
            "        - repair\n" +
            "\n" +
            "To load and unload passengers, use the \"load\" and \"unload\" commands.\n" +
            "    Load/Unload Command Usage Example:\n" +
            "        - load\n" +
            "        - unload\n" +
            "\n" +
            "To see all available commands, enter \"help\".\n" +
            "\n" +
            "To quit the program, enter \"quit\"."
    );
    System.out.println();
  }

  public void displayCommands() {
    System.out.println();
    System.out.println(
        "      AVAILABLE COMMANDS:\n\n" +
            "Help: Displays available commands\n" +
            "go: Move the spacecraft when followed by a location (e.g., go mars)\n" +
            "repair: Repair spacecraft (only possible if an engineer is onboard)\n" +
            "load: Load a passenger to the spacecraft\n" +
            "unload: Unload a passenger from the spacecraft"
    );
  }

  public void displayGameState(int remainingAstro, int remainingDays, int shipHealth,
      String planetName) {
    System.out.println();
    System.out.println("Number of Remaining Astronauts: " + remainingAstro);
    System.out.println("Number of Remaining Days: " + remainingDays);
    System.out.println("Ship's Condition: " + shipHealth);
    System.out.println("Current Planet: " + planetName);
  }

  public void noEngineerToRepair() {
    System.out.println("You need to have at least one engineer on board to repair the ship.");
  }

}
