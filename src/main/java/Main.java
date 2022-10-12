import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Main {

  public static void main(String[] args) throws IOException {
    Reader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    // variable used to save user input
    String userInput;

    Game game1 = new Game();
    Game.clearConsole();
    game1.displayAscii();

    do {
      // prompt the user to enter a command
      System.out.println("Please enter your next command");
      // save user response and sanitize it (turn it into lower-case and trim whitespaces)
      userInput = reader.readLine().trim().toLowerCase();
      System.out.println("You entered: " + userInput);
    } while (keepPlaying(userInput));
  }

  private static boolean keepPlaying(String command)
      throws IOException {
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
