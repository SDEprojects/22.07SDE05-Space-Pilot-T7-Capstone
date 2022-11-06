package com.spacepilot.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageUI {

  private JPanel containerPanel;
  private JPanel imagePanel;
  private JPanel bgPanel[] = new JPanel[10];
  private JLabel bgLabel[] = new JLabel[10];
  private Runnable loadRunnableListener;
  private Runnable unloadRunnableListener;
  private Runnable refuelRunnableListener;
  private Runnable interactRunnableListener;
  private Runnable goOrbitRunnableListener;

  private List<String> allPlanets = new ArrayList<>(Arrays.asList("Jupiter", "Mars",
      "Mercury", "Moon", "Neptune", "Orbit", "Saturn", "Uranus", "Venus"));
  private String item;
  private List<String> inventory;
  private String dangerCondition;
  private String currentPlanet;
  private int numberOfAstronauts;
  private JPanel guiInventoryPanel;
  private JLabel refuelsLeftLabel;
  private int refuelsLeft;




  public ImageUI(JPanel containerPanel, Runnable loadRunnable,
      Runnable unloadRunnable, Runnable refuelRunnable,
      Runnable interactRunnable, Runnable goOrbitRunnable) {
    this.containerPanel = containerPanel;
    this.loadRunnableListener = loadRunnable;
    this.unloadRunnableListener = unloadRunnable;
    this.refuelRunnableListener  = refuelRunnable;
    this.interactRunnableListener = interactRunnable;
    this.goOrbitRunnableListener = goOrbitRunnable;

  }


  public void addUpdateToPanel(){
    containerPanel.add(imagePanel, BorderLayout.CENTER);
  }

  public void createTopLevelPanel(){
    imagePanel = new JPanel();
    imagePanel.setBackground(Color.black);
    imagePanel.setLayout(new BorderLayout());
  }

  //Creates panel to hold background label
  public void createBackgroundPanel(int bgNum){

    //creating background panels
    bgPanel[bgNum] = new JPanel();
    bgPanel[bgNum].setBackground(Color.red);
    bgPanel[bgNum].setBounds(0, 0, 1024, 640); //since layout null, setBounds
    bgPanel[bgNum].setLayout(null);
    imagePanel.add(bgPanel[bgNum]);

  }

  //Creates label with background image
  public void createBackgroundLabel(int bgNum, String bgFileName){
    //Set label to icon. Sizing should match panel its within
    ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource(bgFileName));
    bgLabel[bgNum] = new JLabel();
    bgLabel[bgNum].setBounds(0,0, 1024, 640);
    bgLabel[bgNum].setIcon(bgIcon);
  }

  //Creates object image within background
  public void createObject(int bgNum, int objx, int objy, int objWidth, int objHeight, String objFileName, Runnable cmdRunnable){
    //create label
    JButton objectButton = new JButton();
    objectButton.setBounds(objx, objy, objWidth, objHeight); //sets location for astronaut

    //Button formatting
    objectButton.setContentAreaFilled(false);//clears white fill
    objectButton.setBorderPainted(false);//clears border
    objectButton.setFocusPainted(false);//clears focus border from click

    //Set click actions
    objectButton.addActionListener(e -> cmdRunnable.run());

    //attach icon to it
    ImageIcon objectIcon = new ImageIcon(getClass().getClassLoader().getResource(objFileName));

    objectButton.setIcon(objectIcon);
    //add to panel background
    bgPanel[bgNum].add(objectButton);
    bgPanel[bgNum].add(bgLabel[bgNum]); //adds background here

  }

  //Creates map object to open map with click
  public void createMapObject(int bgNum, int objx, int objy, int objWidth, int objHeight, String objFileName){
    //create label
    JButton objectLabel = new JButton();
    objectLabel.setBounds(objx, objy, objWidth, objHeight); //sets location for astronaut

    //Button formatting
    objectLabel.setBackground(null);
    objectLabel.setContentAreaFilled(false);//clears white fill
    objectLabel.setBorderPainted(false);//clears border
    objectLabel.setFocusPainted(false);//clears focus border from click

    //Set click actions
    objectLabel.addActionListener(e -> goOrbitRunnableListener.run());

    //attach icon to it
    ImageIcon objectIcon = new ImageIcon(getClass().getClassLoader().getResource(objFileName));

    objectLabel.setIcon(objectIcon);
    //add to panel background
    bgPanel[bgNum].add(objectLabel);
    bgPanel[bgNum].add(bgLabel[bgNum]); //adds background here

  }


  //Method to update fields to current game statuses
  public void planetBackgroundCustomization(String item, String dangerCondition,
      int numberOfAstronauts, String currentPlanet, List<String> inventory){
    //Assign all values to fields
    this.item = item;
    this.dangerCondition = dangerCondition;
    this.numberOfAstronauts = numberOfAstronauts;
    this.currentPlanet = currentPlanet;
    this.inventory = inventory;
  }

  //Uses current game status to create updated planet backgrounds
  public void createPlanetScreen(){
    //Clear entire panel
    bgPanel[2].removeAll();

    //Adds currentPlanet background and a spaceship.
    if(allPlanets.contains(currentPlanet)){
      switch(currentPlanet){
        case "Jupiter":
          createBackgroundLabel(2, "backgrounds/jupiter.png");
          createMapObject(2, 0, 50, 499, 499, "backgrounds/spaceship-499x499.png");
          break;
        case "Mars":
          createBackgroundLabel(2, "backgrounds/mars.png");
          createMapObject(2, 0, 150, 499, 499, "backgrounds/spaceship-499x499.png");
          break;
        case "Mercury":
          createBackgroundLabel(2, "backgrounds/mercury.png");
          createMapObject(2, 0, 150, 499, 499, "backgrounds/spaceship-499x499.png");
          break;
        case "Moon":
          createBackgroundLabel(2, "backgrounds/moon.png");
          createMapObject(2, 0, 50, 499, 499, "backgrounds/spaceship-499x499.png");
          break;
        case "Neptune":
          createBackgroundLabel(2, "backgrounds/neptune.jpeg");
          createMapObject(2, 0, 150, 499, 499, "backgrounds/spaceship-499x499.png");
          break;
        case "Saturn":
          createBackgroundLabel(2, "backgrounds/saturn.png");
          createMapObject(2, 0, 150, 499, 499, "backgrounds/spaceship-499x499.png");
          break;
        case "Uranus":
          createBackgroundLabel(2, "backgrounds/uranus.png");
          createMapObject(2, 0, 200, 399, 399, "backgrounds/spaceship-499x499.png");
          break;
        case "Venus":
          createBackgroundLabel(2, "backgrounds/venus.png");
          createMapObject(2, 0, 200, 399, 399, "backgrounds/spaceship-499x499.png");
          break;
        default:
          return;
      }
    }
    //Check if astronauts
    if(numberOfAstronauts > 0){ //check is astronauts
      createObject(2, 725, 325, 225, 225, "backgrounds/astronautGroup.png", loadRunnableListener);
    }
    //Check if item
    if(item != null){
      //create item based on planet
      switch (item){
        case "weapon":
          createObject(2, 725, 50, 320, 320, "backgrounds/weapon.png", loadRunnableListener);
          break;
        case "gas mask":
          createObject(2, 800, 175, 143, 125, "backgrounds/gasmask.png", loadRunnableListener);
          break;
        case "alien baby":
          createObject(2, 825, 165, 101, 150, "backgrounds/alienBaby.png", loadRunnableListener);
          break;
        case "cold shield":
          createObject(2, 750, 115, 250, 250, "backgrounds/coldShield.png", loadRunnableListener);
          break;
        default:
          return;
      }

      //create item bubble
      createObject(2, 650, 100, 450, 316, "backgrounds/itemBubble.png", loadRunnableListener);

    }
    //Check which dangerCondition to add
    if(dangerCondition != null) {
      switch (dangerCondition) {
        case "frost alien":
          createObject(2, 450, 200, 280, 320, "backgrounds/blueAlien.png",
              interactRunnableListener);
          break;
        case "Alien Mom":
          createObject(2, 450, 200, 280, 320, "backgrounds/greenAlien.png",
              interactRunnableListener);
          break;
        case "extreme cold":
          createObject(2, 350, 65, 400, 500, "backgrounds/extremeCold.png",
              interactRunnableListener);
          break;
        case "poisonous gas":
          createObject(2, 500, 200, 280, 320, "backgrounds/poisonGas.png",
              interactRunnableListener);
          break;
        default:
          return;
      }
    }
    addUpdateToPanel();
    loopThroughInventoryToCheckCurrentItems();
  }

  //Loops through current inventory to check which items there
  public void loopThroughInventoryToCheckCurrentItems(){
    //Clears inventory panel
    guiInventoryPanel.removeAll();

    for(String item : inventory){
      switch (item){
        case "weapon":
          addHeldItemsToInventoryImagePanel("backgrounds/weapon.png");
          break;
        case "gas mask":
          addHeldItemsToInventoryImagePanel("backgrounds/gasmask.png");
          break;
        case "alien baby":
          addHeldItemsToInventoryImagePanel("backgrounds/alienBaby.png");
          break;
        case "cold shield":
          addHeldItemsToInventoryImagePanel("backgrounds/coldShield.png");
          break;
        default:
          return;
      }
    }
  }

  //Adds items in inventory to panel with an image
  public void addHeldItemsToInventoryImagePanel(String fileName){
    //Create a new label
    JLabel itemLabel = new JLabel();
    //create icon
    ImageIcon objectIcon = new ImageIcon(getClass().getClassLoader().getResource(fileName));
    Image img = objectIcon.getImage();
    Image newImg = img.getScaledInstance(100, 50, Image.SCALE_DEFAULT);
    objectIcon = new ImageIcon(newImg);
    //assign icon to label
    itemLabel.setIcon(objectIcon);
    //set alignment of images to center right
    itemLabel.setHorizontalAlignment(JLabel.RIGHT);
    itemLabel.setVerticalAlignment(JLabel.CENTER);
    //add to inventory panel
    guiInventoryPanel.add(itemLabel);
  }

  //Creates each background panel with image buttons
  public void generateScene(){
    //Earth Scene 1
    createBackgroundPanel(1);
    createBackgroundLabel(1, "backgrounds/earth.png");
    createMapObject(1, 0, 125, 499, 499, "backgrounds/spaceship-499x499.png");
    createObject(1, 400, 350, 647, 385, "backgrounds/building.png", unloadRunnableListener);

    //Planet Scene 2
    createBackgroundPanel(2);
    createBackgroundLabel(2, "backgrounds/moon.png");

    //Station Scene 3
    createBackgroundPanel(3);
    createBackgroundLabel(3, "backgrounds/station.png");
    createMapObject(3, 0, 50, 499, 499, "backgrounds/spaceship-499x499.png");
      //Creates Label tracking refuels on station
    refuelsLeftLabel = new JLabel("3 / 3");
    refuelsLeftLabel.setForeground(Color.white);
    refuelsLeftLabel.setLayout(null);
    refuelsLeftLabel.setBounds(839, 190, 150, 160);
    bgPanel[3].add(refuelsLeftLabel);
    bgPanel[3].add(bgLabel[3]); //adds background here
      //Creates the gas pump
    createObject(3, 720, 200, 233, 360, "backgrounds/gas.png", refuelRunnableListener);

    addUpdateToPanel();
  }

  //Updates fuel count after refuel is clicked
  public void updateRefuelsOnStation(){
    refuelsLeftLabel.setText(refuelsLeft + " / 3");
  }

  //Rotates background scenes
  public void showPlanetScreen1(){
    bgPanel[1].setVisible(false);
    bgPanel[3].setVisible(false);
    bgPanel[2].setVisible(true);
  }

  public void showEarthScreen2(){
    bgPanel[2].setVisible(false);
    bgPanel[3].setVisible(false);
    bgPanel[1].setVisible(true);
  }

  public void showStationScreen3(){
    bgPanel[1].setVisible(false);
    bgPanel[2].setVisible(false);
    bgPanel[3].setVisible(true);
  }


  public void setCurrentPlanet(String currentPlanet) {
    this.currentPlanet = currentPlanet;
  }

  public void setGuiInventoryPanel(JPanel guiInventoryPanel) {
    this.guiInventoryPanel = guiInventoryPanel;
  }

  public void setRefuelsLeft(int refuelsLeft) {
    this.refuelsLeft = refuelsLeft;
  }
}
