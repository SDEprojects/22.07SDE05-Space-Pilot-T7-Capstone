package com.spacepilot;

import com.google.gson.Gson;
import com.spacepilot.controller.Controller;
import com.spacepilot.model.Game;
import com.spacepilot.view.Gui;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class Main {

  public static void main(String[] args) {
//    Release 2.2 into main
    try (Reader input =
        new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input))
    {
      do {
        Game game = createNewGame(); // Model
        Gui gui = new Gui(); // GUI
        game.setOver(false); // Set the current game's status to be not over
        Controller controller = new Controller(game, reader, gui); // Controller
        gui.setControllerField(controller);
        controller.play();
      } while (!createNewGame().isOver());
    } catch (IOException | URISyntaxException | MidiUnavailableException |
             InvalidMidiDataException e) {
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static Game createNewGame() {
    // create a reader
    try (Reader reader = new InputStreamReader(Main.class.getResourceAsStream("/game.json")))
    {
      // convert JSON file to Game and return the Game instance
      return new Gson().fromJson(reader, Game.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}
