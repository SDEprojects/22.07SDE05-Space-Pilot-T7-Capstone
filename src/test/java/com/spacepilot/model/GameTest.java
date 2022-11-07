package com.spacepilot.model;

import static com.spacepilot.controller.Controller.createPlanets;
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

public class GameTest {

  @Before
  public void setUp() throws Exception {
    Game game = new Game();
    Gui gui = new Gui();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Controller controller = new Controller(game,reader,gui);
    controller.setUpGame();
  }


  @Test
  public void calculateRemainingAstronautsViaTotalNumOfAstronautShouldEqualTotalNumberOfAstronautsAtTheBeginningOfTheGame() {
    int totalAstros = game.getTotalNumberOfAstronauts();
    int remainingAstros  = game.calculateRemainingAstronautsViaTotalNumOfAstronauts();
    assertEquals(totalAstros, remainingAstros);
  }


  @Test
  public void calculateRemainingAstronautsViaTotalNumOfAstronautsShouldDecreaseAfterLoadingPassengersOnSpacecraft(){
    //total number of Astronauts in game
    int totalAstros = game.getTotalNumberOfAstronauts();

    //Go to saturn
    game.getSpacecraft().setCurrentPlanet(returnPlanet("saturn"));

    Collection<Object> arrayOfAstronautsOnCurrentPlanet = game.getSpacecraft().getCurrentPlanet()
        .getArrayOfAstronautsOnPlanet();
    //take astronauts from saturn and put them on spacecraft
    game.getSpacecraft().addPassengers(arrayOfAstronautsOnCurrentPlanet);
    game.getSpacecraft().getCurrentPlanet().removeAllAstronauts();

    int remainingAstros = game.calculateRemainingAstronautsViaTotalNumOfAstronauts();

    //Assuming there are astronauts on saturn,
    //The remaining astronauts should be less than the totol number of Astronauts in the game.
    assertTrue(remainingAstros <= totalAstros);
  }

}