import com.google.gson.Gson;
import controller.Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import model.Game;
import view.GameText;

public class Main {

  private static Game game;

  public static void main(String[] args) {
    try (
        Reader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
    ) {
      createNewGame();
      GameText view = new GameText(); // View
      Controller controller = new Controller(game, view, reader); // Controller
      controller.play();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void createNewGame() {
    // create a reader
    try (Reader reader = new InputStreamReader(
        Main.class.getResourceAsStream("/new-game-data.json"))
    ) {
      // convert JSON file to Game
      game = new Gson().fromJson(reader, Game.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
