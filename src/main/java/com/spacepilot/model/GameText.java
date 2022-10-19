package com.spacepilot.model;

public class GameText {

  private String[] title;
  private String[] introduction;
  private String[] instructions;
  private String noEngineerOnBoardAlert;
  private String[] npcDialogue;
  private String userWon;
  private String userLost;

  public String[] getTitle() {
    return title;
  }

  public String[] getIntroduction() {
    return introduction;
  }

  public String[] getInstructions() {
    return instructions;
  }

  public String getNoEngineerOnBoardAlert() {
    return noEngineerOnBoardAlert;
  }

  public String[] getNpcDialogue() {
    return npcDialogue;
  }

  public String getUserWon() {
    return userWon;
  }

  public String getUserLost() {
    return userLost;
  }

}
