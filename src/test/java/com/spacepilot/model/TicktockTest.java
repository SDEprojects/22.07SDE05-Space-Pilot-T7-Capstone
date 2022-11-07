package com.spacepilot.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TicktockTest {

  @Test
  public void ticktockKeepPlayingBecauseTimerIsStillGoing() {
    Ticktock ticktockClass = new Ticktock();
    ticktockClass.ticktock();
    ticktockClass.setMinutes(2);
    ticktockClass.setSeconds(1);
    ticktockClass.getTimer().start();

    assertEquals(false,ticktockClass.getOxygenTickerLose());
    assertEquals(true,ticktockClass.getTimer().isRunning());
  }

  @Test
  public void ticktockOxygenTickerLoseIsFalseAfterRunningOutOfTime() throws InterruptedException {

    Ticktock ticktockClass = new Ticktock();
    ticktockClass.setMinutes(0);
    ticktockClass.setSeconds(1);
    ticktockClass.ticktock();
    ticktockClass.getTimer().start();

    Thread.sleep(5000);

    assertTrue(ticktockClass.getOxygenTickerLose());
  }
}