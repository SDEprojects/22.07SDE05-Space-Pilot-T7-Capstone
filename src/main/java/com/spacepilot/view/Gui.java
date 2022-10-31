package com.spacepilot.view;

import com.spacepilot.controller.Controller;
import com.spacepilot.model.Music;
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
import java.util.function.Consumer;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class Gui {

  static ChoiceHandler choiceHandler = new ChoiceHandler();
  Font titleFont, normalFont;
  private JSlider slider;
  static FloatControl gainControl;
  static float currentVolume;
  static JFrame frame;
  static JPanel titleScreenPanel, titleBtnPanel, controlPanel, statusPanel, centralDisplayPanel, planetStatusPanel, menuPanel, soundPanel, mapPanel, gameOverPanel;
  static JTextField inputTextField;
  static JButton startBtn, continueBtn, menuBtn, mapBtn, mainBtn, repairBtn, helpBtn, loadBtn, unloadBtn, refuelBtn, soundSettingsBtn, videoSettingsBtn, saveGameBtn, loadSaveGameBtn, saveAndQuitGameBtn, godModeBtn, interactBtn, earthBtn, moonBtn, marsBtn, mercuryBtn, jupiterBtn, saturnBtn, venusBtn, uranusBtn, stationBtn, neptuneBtn;

  static JTextArea displayArea;
  public static JLabel titleLabel, currentPlanetLabel,damageConditionLabel,itemsOnPlanetLabel,numberOfAstronautsOnPlanetLabel,strandedAstronautsLabel,shipHealthLabel,fuelLevelLabel,inventoryLabel,repairsLeftLabel,gameOverLabel;
  static JScrollPane scrollPanel;
  static JProgressBar shipHealthBar, fuelLevelBar;
  static Consumer<String> method;
  static int health = 100;
  static double fuel = 100;
  public static final Color VERY_DARK_RED = new Color(153, 0, 0);
  public static final Color DARK_ORANGE = new Color(255, 102, 0);



  public Gui() {
    createMenuPanel();
    createMapPanel();


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

    //Creating central Panel to hold DisplayArea and PlanetStatusDisplayArea
    centralDisplayPanel = new JPanel();
    centralDisplayPanel.setLayout(borderLayout);
    centralDisplayPanel.add(scrollPanel, BorderLayout.CENTER);
    centralDisplayPanel.add(planetStatusPanel, BorderLayout.PAGE_END);

    //Centers a frame onscreen when it opens
    frame.setLocationRelativeTo(null);


    //CREATES CENTER DISPlAY FOR TEXT OUTPUTS
    displayArea = new JTextArea();
    scrollPanel = new JScrollPane(displayArea); //scrollpane to let text scroll
    displayArea.setEditable(false);//stop display from being edited
    displayArea.setWrapStyleWord(true); //wrap at word boundaries, not characters
    scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    displayArea.setLineWrap(true);


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
    godModeBtn.addActionListener(choiceHandler);
    godModeBtn.setActionCommand("god");
    loadBtn.addActionListener(choiceHandler);
    loadBtn.setActionCommand("load");
    unloadBtn.addActionListener(choiceHandler);
    unloadBtn.setActionCommand("unload");
    refuelBtn.addActionListener(choiceHandler);
    refuelBtn.setActionCommand("refuel");
    repairBtn.addActionListener(choiceHandler);
    repairBtn.setActionCommand("repair");
    helpBtn.addActionListener(choiceHandler);
    helpBtn.setActionCommand("help");

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

    //creating time thread (for oxygen display)
    Ticktock.setOxygenTimeLeftLabel(new JLabel());
    Ticktock.getOxygenTimeLeftLabel().setText("Oxygen Time Remaining: 03:00");
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



  public static void playMusic() {
    Music.playAudioMusic("Space_Chill.wav");
  }


  //Converts sout from terminal to displayTextArea instead
  public void appendText(String text) {
    displayArea.append(text);
    displayArea.setCaretPosition((displayArea.getDocument().getLength()));
  }

  //Takes button actions as input and sends to Controller textParser()
  public static class ChoiceHandler implements ActionListener {

    public void actionPerformed(ActionEvent event) {
      String command = event.getActionCommand();
      Controller.textParser(command);
    }
  }

  //method to display status of current planet user is on.
  public static void displayPlanetStatus(String item, String damageCondition, int numberOfAstronauts){
    itemsOnPlanetLabel.setText("Items on Planet: " + (item == null ? "None" : item));
    damageConditionLabel.setText("Damage Condition: " + (damageCondition == null ? "None" : damageCondition));
    numberOfAstronautsOnPlanetLabel.setText("# Astronauts on Planet: " + numberOfAstronauts);
  }


  //THESE METHODS WILL SHOW RESPECTIVE SCREENS AND HIDE OTHERS

  //Starts gui frame by setting to visible and showing title screen
  public void startGui(){
    createTitleScreen();
    frame.add(titleScreenPanel, BorderLayout.CENTER);
    frame.setVisible(true);
  }

  //Shows game screen once users moves on from titleScreen
  public void startGameScreenPanels(){
    //Attach panels to the outermost Main Frame
    frame.remove(titleScreenPanel);

    frame.add(statusPanel, BorderLayout.PAGE_START);
    frame.add(centralDisplayPanel, BorderLayout.CENTER);
    frame.add(controlPanel, BorderLayout.LINE_END);
    frame.setVisible(true);
  }
  public static void soundSettings() {
    menuPanel.setVisible(false);
    scrollPanel.setVisible(false);
    mapPanel.setVisible(false);
    centralDisplayPanel.add(soundPanel, BorderLayout.CENTER);
    soundPanel.setVisible(true);
  }

  public static void goToPlanet(String planet) {
    Controller.textParser(planet);
    Controller.displayGameState();
    mapPanel.setVisible(false);
    scrollPanel.setVisible(true);
  }

  public static void planetButtons(JButton btn, String planet) {
    btn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        goToPlanet(planet);
      }
    });
  }

  public static void soundButtons(JButton btn, Consumer<String> musicMethod, String wavFile) {
    btn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        musicMethod.accept(wavFile);
      }
    });
  }

  public static void showMenu() {
    soundPanel.setVisible(false);
    scrollPanel.setVisible(false);
    mapPanel.setVisible(false);
    centralDisplayPanel.add(menuPanel, BorderLayout.CENTER);
    menuPanel.setVisible(true);
  }

  public static void showMap() {
    menuPanel.setVisible(false);
    soundPanel.setVisible(false);
    scrollPanel.setVisible(false);
    centralDisplayPanel.add(mapPanel, BorderLayout.CENTER);
    mapPanel.setVisible(true);
  }

  public static void showMain() {
    menuPanel.setVisible(false);
    soundPanel.setVisible(false);
    mapPanel.setVisible(false);
    centralDisplayPanel.add(scrollPanel, BorderLayout.CENTER);
    scrollPanel.setVisible(true);
  }
  public void createGameOverScreen(){
    planetStatusPanel.setVisible(false);
    centralDisplayPanel.setVisible(false);
    statusPanel.setVisible(false);
    controlPanel.setVisible(false);

    gameOverPanel = new JPanel();
    gameOverPanel.setLayout(new GridLayout(2, 1, 5, 4));
    ImageIcon gameOverIcon = new ImageIcon(getClass().getClassLoader().getResource("game_over_PNG56.png"));
    gameOverLabel = new JLabel();
    gameOverLabel.setIcon(gameOverIcon);
    gameOverPanel.add(gameOverLabel);
    frame.add(gameOverPanel);


    startBtn = new JButton("Start New Game");
    continueBtn = new JButton("Continue Game");
    titleBtnPanel = new JPanel();

    continueBtn.addActionListener(choiceHandler);
    continueBtn.setActionCommand("continue");

    startBtn.setBackground(Color.black);
    continueBtn.setBackground(Color.black);

    gameOverPanel.add(startBtn);
    gameOverPanel.add(continueBtn);
    //Add btn listeners
    startBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        startGameScreenPanels();
      }
    });
    continueBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Controller.textParser("continue");
        startGameScreenPanels();
      }
    });
  }

  public void createTitleScreen(){
    //Create components

    titleScreenPanel= new JPanel();
    titleLabel  = new JLabel("Space Pilot", SwingConstants.CENTER); //centers label
    titleFont = new Font("Times New Roman", Font.PLAIN, 90);
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
        startGameScreenPanels();
      }
    });
    continueBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Controller.textParser("continue");
        startGameScreenPanels();
      }
    });

  }
  public static void createMenuPanel() {

    menuPanel = new JPanel(); //Create Panel for content

    //Creating a menu
    menuPanel.setBackground(Color.black);
    //Creating menu buttons
    soundSettingsBtn = new JButton("Sound Settings");
    soundSettingsBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        soundSettings();
      }
    });
    videoSettingsBtn = new JButton("Video Settings");
    saveGameBtn = new JButton("Save");
    loadSaveGameBtn = new JButton("Load Saved Game");
    saveAndQuitGameBtn = new JButton("Quit Game");
    menuPanel.add(soundSettingsBtn); //Adding all buttons to menu frame
    menuPanel.add(videoSettingsBtn);
    menuPanel.add(saveGameBtn);
    menuPanel.add(loadSaveGameBtn);
    menuPanel.add(saveAndQuitGameBtn);
  }

  public static void createMapPanel() {

    mapPanel = new JPanel(); //Create Panel for content
    //Creating a menu
    mapPanel.setBackground(Color.black);
    //Creating maps buttons to go to respective planets below
    earthBtn = new JButton("Earth");
    planetButtons(earthBtn, "go earth");
    moonBtn = new JButton("Moon");
    planetButtons(moonBtn, "go moon");
    marsBtn = new JButton("Mars");
    planetButtons(marsBtn, "go mars");
    mercuryBtn = new JButton("Mercury");
    planetButtons(mercuryBtn, "go mercury");
    saturnBtn = new JButton("Saturn");
    planetButtons(saturnBtn, "go saturn");
    venusBtn = new JButton("Venus");
    planetButtons(venusBtn, "go venus");
    neptuneBtn = new JButton("Neptune");
    planetButtons(neptuneBtn, "go neptune");
    jupiterBtn = new JButton("Jupiter");
    planetButtons(jupiterBtn, "go jupiter");
    stationBtn = new JButton("Station");
    planetButtons(stationBtn, "go station");
    uranusBtn = new JButton("Uranus");
    planetButtons(uranusBtn, "go uranus");
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




  //  GETTERS AND SETTERS
  public static int getHealth() {
    return health;
  }

  public static void setHealth(int health) {
    Gui.health = health;
  }

  public static double getFuel() {
    return fuel;
  }

  public static void setFuel(double fuel) {
    Gui.fuel = fuel;
  }

  public static JProgressBar getFuelLevelBar() {
    return fuelLevelBar;
  }

  public static void setFuelLevelBar(JProgressBar fuelLevelBar) {
    Gui.fuelLevelBar = fuelLevelBar;
  }

  public static JProgressBar getShipHealthBar() {
    return shipHealthBar;
  }

  public static void setShipHealthBar(JProgressBar shipHealthBar) {
    Gui.shipHealthBar = shipHealthBar;
  }
}


