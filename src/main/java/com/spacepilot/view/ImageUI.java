package com.spacepilot.view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageUI {

  private JPanel containerPanel;
  private JPanel imagePanel;
  private JPanel bgPanel[] = new JPanel[10];
  private JLabel bgLabel[] = new JLabel[10];

  public ImageUI(JPanel containerPanel) {
    this.containerPanel = containerPanel;

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
  public void createObject(int bgNum, int objx, int objy, int objWidth, int objHeight, String objFileName){
    //create label
    JLabel objectLabel = new JLabel();
    objectLabel.setBounds(objx, objy, objWidth, objHeight); //sets location for astronaut

    //attach icon to it
    ImageIcon objectIcon = new ImageIcon(getClass().getClassLoader().getResource(objFileName));

    objectLabel.setIcon(objectIcon);
    //add to panel background
    bgPanel[bgNum].add(objectLabel);
    bgPanel[bgNum].add(bgLabel[bgNum]); //adds background here

  }

  public void generateScreen(){
    //Planet Screen1
    createBackgroundPanel(1, "planet-bk-blue.jpeg");
    createObject(1, 800, 300, 200, 200, "astronaut_origin_thumbnail.png");
    createObject(1, 0, 50, 499, 499, "spaceship1.png");
    createObject(1, 500, 200, 280, 320, "alien_thumbnail.png");

    //another one
//    createObject(2, "");


    addUpdateToPanel();
  }

}
