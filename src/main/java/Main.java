import controller.Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import model.Game;
import view.GameText;

public class Main {

  public static void main(String[] args) throws IOException {
    try (
        Reader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
    ) {
      Game game = new Game(); // Model: Game class stores the current state of the game
      GameText view = new GameText(); // View
      Controller controller = new Controller(game, view, reader);
      controller.play();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
