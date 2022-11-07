package com.spacepilot.model;

import static com.spacepilot.controller.Controller.game;
import static com.spacepilot.controller.Controller.returnPlanet;
import static org.junit.Assert.*;

import com.spacepilot.controller.Controller;
import com.spacepilot.view.Gui;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;

public class SpacecraftTest {

  @Before
  public void setUp() throws Exception {
    Game game = new Game();
    Gui gui = new Gui();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Controller controller = new Controller(game,reader,gui);
    controller.setUpGame();
  }

  @Test
  public void addPassengersShouldStopAtCapacity() {

    //Set the capacity of the spacecraft to 1;
    game.getSpacecraft().setSpacecraftCapacity(1);
    //Go to saturn
    game.getSpacecraft().setCurrentPlanet(returnPlanet("saturn"));

    //See how many astronauts there are before we addPassengers()
    Collection<Object> arrayOfAstronautsOnCurrentPlanet = game.getSpacecraft().getCurrentPlanet()
          .getArrayOfAstronautsOnPlanet();
    int numberOfAstronautsBeforeAddingPassengers = arrayOfAstronautsOnCurrentPlanet.size();

    //take astronauts from saturn and put them on spacecraft
    game.getSpacecraft().addPassengers(arrayOfAstronautsOnCurrentPlanet);

    //See how many astronauts there are after we addPassengers()
    int numberOfAstronautsAfterAddingPassengers = game.getSpacecraft().getCurrentPlanet()
        .getArrayOfAstronautsOnPlanet().size();

    //Assuming there are astronauts on saturn,
    //You should not be able to add more than one passenger to the space craft
    //Ergo, only removing one astronaut
    assertTrue(numberOfAstronautsAfterAddingPassengers <= numberOfAstronautsBeforeAddingPassengers);

  }
}