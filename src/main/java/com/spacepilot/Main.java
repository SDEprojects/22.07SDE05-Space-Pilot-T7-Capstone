package com.spacepilot;

import com.google.gson.Gson;
import com.spacepilot.controller.Controller;
import com.spacepilot.model.Music;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import com.spacepilot.model.Game;
import com.spacepilot.view.View;
import java.net.URISyntaxException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class Main {

  public static void main(String[] args) {
    try (
        Reader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
    ) {
      Music.playMusic("src/main/resources/black-hole-audio.mp3.mid");
      createNewGame();
      Game game = createNewGame(); // Model
      View view = new View(); // View
      Controller controller = new Controller(game, view, reader); // Controller
      controller.play();
    } catch (IOException | InvalidMidiDataException | MidiUnavailableException e) {
      throw new RuntimeException(e);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public static Game createNewGame() {
    // create a reader
    try (Reader reader = new InputStreamReader(
        Main.class.getResourceAsStream("/game.json"))
    ) {
      // convert JSON file to Game
      return new Gson().fromJson(reader, Game.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
