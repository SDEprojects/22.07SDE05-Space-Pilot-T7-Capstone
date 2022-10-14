import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class Main {

  private static final HashMap<String, String> COMMANDS = new HashMap<>() {{
    put("Help", "Displays available commands");
    put("Quit", "Quit the game");
    put("go", "Move the spacecraft when followed by a location (e.g., go mars)");
  }};

  public static void main(String[] args) throws IOException {
    Reader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    // variable used to save user input
    String userInput;
  
    Game game1 = new Game();
    Game.clearConsole();
    game1.printTitle();
    // TODO: print how to play information (commands, etc)

    do {
      // prompt the user to enter a command
      System.out.println("Please enter your next command");
      // save user response and sanitize it (turn it into lower-case and trim whitespaces)
      userInput = reader.readLine().trim().toLowerCase();
      String[] userCommand = textParser(userInput);

      if (userCommand[0].equals("go")) {
        if (userCommand[1].equals("moon")) {
          // do something
        }
        if (userCommand[1].equals("mars")) {
          // do something
        }
        if (userCommand[1].equals("mercury")) {
          // do something
        }
      }

      if (userCommand[0].equals("help")) {
        displayCommands();
      }

      if (userCommand[0].equals("fix")) {
        // fix something
      }

    } while (keepPlaying(userInput));
  }

  // Our own customized text parser, replace with a text parser API later
  private static String[] textParser(String text) {
    String[] result = new String[2];
    String[] splitText = text.split(" ");
    String verb = splitText[0]; // First word
    String noun = splitText[splitText.length - 1]; // Last word
    result[0] = verb;
    result[1] = noun;
    return result;
  }

  private static void displayCommands() {
    for (String command : COMMANDS.keySet()) {
      String explanation = COMMANDS.get(command);
      System.out.println(command + ": " + explanation);
    }
  }

  private static boolean keepPlaying(String command) throws IOException {
    // check if user input was "quit"
    if (command.equals("quit")) {
      // return false, which will break the while loop and end the program
      return false;
      // otherwise, return true and let the user continue playing
    } else {
      return true;
    }
  }

}
