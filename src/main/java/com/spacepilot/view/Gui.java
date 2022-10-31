package com.spacepilot.view;

import static com.spacepilot.controller.Controller.game;
import static com.spacepilot.controller.Controller.loadSavedGame;

import com.spacepilot.controller.Controller;
import com.spacepilot.model.Music;
import com.spacepilot.model.Ticktock;
import com.spacepilot.model.Planet;
import com.spacepilot.model.Planet;
import com.spacepilot.model.Ticktock;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;
import javax.sound.sampled.FloatControl;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Gui {

  static ChoiceHandler choiceHandler = new ChoiceHandler();
  static JSlider slider;
  static FloatControl gainControl;
  static float currentVolume;
  static JFrame frame;
  static JPanel inputPanel, controlPanel, statusPanel, centralDisplayPanel, planetStatusPanel, menuPanel, soundPanel, mapPanel;
  static JTextField inputTextField;
  static JButton goBtn, menuBtn, mapBtn, mainBtn, repairBtn, oxygenBtn, loadBtn, unloadBtn, refuelBtn, soundSettingsBtn, videoSettingsBtn, saveGameBtn, loadSaveGameBtn, saveAndQuitGameBtn, godModeBtn, interactBtn, earthBtn, moonBtn, marsBtn, mercuryBtn, jupiterBtn, saturnBtn, venusBtn, uranusBtn, stationBtn, neptuneBtn;

  static JTextArea displayArea;
  static JLabel shipHealthLabel, fuelLevelLabel, inventoryLabel, repairsLeftLabel, strandedAstronautsLabel, numberOfAstronautsOnPlanetLabel, itemsOnPlanetLabel,
      damageConditionLabel;
  static JScrollPane scrollPanel;

  Consumer<String> method;

//  public static void main(String[] args) {
//
////    new Gui();
//      Gui gui = new Gui();
//      System.setOut(new PrintStream(new RedirectingOutputStream(gui), true));
//
//  }

  public Gui() {
    createMenuPanel();
    createMapPanel();

    //Different type of layouts to use on JPanels and JFrames as needed.
    BorderLayout borderLayout = new BorderLayout();
    GridBagLayout gridBagLayout = new GridBagLayout();
    GridLayout gridLayout = new GridLayout(0, 1, 5,
        5); //0 rows, 1 col, 5 horizontal gap btw buttons, 5 vertical gap
    FlowLayout flowLayout = new FlowLayout();

    //Creating the outermost Main Frame
    frame = new JFrame(
        "Main Panel"); //Create Frame for content //Default layout is BorderLayout
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Set closing event
//    frame.pack(); //Sets size of frame on default
    frame.setSize(new Dimension(800, 500));

    //Creating the bottom panel for user input
    inputPanel = new JPanel(); //Creates panel
    inputTextField = new JTextField(20); //Creates input text field
    inputTextField.setSize(20, 5);
    inputTextField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getFieldText(inputTextField.getText());
      }
    });
    goBtn = new JButton("Go"); //Creates button
    //Adding the Components to inputPanel using Flow Layout
    inputPanel.add(goBtn);
    inputPanel.add(inputTextField);

    //Creating TextArea for displaying output strings on Center-Right
    displayArea = new JTextArea();
    scrollPanel = new JScrollPane(displayArea); //scrollpane to let text scroll
    displayArea.setEditable(false);//stop display from being edited
    displayArea.setWrapStyleWord(true); //wrap at word boundaries, not characters
    scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    displayArea.setLineWrap(true);

    //Creating Panel for holding buttons on Center-Left
    controlPanel = new JPanel();
    controlPanel.setBackground(Color.blue);
    menuBtn = new JButton("Menu");
    //    Event Listener for menu button to open new window w/menu options
    menuBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMenu();
      }
    });
    mapBtn = new JButton("Map");
    mapBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMap();
      }
    });
    mainBtn = new JButton("Main Screen");
    mainBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMain();
      }
    });
    repairBtn = new JButton("Repair");
    oxygenBtn = new JButton("Use Oxygen");
    loadBtn = new JButton("Load");
    unloadBtn = new JButton("Unload");
    refuelBtn = new JButton("Refuel");
    interactBtn = new JButton("Interact");
    godModeBtn = new JButton("GOD MODE");
    controlPanel.setLayout(gridLayout); //Setting controlPanel to grid layout
    controlPanel.add(menuBtn); //Adding all buttons to control panel
    controlPanel.add(mapBtn);
    controlPanel.add(mainBtn);
    controlPanel.add(repairBtn);
    controlPanel.add(oxygenBtn);
    controlPanel.add(loadBtn);
    controlPanel.add(unloadBtn);
    controlPanel.add(refuelBtn);
    controlPanel.add(godModeBtn);
    controlPanel.add(interactBtn);

    //Creating Top Panels for Status's
    statusPanel = new JPanel();
    GridLayout panelGridLayout = new GridLayout(3, 2, 2, 3); //Created grid layout
    statusPanel.setLayout(panelGridLayout); //Set status panel to gridLayout
    //Creating the Labels and TextAreas(for updating and displaying text)
    //Left of Panel
    JLabel currentPlanetLabel = new JLabel(
        "Current Planet:"); //Labels can have string names and icons.
    JTextArea currentPlanetText = new JTextArea("Pluto");
    shipHealthLabel = new JLabel("Ship Health:");
    JTextArea shipHealthText = new JTextArea("100");
    fuelLevelLabel = new JLabel("Fuel Level:");
    JTextArea fuelLevelText = new JTextArea("100");
    inventoryLabel = new JLabel("Inventory:");
    JTextArea inventoryText = new JTextArea("[alien baby]");
    //Right of Panel
    Ticktock.setOxygenTimeLeftLabel(new JLabel());
    JTextArea oxygenTimeLeftText = new JTextArea("some");
    repairsLeftLabel = new JLabel("Repairs Left:");
    JTextArea repairsLeftText = new JTextArea("2/3");
    strandedAstronautsLabel = new JLabel("Stranded Astronauts:");
    JTextArea strandedAstronautsText = new JTextArea("2");

    // countdown Timer setup
    Ticktock.getOxygenTimeLeftLabel().setText("Oxygen Time Remaining: 03:00");
    Ticktock.setMinutes(3);
    Ticktock.setSeconds(0);
    Ticktock.ticktock();
    Ticktock.getTimer().start();

//Music Panel added below with individual buttons that invoke audio actions
    soundPanel = new JPanel();
    //button plays and pauses current track
    JButton playPauseBtn = new JButton("Play/Pause");
    method = i -> Music.musicMute();
    soundButtons(playPauseBtn, method, null);
    soundPanel.add(playPauseBtn);
    //button mutes and unMutes FX
    JButton muteBtn = new JButton("Mute FX");
    method = i -> Music.fxMute();
    soundButtons(muteBtn, method, null);
    soundPanel.add(muteBtn);
    //button plays track 1 as background music
    JButton track1B = new JButton("Track 1");
    method = wavFile -> Music.track1(wavFile);
    soundButtons(track1B, method, "Space_Chill.wav");
    soundPanel.add(track1B);
    //button plays track 2 as background music
    JButton track2B = new JButton("Track 2");
    method = wavFile -> Music.track2(wavFile);
    soundButtons(track2B, method, "Space_Ambient.wav");
    soundPanel.add(track2B);
    //button plays track 3 as background music
    JButton track3B = new JButton("Track 3");
    method = wavFile -> Music.track3(wavFile);
    soundButtons(track3B, method, "Space_Cinematic.wav");
    soundPanel.add(track3B);
    //button plays track 4 as background music
    JButton track4B = new JButton("Track 4");
    method = wavFile -> Music.track4(wavFile);
    soundButtons(track4B, method, "Space_Cyber.wav");
    soundPanel.add(track4B);
//slider is implemented to adjust volume up and down for current background music
    slider = new JSlider(-40, 6);
    soundPanel.add(slider);
    slider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {

        currentVolume = Music.currentVolume();
        currentVolume = slider.getValue();
        gainControl = Music.gainControl();
        gainControl.setValue(currentVolume);

      }
    });
    soundPanel.setVisible(true);
    playMusic();

//God Mode button inputs "god" into controller using choice handler
    godModeBtn.addActionListener(choiceHandler);
    godModeBtn.setActionCommand("god");
//Load button inputs "load" into controller
    loadBtn.addActionListener(choiceHandler);
    loadBtn.setActionCommand("load");

    refuelBtn.addActionListener(choiceHandler);
    refuelBtn.setActionCommand("refuel");

    //Adding Labels to the status panel
    statusPanel.add(currentPlanetLabel);
    statusPanel.add(Ticktock.getOxygenTimeLeftLabel());
    statusPanel.add(shipHealthLabel);
    statusPanel.add(repairsLeftLabel);
    statusPanel.add(fuelLevelLabel);
    statusPanel.add(strandedAstronautsLabel);
    statusPanel.add(inventoryLabel);

    //Creating planetStatusPanel and Labels
    planetStatusPanel = new JPanel();
//    planetStatusPanel.setLayout(gridLayout);//Layout to stack the labels vertically. Can be removed.
    numberOfAstronautsOnPlanetLabel = new JLabel("# of Astronauts on Planet: ");
    itemsOnPlanetLabel = new JLabel("Items on Planet:");
    damageConditionLabel = new JLabel("Danger Condition: ");
    planetStatusPanel.add(numberOfAstronautsOnPlanetLabel);
    planetStatusPanel.add(itemsOnPlanetLabel);
    planetStatusPanel.add(damageConditionLabel);

    //Creating central Panel to hold DisplayArea and PlanetStatusDisplayArea
    centralDisplayPanel = new JPanel();
    centralDisplayPanel.setLayout(borderLayout);
    centralDisplayPanel.add(scrollPanel, BorderLayout.CENTER);
    centralDisplayPanel.add(planetStatusPanel, BorderLayout.PAGE_END);

    //Attach panels to the outermost Main Frame
    frame.add(statusPanel, BorderLayout.PAGE_START);
    frame.add(centralDisplayPanel, BorderLayout.CENTER);
    frame.add(controlPanel, BorderLayout.LINE_END);
//    frame.add(soundPanel, BorderLayout.PAGE_END);
//    frame.add(menuPanel, BorderLayout.CENTER);

    //Centers a frame onscreen when it opens
    frame.setLocationRelativeTo(null);

  }

  //Starts gui frame by setting to visible
  public void startGui() {
    frame.setVisible(true);

  }


  public static void playMusic() {
    Music.playAudioMusic("Space_Chill.wav");
  }

  //Helps convert sout to displayTextArea
  public void appendText(String text) {
    displayArea.append(text);
    displayArea.setCaretPosition((displayArea.getDocument().getLength()));
//    displayArea.update(displayArea.getGraphics());
  }

  public static String getFieldText(String input) {
    return input;
  }
  //Helps convert sout to displayTextArea
  public void appendText(String text){
    displayArea.append(text);
    displayArea.setCaretPosition((displayArea.getDocument().getLength()));
//    displayArea.update(displayArea.getGraphics());
  }

  public static String getFieldText(String input){
    return input;
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

  //  these methods will show respective screens and hide the others in primary display area
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

  public static void soundSettings() {
    menuPanel.setVisible(false);
    scrollPanel.setVisible(false);
    mapPanel.setVisible(false);
    centralDisplayPanel.add(soundPanel, BorderLayout.CENTER);
    soundPanel.setVisible(true);
  }

  public static class ChoiceHandler implements ActionListener {

    public void actionPerformed(ActionEvent event) {
      String command = event.getActionCommand();
      Controller.textParser(command);
    }
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

}


