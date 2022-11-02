package com.spacepilot.view;

import com.spacepilot.view.Gui.ChoiceHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageUI {

  private JPanel containerPanel;
  private JPanel imagePanel;
  private JPanel bgPanel[] = new JPanel[10];
  private JLabel bgLabel[] = new JLabel[10];
  private ChoiceHandler choiceH;

  public ImageUI(JPanel containerPanel, ChoiceHandler choiceHandler) {
    this.containerPanel = containerPanel;
    this.choiceH = choiceHandler;

//    createBackgroundPanel();
//    createObject();
//    addUpdateToPanel();
    createTopLevelPanel();
    generateScreen();

  }
  public ImageUI(){

  }

  public JPanel getImagePanel() {
    return imagePanel;
  }

  public void addUpdateToPanel(){
    containerPanel.add(imagePanel, BorderLayout.CENTER);
  }

  public void createTopLevelPanel(){
    imagePanel = new JPanel();
    imagePanel.setBackground(Color.black);
    imagePanel.setLayout(new BorderLayout());
  }

  //Method to create background
  public void createBackgroundPanel(int bgNum, String bgFileName){

    //creating background panels
    bgPanel[bgNum] = new JPanel();
    bgPanel[bgNum].setBackground(Color.red);
    bgPanel[bgNum].setBounds(0, 0, 1024, 640); //since layout null, setBounds
    bgPanel[bgNum].setLayout(null);
    imagePanel.add(bgPanel[bgNum]);

    //Set label to icon. Sizing should match panel its within
    ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource(bgFileName));
    bgLabel[bgNum] = new JLabel();
    bgLabel[bgNum].setBounds(0,0, 1024, 640);
    bgLabel[bgNum].setIcon(bgIcon);

    //Add all to center display
//    centralDisplayPanel.add(imagePanel, BorderLayout.CENTER);
//    containerPanel.add(imagePanel, BorderLayout.CENTER);

//    return imagePanel;
  }

  //Creates object image within background
  public void createObject(int bgNum, int objx, int objy, int objWidth, int objHeight, String objFileName, String command){
    //create label
    JButton objectLabel = new JButton();
    objectLabel.setBounds(objx, objy, objWidth, objHeight); //sets location for astronaut

    //Button formatting
    objectLabel.setContentAreaFilled(false);//clears white fill
    objectLabel.setBorderPainted(false);//clears border
    objectLabel.setFocusPainted(false);//clears focus border from click

    //Set click actions
    objectLabel.addActionListener(choiceH);
    objectLabel.setActionCommand(command);

    //attach icon to it
    ImageIcon objectIcon = new ImageIcon(getClass().getClassLoader().getResource(objFileName));

    objectLabel.setIcon(objectIcon);
    //add to panel background
    bgPanel[bgNum].add(objectLabel);
    bgPanel[bgNum].add(bgLabel[bgNum]); //adds background here

  }

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
    objectLabel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Gui.showMap();
      }
    });

//    objectLabel.setActionCommand(command);

    //attach icon to it
    ImageIcon objectIcon = new ImageIcon(getClass().getClassLoader().getResource(objFileName));

    objectLabel.setIcon(objectIcon);
    //add to panel background
    bgPanel[bgNum].add(objectLabel);
    bgPanel[bgNum].add(bgLabel[bgNum]); //adds background here

  }

  public void generateScreen(){
    //Earth Scene 1
    createBackgroundPanel(1, "earth.jpeg");
    createMapObject(1, 0, 50, 499, 499, "spaceship-499x499.png");
    createObject(1, 400, 350, 647, 385, "building2.png", "load");

    //Planet Scene 2
    createBackgroundPanel(2, "planetBlue-1024x640.jpeg");
    createObject(2, 800, 400, 200, 200, "astronaut_origin_thumbnail.png", "load");
    createMapObject(2, 0, 50, 499, 499, "spaceship-499x499.png");
    createObject(2, 500, 200, 280, 320, "alien_thumbnail.png", "load");
//    bgPanel[1].add(bgLabel[1]);

    //Station Scene 3
    createBackgroundPanel(3, "station.jpg");
    createMapObject(3, 0, 50, 499, 499, "spaceship-499x499.png");
    createObject(3, 720, 300, 233, 320, "gas1.png", "refuel");


    addUpdateToPanel();
  }

  public void showPlanetScreen1(){
    bgPanel[1].setVisible(false);
    bgPanel[2].setVisible(true);
    bgPanel[3].setVisible(false);
  }

  public void showEarthScreen2(){
    bgPanel[1].setVisible(true);
    bgPanel[2].setVisible(false);
    bgPanel[3].setVisible(false);
  }

  public void showStationScreen3(){
    bgPanel[1].setVisible(false);
    bgPanel[2].setVisible(false);
    bgPanel[3].setVisible(true);
  }

}
