package com.spacepilot.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Gui {

  public static void main(String[] args) {

    //Different type of layouts to use on JPanels and JFrames as needed.
    BorderLayout borderLayout = new BorderLayout();
    GridBagLayout gridBagLayout = new GridBagLayout();
    GridLayout gridLayout = new GridLayout(0, 1, 5, 5); //0 rows, 1 col, 5 horizontal gap btw buttons, 5 vertical gap
    FlowLayout flowLayout = new FlowLayout();

    //Creating the outermost Main Frame
    JFrame frame = new JFrame("Main Panel"); //Create Frame for content //Default layout is BorderLayout
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Set closing event
    frame.pack(); //Sets size of frame on default

    //Creating the bottom panel for user input
    JPanel inputPanel = new JPanel(); //Creates panel
    JTextField inputTextField = new JTextField(20); //Creates input text field
    inputTextField.setSize(20, 5);
    JButton goBtn = new JButton("Go"); //Creates button
      //Adding the Components to inputPanel using Flow Layout
    inputPanel.add(goBtn);
    inputPanel.add(inputTextField);

    //Creating TextArea for displaying output strings on Center-Right
    JTextArea displayArea = new JTextArea();



    //Creating Panel for holding buttons on Center-Left
    JPanel controlPanel = new JPanel();
    JButton menuBtn = new JButton("Menu");
    JButton mapBtn = new JButton("Map");
    JButton repairBtn = new JButton("Repair");
    JButton oxygenBtn = new JButton("Use Oxygen");
    JButton loadBtn = new JButton("Load");
    JButton unloadBtn = new JButton("Unload");
    JButton interactBtn = new JButton("Interact");
    controlPanel.setLayout(gridLayout); //Setting controlPanel to grid layout
    controlPanel.add(menuBtn); //Adding all buttons to control panel
    controlPanel.add(mapBtn);
    controlPanel.add(repairBtn);
    controlPanel.add(oxygenBtn);
    controlPanel.add(loadBtn);
    controlPanel.add(unloadBtn);
    controlPanel.add(interactBtn);

    //Creating Top Panels for Status's
    JPanel statusPanel = new JPanel();
    GridLayout panelGridLayout = new GridLayout(3, 2, 2, 3 ); //Created grid layout
    statusPanel.setLayout(panelGridLayout); //Set status panel to gridLayout
    //Creating the Labels and TextAreas(for updating and displaying text)
      //Left of Panel
    JLabel currentPlanetLabel = new JLabel("Current Planet:"); //Labels can have string names and icons.
    JTextArea currentPlanetText = new JTextArea("Pluto");
    JLabel shipHealthLabel = new JLabel("Ship Health:");
    JTextArea shipHealthText = new JTextArea("100");
    JLabel passengersLabel = new JLabel("Passengers:");
    JTextArea passengersText = new JTextArea("0/50");
      //Right of Panel
    JLabel oxygenTimeLeftLabel  = new JLabel("Oxygen Time Remaining:");
    JTextArea oxygenTimeLeftText = new JTextArea("some");
    JLabel strandedAstronautsLabel = new JLabel("Stranded Astronauts:");
    JTextArea strandedAstronautsText = new JTextArea("2");
      //Adding Labels to the status panel
    statusPanel.add(currentPlanetLabel);
    statusPanel.add(oxygenTimeLeftLabel);
    statusPanel.add(shipHealthLabel);
    statusPanel.add(strandedAstronautsLabel);
    statusPanel.add(passengersLabel);


    //Attach panels to the outermost Main Frame
    frame.add(statusPanel, BorderLayout.PAGE_START);
    frame.add(displayArea, BorderLayout.CENTER);
    frame.add(controlPanel, BorderLayout.LINE_END);
    frame.add(inputPanel, BorderLayout.PAGE_END );

    //Creating a menu
    JMenu menu = new JMenu("Menu");
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

}
