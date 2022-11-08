package com.spacepilot.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TicktockTest {

  private Ticktock ticktockClass;

  @Before
  public void setUp() throws Exception {
    //Create a timer and set the time to run
    Ticktock ticktockClass = new Ticktock();
  }

  @Test
  public void ticktockKeepPlayingBecauseTimerIsStillGoing() {


    ticktockClass.ticktock();
    ticktockClass.setMinutes(2);
    ticktockClass.setSeconds(1);
    ticktockClass.getTimer().start();

    //Makes sure the timer is still running
    assertEquals(true,ticktockClass.getTimer().isRunning());
    //The oxygenTickerLose Field should remain false until time has run out
    assertEquals(false,ticktockClass.getOxygenTickerLose());
  }

  @Test
  public void ticktockOxygenTickerLoseIsFalseAfterRunningOutOfTime() throws InterruptedException {
   //Set the timer to 00:01 seconds
    ticktockClass.setMinutes(0);
    ticktockClass.setSeconds(1);
    ticktockClass.ticktock();
    ticktockClass.getTimer().start();

    //Lets the timer run for 5
    Thread.sleep(5000);

    //After timer runs out, oxygenTickerLose should turn true
    assertTrue(ticktockClass.getOxygenTickerLose());
  }
}