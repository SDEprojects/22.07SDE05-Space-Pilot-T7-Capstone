package com.spacepilot.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Gui {

  private static JLabel oxygenTimeLeftLabel;
  static Timer timer;
  static int seconds;
  static int minutes;
  static String doubleDigitSeconds;
  static String doubleDigitMinutes;
  static DecimalFormat dFormat = new DecimalFormat("00");
  static JFrame frame;
  static JPanel inputPanel, controlPanel, statusPanel;
  static JTextField inputTextField;
  static JButton goBtn, menuBtn, mapBtn,repairBtn, oxygenBtn, loadBtn, unloadBtn,refuelBtn;
  static JTextArea displayArea;
  static JLabel shipHealthLabel,fuelLevelLabel, inventoryLabel, repairsLeftLabel, strandedAstronautsLabel;
  static JMenu menu;
  static JScrollPane scrollPaneDisplay;

//  public static void main(String[] args) {
//
////    new Gui();
//      Gui gui = new Gui();
//      System.setOut(new PrintStream(new RedirectingOutputStream(gui), true));
//
//  }

  public Gui(){
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
    scrollPaneDisplay = new JScrollPane(displayArea); //scrollpane to let text scroll
    displayArea.setEditable(false);//stop display from being edited
    displayArea.setWrapStyleWord(true); //wrap at word boundaries, not characters
    scrollPaneDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPaneDisplay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    displayArea.setLineWrap(true);




    //Creating Panel for holding buttons on Center-Left
    controlPanel = new JPanel();
    controlPanel.setBackground(Color.blue);
    menuBtn = new JButton("Menu");
    mapBtn = new JButton("Map");
    repairBtn = new JButton("Repair");
    oxygenBtn = new JButton("Use Oxygen");
    loadBtn = new JButton("Load");
    unloadBtn = new JButton("Unload");
    refuelBtn = new JButton("Refuel");
    controlPanel.setLayout(gridLayout); //Setting controlPanel to grid layout
    controlPanel.add(menuBtn); //Adding all buttons to control panel
    controlPanel.add(mapBtn);
    controlPanel.add(repairBtn);
    controlPanel.add(oxygenBtn);
    controlPanel.add(loadBtn);
    controlPanel.add(unloadBtn);
    controlPanel.add(refuelBtn);

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
    oxygenTimeLeftLabel = new JLabel();;
    JTextArea oxygenTimeLeftText = new JTextArea("some");
    repairsLeftLabel = new JLabel("Repairs Left:");
    JTextArea repairsLeftText = new JTextArea("2/3");
    strandedAstronautsLabel = new JLabel("Stranded Astronauts:");
    JTextArea strandedAstronautsText = new JTextArea("2");

    // countdown Timer setup
    oxygenTimeLeftLabel.setText("Oxygen Time Remaining: 03:00");
    minutes = 3;
    seconds = 0;
    ticktock();
    timer.start();

    //Adding Labels to the status panel
    statusPanel.add(currentPlanetLabel);
    statusPanel.add(oxygenTimeLeftLabel);
    statusPanel.add(shipHealthLabel);
    statusPanel.add(repairsLeftLabel);
    statusPanel.add(fuelLevelLabel);
    statusPanel.add(strandedAstronautsLabel);
    statusPanel.add(inventoryLabel);


    //Attach panels to the outermost Main Frame
    frame.add(statusPanel, BorderLayout.PAGE_START);
    frame.add(scrollPaneDisplay, BorderLayout.CENTER);
    frame.add(controlPanel, BorderLayout.LINE_END);
    frame.add(inputPanel, BorderLayout.PAGE_END);

    //Creating a menu
    menu = new JMenu("Menu");
      //Creating menu items
    JMenuItem quit = new JMenuItem("Quit");
    JMenuItem saveQuit = new JMenuItem("Save and Quit");
    JMenuItem help = new JMenuItem("Help");
    JMenuItem volume = new JMenuItem("Volume Placeholder");
      //Adding items to menu
    menu.add(quit);
    menu.add(saveQuit);
    menu.add(help);
    menu.add(volume);

    //Centers a frame onscreen when it opens
    frame.setLocationRelativeTo(null);

    //Makes frame appear onscreen. Set to true.
    frame.setVisible(true);


  }

  private static void ticktock() {
    timer = new Timer(1000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        seconds--;
        doubleDigitSeconds = dFormat.format(seconds);
        doubleDigitMinutes = dFormat.format(minutes);
        oxygenTimeLeftLabel.setText("Oxygen Time Remaining: " +  doubleDigitMinutes + ":" + doubleDigitSeconds);

        if (seconds == -1){
          seconds = 59;
          minutes --;
          doubleDigitSeconds = dFormat.format(seconds);
          doubleDigitMinutes = dFormat.format(minutes);
          oxygenTimeLeftLabel.setText("Oxygen Time Remaining: " +  doubleDigitMinutes + ":" + doubleDigitSeconds);
        }
        if(minutes == 0 && seconds ==0){
          timer.stop();
        }

      }
    });
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

}
