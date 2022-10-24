package com.spacepilot;

import com.google.gson.Gson;
import com.spacepilot.controller.Controller;
import com.spacepilot.model.Game;
import com.spacepilot.model.Music;
import com.spacepilot.view.View;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class Main {

  public static void main(String[] args) {
    try (Reader input = new InputStreamReader(
        System.in); BufferedReader reader = new BufferedReader(input);) {
      boolean continuePlaying = true;

      while (continuePlaying) {
        Music.playMusic("src/main/resources/black-hole-audio.mp3.mid");
        Game game = createNewGame(); // Model
        View view = new View(); // View
        Controller controller = new Controller(game, view, reader); // Controller
        controller.play();
        //Prompt user if another game
        controller.getUserInput("Turn & Burn! Would you like to play again?\n"
            + "Enter n for no (quit)\n"
            + "or enter anything else to play another game\n");
        //If the input is n, chance continuePlaying to false
        if (controller.getUserInput().equals("n")) {
          continuePlaying = false;
        }
        game.setOver(false);
      }
    } catch (IOException | URISyntaxException | MidiUnavailableException |
             InvalidMidiDataException e) {
      throw new RuntimeException(e);
    }
  }

  public static Game createNewGame() {
    // create a reader
    try (Reader reader = new InputStreamReader(Main.class.getResourceAsStream("/game.json"))
    ) {
      // convert JSON file to Game
      return new Gson().fromJson(reader, Game.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
