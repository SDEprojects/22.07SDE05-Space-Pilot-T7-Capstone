package com.spacepilot.view;

import com.spacepilot.controller.Controller;
import com.spacepilot.model.Music;
import com.spacepilot.model.Planet;
import com.spacepilot.model.Ticktock;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.function.Consumer;
import javax.sound.sampled.FloatControl;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class Gui {

  public static final Color VERY_DARK_RED = new Color(153, 0, 0);
  public static final Color DARK_ORANGE = new Color(255, 102, 0);
  private JFrame frame;
  private JTextArea displayArea;
  private JScrollPane scrollPanel;
  private JProgressBar shipHealthBar, fuelLevelBar;
  private FloatControl gainControl;
  private JSlider slider;
  private Consumer<String> method;
  private Controller controllerField;


  private JPanel titleScreenPanel, titleBtnPanel, controlPanel, statusPanel, centralDisplayPanel,
       planetStatusPanel, menuPanel, soundPanel, mapPanel, gameOverPanel;
  private  JLabel titleLabel, currentPlanetLabel, damageConditionLabel, itemsOnPlanetLabel,
       numberOfAstronautsOnPlanetLabel, strandedAstronautsLabel, inventoryLabel, repairsLeftLabel;
  private JButton continueBtn, startBtn;
  public JButton mapBtn, menuBtn, repairBtn, helpBtn, loadBtn, unloadBtn, refuelBtn, interactBtn, godModeBtn, mainBtn ;
  private float currentVolume;
  private int health = 100;
  private double fuel = 100;
  private Font normalFont;

  public Gui() {

    //Different type of layouts to use on JPanels and JFrames as needed.
    BorderLayout borderLayout = new BorderLayout();
    GridBagLayout gridBagLayout = new GridBagLayout();
    GridLayout gridLayout = new GridLayout(0, 1, 5,
        5); //0 rows, 1 col, 5 horizontal gap btw buttons, 5 vertical gap
    FlowLayout flowLayout = new FlowLayout();

    //CREATES OUTERMOST MAIN FRAME
    frame = new JFrame("Main Panel"); //Create Frame for content //Default layout is BorderLayout
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Set closing event
    frame.setSize(new Dimension(800, 500));

    //Centers a frame onscreen when it opens
    frame.setLocationRelativeTo(null);



    //These methods are used to create sections of the gui
    createCenterDisplayPanel();
    createRightSideControlPanel(gridLayout);
    createMenuPanel();
    createMapPanel();
    createTopOfScreenStatusPanel();
    createMusicPanelInMenuScreen();
    createBottomPlanetStatusPanel();


    //Creating central Panel to hold DisplayArea and PlanetStatusDisplayArea
    centralDisplayPanel = new JPanel();
    centralDisplayPanel.setLayout(borderLayout);
    centralDisplayPanel.add(scrollPanel, BorderLayout.CENTER);
    centralDisplayPanel.add(planetStatusPanel, BorderLayout.PAGE_END);
  }


// THESE METHODS CREATE DIFFERENT SECTIONS OF THE GUI
  private void createBottomPlanetStatusPanel() {
    //CREATES BOTTOM PANEL: (Display Current Planet Status)
    planetStatusPanel = new JPanel();

    itemsOnPlanetLabel = new JLabel("Items on Planet:");
    numberOfAstronautsOnPlanetLabel = new JLabel("# of Astronauts on Planet: ");
    damageConditionLabel = new JLabel("Danger Condition: ");

    planetStatusPanel.setLayout(new GridLayout(1, 3, 3, 3)); //Layout to spread labels out
    planetStatusPanel.add(numberOfAstronautsOnPlanetLabel);
    planetStatusPanel.add(itemsOnPlanetLabel);
    planetStatusPanel.add(damageConditionLabel);
  }

  private void createCenterDisplayPanel() {
    //CREATES CENTER DISPlAY FOR TEXT OUTPUTS
    displayArea = new JTextArea();
    scrollPanel = new JScrollPane(displayArea); //scrollpane to let text scroll
    displayArea.setEditable(false);//stop display from being edited
    displayArea.setWrapStyleWord(true); //wrap at word boundaries, not characters
    scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    displayArea.setLineWrap(true);
  }

  private void createTopOfScreenStatusPanel() {
    //CREATES TOP PANEL: (Status of Spaceship)
    statusPanel = new JPanel();
    statusPanel.setBackground(Color.gray);
    statusPanel.setBounds(100, 15, 200, 30);
    GridLayout panelGridLayout = new GridLayout(3, 2, 2, 3); //Created grid layout
    statusPanel.setLayout(panelGridLayout); //Set status panel to gridLayout

    //Creating the Labels (for updating and displaying text)
    currentPlanetLabel = new JLabel("Current Planet:"); //Labels can have string names and icons
    repairsLeftLabel = new JLabel("Repairs Left:");
    strandedAstronautsLabel = new JLabel("Stranded Astronauts:");
    inventoryLabel = new JLabel("Inventory:");
    Ticktock.setOxygenTimeLeftLabel(new JLabel("Oxygen Time Remain: 03:00"));

    //creating time thread (for oxygen display)
    Ticktock.setMinutes(3);
    Ticktock.setSeconds(0);
    Ticktock.ticktock();
    Ticktock.getTimer().start();

    //creating ship health bar
    shipHealthBar = new JProgressBar(0, 100);
    shipHealthBar.setForeground(VERY_DARK_RED);
    shipHealthBar.setBackground(Color.gray);
    shipHealthBar.setBorder(BorderFactory.createLineBorder(VERY_DARK_RED, 2));
    shipHealthBar.getUI();
    shipHealthBar.setUI(new BasicProgressBarUI() {
      protected Color getSelectionBackground() { return Color.black; }
      protected Color getSelectionForeground() { return Color.black; }
    });
    shipHealthBar.setValue(health);
    shipHealthBar.setString("Health: " + health + "%");
    shipHealthBar.setStringPainted(true);

    //creating ship fuel level bar
    fuelLevelBar = new JProgressBar(0, 100);
    fuelLevelBar.setForeground(DARK_ORANGE);
    fuelLevelBar.setBorder(BorderFactory.createLineBorder(DARK_ORANGE, 2));
    fuelLevelBar.setBackground(Color.gray);
    fuelLevelBar.getUI();
    fuelLevelBar.setUI(new BasicProgressBarUI() {
      protected Color getSelectionBackground() { return Color.black; }
      protected Color getSelectionForeground() { return Color.black; }
    });
    fuelLevelBar.setValue((int) getFuel());
    fuelLevelBar.setString("Fuel: " + getFuel() + "%");
    fuelLevelBar.setStringPainted(true);
    JTextArea fuelLevelText = new JTextArea("100");

    //Adding Labels to the status panel
    statusPanel.add(currentPlanetLabel);
    statusPanel.add(Ticktock.getOxygenTimeLeftLabel());
    statusPanel.add(shipHealthBar);
    statusPanel.add(repairsLeftLabel);
    statusPanel.add(strandedAstronautsLabel);
    statusPanel.add(fuelLevelBar);
    statusPanel.add(inventoryLabel);
  }

  private void createMusicPanelInMenuScreen() {
    //CREATES MUSIC PANEL
    soundPanel = new JPanel();

    //creates buttons for music panel
    JButton muteBtn = new JButton("Mute FX");
    JButton playPauseBtn = new JButton("Play/Pause");
    JButton track1B = new JButton("Track 1");
    JButton track2B = new JButton("Track 2");
    JButton track3B = new JButton("Track 3");
    JButton track4B = new JButton("Track 4");
    slider = new JSlider(-40, 6);

    //button plays and pauses current track
    method = i -> Music.musicMute();
    soundButtons(playPauseBtn, method, null);
    //button mutes and unMutes FX
    method = i -> Music.fxMute();
    soundButtons(muteBtn, method, null);
    //button plays track 1 as background music
    method = wavFile -> Music.track1(wavFile);
    soundButtons(track1B, method, "Space_Chill.wav");
    //button plays track 2 as background music
    method = wavFile -> Music.track2(wavFile);
    soundButtons(track2B, method, "Space_Ambient.wav");
    //button plays track 3 as background music
    method = wavFile -> Music.track3(wavFile);
    soundButtons(track3B, method, "Space_Cinematic.wav");
    //button plays track 4 as background music
    method = wavFile -> Music.track4(wavFile);
    soundButtons(track4B, method, "Space_Cyber.wav");
    //slider is implemented to adjust volume up and down for current background music
    slider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {

        currentVolume = Music.currentVolume();
        currentVolume = slider.getValue();
        gainControl = Music.gainControl();
        gainControl.setValue(currentVolume);

      }
    });

    playMusic();

    //adds buttons to music panel
    soundPanel.add(playPauseBtn);
    soundPanel.add(muteBtn);
    soundPanel.add(track1B);
    soundPanel.add(track2B);
    soundPanel.add(track3B);
    soundPanel.add(track4B);
    soundPanel.add(slider);
  }

  private void createRightSideControlPanel(GridLayout gridLayout) {
    //CREATING PANEL ON THE RIGHT TO HOLD BUTTONS
    controlPanel = new JPanel();
    controlPanel.setBackground(Color.blue);

    //creating buttons right panel
    mapBtn = new JButton("Map");
    menuBtn = new JButton("Menu");
    repairBtn = new JButton("Repair");
    helpBtn = new JButton("Help");
    loadBtn = new JButton("Load");
    unloadBtn = new JButton("Unload");
    refuelBtn = new JButton("Refuel");
    interactBtn = new JButton("Interact");
    godModeBtn = new JButton("GOD MODE");
    mainBtn = new JButton("Main Screen");

    // When a user presses a button, the respective word is given to the Controller to use for function
      chaChaRealSmooth(godModeBtn,"god",false);
      chaChaRealSmooth(loadBtn,"load",false);
      chaChaRealSmooth(unloadBtn,"unload",false);
      chaChaRealSmooth(refuelBtn,"refuel",false);
      chaChaRealSmooth(repairBtn,"repair",false);
      chaChaRealSmooth(helpBtn,"help",false);
    //Event Listener for menu button to open new window w/menu options
    menuBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMenu();
      }
    });
    mapBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMap();
      }
    });
    mainBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMain();
      }
    });

    //adding button to right panel: Control Panel
    controlPanel.setLayout(gridLayout); //Setting controlPanel to grid layout
    controlPanel.add(menuBtn);
    controlPanel.add(mapBtn);
    controlPanel.add(mainBtn);
    controlPanel.add(repairBtn);
    controlPanel.add(helpBtn);
    controlPanel.add(loadBtn);
    controlPanel.add(unloadBtn);
    controlPanel.add(refuelBtn);
    controlPanel.add(godModeBtn);
    controlPanel.add(interactBtn);
  }

  public void createTitleScreen(){
    //Create components

    titleScreenPanel= new JPanel();
    titleLabel  = new JLabel("Space Pilot", SwingConstants.CENTER); //centers label
    Font titleFont = new Font("Times New Roman", Font.PLAIN, 90);
    normalFont = new Font("Times New Roman", Font.PLAIN, 30);
    startBtn = new JButton("Start Game");
    continueBtn = new JButton("Continue Game");
    titleBtnPanel = new JPanel();
    //Set up titlePanel
    titleScreenPanel.setLayout(new GridLayout(2, 1, 5, 4));
    titleScreenPanel.setBackground(Color.black);
    //Set up titleLabel
    titleLabel.setFont(titleFont);
    titleLabel.setForeground(Color.white);
    //Set up titleButtons & Panel
    startBtn.setBackground(Color.black);
    continueBtn.setBackground(Color.black);
    titleBtnPanel.setBackground(Color.black);
    //Add components together
    titleScreenPanel.add(titleLabel);
    titleBtnPanel.add(startBtn);
    titleBtnPanel.add(continueBtn);
    titleScreenPanel.add(titleBtnPanel);

    //Add btn listeners
    startBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showGameScreenPanels();
      }
    });
    chaChaRealSmooth(continueBtn,"continue",false);
  }

  public void soundButtons(JButton btn, Consumer<String> musicMethod, String wavFile) {
    btn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        musicMethod.accept(wavFile);
      }
    });
  }

  public void createMenuPanel() {

    menuPanel = new JPanel(); //Create Panel for content

    //Creating a menu
    menuPanel.setBackground(Color.black);
    //Creating menu buttons
    JButton soundSettingsBtn = new JButton("Sound Settings");
    soundSettingsBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showSoundSettings();
      }
    });
    JButton videoSettingsBtn = new JButton("Video Settings");
    JButton saveGameBtn = new JButton("Save");
    JButton loadSaveGameBtn = new JButton("Load Saved Game");
    JButton saveAndQuitGameBtn = new JButton("Quit Game");
    menuPanel.add(soundSettingsBtn); //Adding all buttons to menu frame
    menuPanel.add(videoSettingsBtn);
    menuPanel.add(saveGameBtn);
    menuPanel.add(loadSaveGameBtn);
    menuPanel.add(saveAndQuitGameBtn);
  }

  public void createMapPanel() {
    mapPanel = new JPanel(); //Create Panel for content
    //Creating a menu
    mapPanel.setBackground(Color.black);
    //Creating maps buttons to go to respective planets below
    JButton earthBtn = new JButton("Earth");
    chaChaRealSmooth(earthBtn,"go earth",true);
    JButton moonBtn = new JButton("Moon");
    chaChaRealSmooth(moonBtn,"go moon",true);
    JButton marsBtn = new JButton("Mars");
    chaChaRealSmooth(marsBtn,"go mars",true);
    JButton mercuryBtn = new JButton("Mercury");
    chaChaRealSmooth(mercuryBtn,"go mercury",true);
    JButton saturnBtn = new JButton("Saturn");
    chaChaRealSmooth(saturnBtn,"go saturn",true);
    JButton venusBtn = new JButton("Venus");
    chaChaRealSmooth(venusBtn,"go venus",true);
    JButton neptuneBtn = new JButton("Neptune");
    chaChaRealSmooth(neptuneBtn,"go neptune",true);
    JButton jupiterBtn = new JButton("Jupiter");
    chaChaRealSmooth(jupiterBtn,"go jupiter",true);
    JButton stationBtn = new JButton("Station");
    chaChaRealSmooth(stationBtn,"go station",true);
    JButton uranusBtn = new JButton("Uranus");
    chaChaRealSmooth(uranusBtn,"go uranus",true);
    mapPanel.add(earthBtn); //Adding all buttons to menu frame
    mapPanel.add(moonBtn);
    mapPanel.add(marsBtn);
    mapPanel.add(mercuryBtn);
    mapPanel.add(jupiterBtn);
    mapPanel.add(neptuneBtn);
    mapPanel.add(venusBtn);
    mapPanel.add(uranusBtn);
    mapPanel.add(saturnBtn);
    mapPanel.add(stationBtn);
  }

  //TODO
  //Once refactoring is complete, fix this method
//  public void createGameOverScreen(){
//    planetStatusPanel.setVisible(false);
//    centralDisplayPanel.setVisible(false);
//    statusPanel.setVisible(false);
//    controlPanel.setVisible(false);
//
//    gameOverPanel = new JPanel();
//    gameOverPanel.setLayout(new GridLayout(2, 1, 5, 4));
//    ImageIcon gameOverIcon = new ImageIcon(getClass().getClassLoader().getResource("game_over_PNG56.png"));
//    JLabel gameOverLabel = new JLabel();
//    gameOverLabel.setIcon(gameOverIcon);
//    gameOverPanel.add(gameOverLabel);
//    frame.add(gameOverPanel);
//
//
//    startBtn = new JButton("Start New Game");
//    continueBtn = new JButton("Continue Game");
//    titleBtnPanel = new JPanel();
//
//    continueBtn.addActionListener(choiceHandler);
//    continueBtn.setActionCommand("continue");
//
//    startBtn.setBackground(Color.black);
//    continueBtn.setBackground(Color.black);
//
//    gameOverPanel.add(startBtn);
//    gameOverPanel.add(continueBtn);
//    //Add btn listeners
//    startBtn.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        showGameScreenPanels();
//      }
//    });
//    continueBtn.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
////        Controller.textParser("continue");
//        showGameScreenPanels();
//      }
//    });
//  }


  //THESE METHODS WILL SHOW RESPECTIVE SCREENS AND HIDE OTHERS
  public void showGuiStart(){
    createTitleScreen();
    frame.add(titleScreenPanel, BorderLayout.CENTER);
    frame.setVisible(true);
  }

  public void showSoundSettings() {
    menuPanel.setVisible(false);
    scrollPanel.setVisible(false);
    mapPanel.setVisible(false);
    centralDisplayPanel.add(soundPanel, BorderLayout.CENTER);
    soundPanel.setVisible(true);
  }

  public void showMenu() {
    soundPanel.setVisible(false);
    scrollPanel.setVisible(false);
    mapPanel.setVisible(false);
    centralDisplayPanel.add(menuPanel, BorderLayout.CENTER);
    menuPanel.setVisible(true);
  }

  public void showMap() {
    menuPanel.setVisible(false);
    soundPanel.setVisible(false);
    scrollPanel.setVisible(false);
    centralDisplayPanel.add(mapPanel, BorderLayout.CENTER);
    mapPanel.setVisible(true);
  }

  public  void showMain() {
    menuPanel.setVisible(false);
    soundPanel.setVisible(false);
    mapPanel.setVisible(false);
    centralDisplayPanel.add(scrollPanel, BorderLayout.CENTER);
    scrollPanel.setVisible(true);
  }

  public void showGameScreenPanels(){
    //Attach panels to the outermost Main Frame
    frame.remove(titleScreenPanel);
    frame.add(statusPanel, BorderLayout.PAGE_START);
    frame.add(centralDisplayPanel, BorderLayout.CENTER);
    frame.add(controlPanel, BorderLayout.LINE_END);
    frame.setVisible(true);
  }




  //Converts sout from terminal to displayTextArea instead
  public void appendText(String text) {
    displayArea.append(text);
    displayArea.setCaretPosition((displayArea.getDocument().getLength()));
  }


  //Takes button actions as input and sends to Controller textParser()
  public void chaChaRealSmooth(JButton btn, String command,Boolean planet){
    btn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controllerField.textParser(command);
        if (!planet) {
          return;
        }
        mapPanel.setVisible(false);
        scrollPanel.setVisible(true);
      }
    });
  }

  public static void playMusic() {
    Music.playAudioMusic("Space_Chill.wav");
  }



  //  STATUS UPDATES
  public void displayPlanetStatus(String item, String damageCondition, int numberOfAstronauts){
    itemsOnPlanetLabel.setText("Items on Planet: " + (item == null ? "None" : item));
    damageConditionLabel.setText("Damage Condition: " + (damageCondition == null ? "None" : damageCondition));
    numberOfAstronautsOnPlanetLabel.setText("# Astronauts on Planet: " + numberOfAstronauts);
  }
  public void displayGameStatus(Collection<String> inventory, Planet planet, int repairsLeft, int strandedAstros ){
    inventoryLabel.setText("Inventory: " + inventory);
    currentPlanetLabel.setText("Current Planet: " + planet.getName());
    repairsLeftLabel.setText("Repairs Left: " + repairsLeft);
    strandedAstronautsLabel.setText("Stranded Astronauts: " + strandedAstros);
  }





  //  GETTERS AND SETTERS
  public  int getHealth() {
    return health;
  }
  public void setHealth(int health) {
    this.health = health;
  }
  public double getFuel() {
    return fuel;
  }
  public void setFuel(double fuel) {
    this.fuel = fuel;
  }
  public JProgressBar getFuelLevelBar() {
    return fuelLevelBar;
  }
  public JProgressBar getShipHealthBar() {
    return shipHealthBar;
  }

  public void setControllerField(Controller controllerField) {
    this.controllerField = controllerField;
  }
}


