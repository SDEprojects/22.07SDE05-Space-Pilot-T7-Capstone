package com.spacepilot.model;

import static com.spacepilot.controller.Controller.createPlanets;
import static com.spacepilot.controller.Controller.game;
import static org.junit.Assert.*;

import com.spacepilot.controller.Controller;
import com.spacepilot.view.Gui;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.junit.Before;
import org.junit.Test;

public class PlanetTest {

  @Before
  public void setUp() throws Exception {
    Game game = new Game();
    Gui gui = new Gui();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Controller controller = new Controller(game,reader,gui);
    game.setPlanets(createPlanets());

  }

  @Test
  public void testPlaceAstronautsMethodDoesNotPlaceMoreThanFourAstronautsPerPlanet() {

   //Tests each planet to see if there are more than 4 astronauts
    for (Planet planet : game.getPlanets()) {
      int numOfAstronautsOnPlanet = planet.getNumOfAstronautsOnPlanet();
      assertTrue(numOfAstronautsOnPlanet<=4);
    }
  }

  @Test
  public void testPlaceAstronautsMethodDoesNotPlaceANegativeNumberOfAstronautsPerPlanet() {
    //Tests each planet to see if there are less than 0 astronauts
    for (Planet planet : game.getPlanets()) {
      int numOfAstronautsOnPlanet = planet.getNumOfAstronautsOnPlanet();
      assertTrue(numOfAstronautsOnPlanet>=0);
    }
  }

}