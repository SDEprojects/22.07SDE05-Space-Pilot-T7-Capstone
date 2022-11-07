package com.spacepilot.view;

import com.spacepilot.controller.Controller;
import com.spacepilot.model.Music;
import com.spacepilot.model.Planet;
import com.spacepilot.model.Ticktock;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import javax.sound.sampled.FloatControl;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class Gui {

  public static final Color VERY_DARK_RED = new Color(153, 0, 0);
  public static final Color DARK_ORANGE = new Color(255, 102, 0);
  public static final Color PURPLE = new Color(133, 13, 191);
  private static JProgressBar shipHealthBar;
  public ImageIcon planetIcon, btnIcon;
  private JFrame frame;
  private JTextArea displayArea;
  private JScrollPane scrollPanel;
  private JProgressBar fuelLevelBar;
  private FloatControl gainControl;
  private JSlider slider;
  private Consumer<String> method;
  private Controller controllerField;
  private JPanel titleScreenPanel, titleBtnPanel, controlPanel, statusPanel, centralDisplayPanel, inventoryPanel, rightSidePanel,
      planetStatusPanel, menuPanel, soundPanel, mapPanel, helpScreenPanel, helpBtnPanel, backgroundScreenPanel;
  private JLabel titleLabel, currentPlanetLabel, damageConditionLabel, itemsOnPlanetLabel,
      numberOfAstronautsOnPlanetLabel, strandedAstronautsLabel, inventoryLabel, repairsLeftLabel, warningLabel, helpLabel;
  private JButton continueBtn, startBtn, sunBtn, stationBtn, mapBtn, menuBtn, repairBtn, helpBtn, loadBtn, unloadBtn, refuelBtn, interactBtn, godModeBtn, mainBtn, dotBtn;
  private Boolean warningBoolean = true;

  private float currentVolume;
  private Font normalFont;
  private Ticktock ticktock;
  private ImageUI imageUI;
  private BorderLayout borderLayout = new BorderLayout();
  private GridBagLayout gridBagLayout = new GridBagLayout();
  private GridLayout gridLayout = new GridLayout(0, 1, 5,
      5); //0 rows, 1 col, 5 horizontal gap btw buttons, 5 vertical gap
  private FlowLayout flowLayout = new FlowLayout();
  private JPanel gameOverLosePanel = new JPanel();
  private JPanel bottomPanel;
  private JPanel gameOverWinPanel = new JPanel();

  //CONSUMER TIPS
//  private Consumer <String> movePlanetsListenerConsumer;
//
//  public void setMovePlanetsListenerConsumer(
//      Consumer<String> movePlanetsListenerConsumer) {
//    this.movePlanetsListenerConsumer = movePlanetsListenerConsumer;
//  }

  public Gui() {

    //CREATES OUTERMOST MAIN FRAME
    frame = new JFrame("Main Panel"); //Create Frame for content //Default layout is BorderLayout
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Set closing event
    frame.setSize(new Dimension(1140, 900));
    frame.setResizable(false);

    //Centers a frame onscreen when it opens
    frame.setLocationRelativeTo(null);
  }

  public static void playMusic() {
    Music.playAudioMusic("sounds/Space_Chill.wav");
  }

  public void createSectionsOfGui() {
    //These methods are used to create sections of the gui

    createCenterDisplayArea();
    createMenuPanel();
    createMapPanel();
    createTopOfScreenStatusPanel();
    createMusicPanelInMenuScreen();
    createBottomPlanetStatusPanel();
    createCentralDisplayPanel(); //creating central Panel to hold DisplayArea and PlanetStatusDisplayArea and ScrollPanel
    passCommandMethodsToImageGui(); //method that instantiates gui and passes /and or sets runnables here.
    createRightSideControlPanel(gridLayout);
    createBackgroundImagesForGui(); //creates background images using passed in runnables
    createGameOverLoseScreen();
    createGameOverWinScreen();
  }

  public void createCentralDisplayPanel() {
    //Creating central Panel to hold DisplayArea and PlanetStatusDisplayArea and ScrollPanel
    centralDisplayPanel = new JPanel();
    centralDisplayPanel.setLayout(borderLayout);
    centralDisplayPanel.add(planetStatusPanel, BorderLayout.PAGE_START);
    centralDisplayPanel.add(scrollPanel, BorderLayout.PAGE_END);

    warningLabel = new JLabel("");

    ImageIcon warningIcon = new ImageIcon(
        getClass().getClassLoader().getResource("images/Warning.png"));
    Image img = warningIcon.getImage();
    Image newImg = img.getScaledInstance(75, 75, Image.SCALE_DEFAULT);
    warningIcon = new ImageIcon(newImg);
    warningLabel.setBorder(BorderFactory.createLineBorder(Color.black));
    warningLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
    warningLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    warningLabel.setBorder(BorderFactory.createLineBorder(null, 0));
    warningLabel.setIcon(warningIcon);
    warningLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
    warningLabel.setBounds(45, -15, 1000, 200);
    centralDisplayPanel.add(warningLabel);
    warningLabel.setVisible(false);

    //Call method that instantiates gui and passes /and or sets runnables next, then createBackground
    // images for Gui method.
  }

  public void passCommandMethodsToImageGui() {
    //Creates an instance of imageUI while passing in runnables carrying the command methods from Gui to ImageUI
    //Effectively connects the class so this method can be passed and used in imageUI
    imageUI = new ImageUI(centralDisplayPanel,
        new Runnable() {
          @Override
          public void run() {
            controllerField.textParser("load");
            //Updates background panel to remove astronuats
            imageUI.createPlanetScreen();
            //Refreshes panel to show updated images
            centralDisplayPanel.setVisible(false);
            centralDisplayPanel.setVisible(true);
          }
        },
        new Runnable() {
          @Override
          public void run() {
            controllerField.textParser("unload");
          }
        },
        new Runnable() {
          @Override
          public void run() {
            controllerField.textParser("refuel");
            imageUI.updateRefuelsOnStation();
          }
        },
        new Runnable() {
          @Override
          public void run() {
            controllerField.textParser("interact");
          }
        },
        new Runnable() {
          @Override
          public void run() {
            showMap();
            controllerField.textParser("go orbit");
            //updates fuel after moving
            controllerField.updateFuel();
            //update text for orbit
            currentPlanetLabel.setText(
                "<html>&emsp Current Location: <font color=#990000>Orbit</font></html>");
            controllerField.checkGameResult();
          }
        });

  }

  //Instantiates imageGUI and constructs background images for centralDisplayPanel Center
  public void createBackgroundImagesForGui() {
    //Adds the background panel to centerDisplayPanel
    imageUI.createTopLevelPanel();
    //Adds background images, scenes, and buttons to background panel
    imageUI.generateScene();
  }

  // THESE METHODS CREATE DIFFERENT SECTIONS OF THE GUI
  private void createBottomPlanetStatusPanel() {
    //CREATES BOTTOM PANEL: (Display Current Location Status)
    planetStatusPanel = new JPanel();
    planetStatusPanel.setBackground(Color.black);

    itemsOnPlanetLabel = new JLabel("<html>&emsp Items on Planet: </html>");
    numberOfAstronautsOnPlanetLabel = new JLabel("<html>&emsp # of Astronauts on Planet: </html>");
    damageConditionLabel = new JLabel("<html>&emsp Threat: </html>");
    setStatusPanelFont(itemsOnPlanetLabel);
    setStatusPanelFont(numberOfAstronautsOnPlanetLabel);
    setStatusPanelFont(damageConditionLabel);

    planetStatusPanel.setLayout(new GridLayout(1, 3, 3, 3)); //Layout to spread labels out
    planetStatusPanel.add(numberOfAstronautsOnPlanetLabel);
    planetStatusPanel.add(itemsOnPlanetLabel);
    planetStatusPanel.add(damageConditionLabel);
  }

  private void createCenterDisplayArea() {
    //CREATES CENTER DISPlAY FOR TEXT OUTPUTS
    displayArea = new JTextArea();
    scrollPanel = new JScrollPane(displayArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //scrollpane to let text scroll
    displayArea.setEditable(false);//stop display from being edited
    displayArea.setWrapStyleWord(true); //wrap at word boundaries, not characters
    displayArea.setLineWrap(true);
    displayArea.setBackground(Color.black);
    displayArea.setForeground(Color.lightGray);
    displayArea.setRows(5); //Adjusts size of display area
    displayArea.setFont(new Font("Times New Roman", Font.BOLD, 30));
  }

  private void createTopOfScreenStatusPanel() {
    ticktock = new Ticktock();

    //CREATES TOP PANEL: (Status of Spaceship)
    statusPanel = new JPanel();
    statusPanel.setBackground(Color.black);
    statusPanel.setBounds(100, 15, 200, 30);
    GridLayout panelGridLayout = new GridLayout(2, 2, 2, 3); //Created grid layout
    statusPanel.setLayout(panelGridLayout); //Set status panel to gridLayout

    //Creating the Labels (for updating and displaying text)
    currentPlanetLabel = new JLabel(
        "<html>&emsp Current Location:</html>"); //Labels can have string names and icons
    repairsLeftLabel = new JLabel("<html>&emsp Repairs Left:</html>");
    strandedAstronautsLabel = new JLabel("Stranded Astronauts:");
//    inventoryLabel = new JLabel("Inventory:");
    ticktock.setOxygenTimeLeftLabel(
        new JLabel("<html>Oxygen Remaining: <font color=#990000>08:00</font></html>"));

    setStatusPanelFont(currentPlanetLabel);
    setStatusPanelFont(repairsLeftLabel);
    setStatusPanelFont(strandedAstronautsLabel);
    setStatusPanelFont(ticktock.getOxygenTimeLeftLabel());

    //creating time thread (for oxygen display)
    ticktock.setMinutes(8);
    ticktock.setSeconds(0);
    ticktock.ticktock();
//    ticktock.getTimer().start();

    //creating ship health bar
    shipHealthBar = new JProgressBar(0, 100);
    shipHealthBar.setForeground(VERY_DARK_RED);
    shipHealthBar.setBackground(Color.black);
    shipHealthBar.setBorder(BorderFactory.createLineBorder(VERY_DARK_RED, 2));
    shipHealthBar.getUI();
    shipHealthBar.setUI(new BasicProgressBarUI() {
      protected Color getSelectionBackground() {
        return VERY_DARK_RED;
      }

      protected Color getSelectionForeground() {
        return Color.black;
      }
    });
    shipHealthBar.setValue(100);
    shipHealthBar.setString("Health: " + 100 + "%");
    shipHealthBar.setFont(new Font("Times New Roman", Font.BOLD, 20));
    shipHealthBar.setStringPainted(true);

    //creating ship fuel level bar
    fuelLevelBar = new JProgressBar(0, 100);
    fuelLevelBar.setForeground(DARK_ORANGE);
    fuelLevelBar.setBorder(BorderFactory.createLineBorder(DARK_ORANGE, 2));
    fuelLevelBar.setBackground(Color.black);
    fuelLevelBar.getUI();
    fuelLevelBar.setUI(new BasicProgressBarUI() {
      protected Color getSelectionBackground() {
        return DARK_ORANGE;
      }

      protected Color getSelectionForeground() {
        return Color.black;
      }
    });
    fuelLevelBar.setValue(100);
    fuelLevelBar.setString("Fuel: " + 100 + "%");
    fuelLevelBar.setFont(new Font("Times New Roman", Font.BOLD, 20));
    fuelLevelBar.setStringPainted(true);
    JTextArea fuelLevelText = new JTextArea("100");

    //Adding Labels to the status panel
    statusPanel.add(currentPlanetLabel);
    statusPanel.add(ticktock.getOxygenTimeLeftLabel());
    statusPanel.add(shipHealthBar);
    statusPanel.add(repairsLeftLabel);
    statusPanel.add(strandedAstronautsLabel);
    statusPanel.add(fuelLevelBar);
//    statusPanel.add(inventoryLabel);
  }

  private void createMusicPanelInMenuScreen() {
    //CREATES MUSIC PANEL
    soundPanel = new JPanel();

    soundPanel.setLayout(null);
    //Creating a menu
//    soundPanel.setBackground(Color.black);

    JLabel backgroundLabel = new JLabel("");
    backgroundLabel.setIcon(
        new ImageIcon(getClass().getClassLoader().getResource("images/Space.jpg")));
    backgroundLabel.setBounds(0, 0, 1140, 900);

    //creates buttons for music panel
    JButton muteBtn = new JButton();
    JButton playBtn = new JButton();
    JButton pauseBtn = new JButton();
    JButton track1B = new JButton();
    JButton track2B = new JButton();
    JButton track3B = new JButton();
    JButton track4B = new JButton();
    JPanel sliderPanel = new JPanel();
    JButton exitMenuBtn = new JButton();
    slider = new JSlider(-40, 6);

    //button plays current track
    method = i -> Music.musicPlay();
    soundButtons(playBtn, method, null);
    planetIcons(playBtn, "images/Play.png", 50, 75, 400, 75, 400, 75);
    //button pauses current track
    method = i -> Music.musicPause();
    soundButtons(pauseBtn, method, null);
    planetIcons(pauseBtn, "images/Pause.png", 550, 75, 400, 75, 400, 75);
    //button mutes and unMutes FX
    method = i -> Music.fxMute();
    soundButtons(muteBtn, method, null);
    planetIcons(muteBtn, "images/FxMute.png", 50, 175, 400, 75, 400, 75);
    //button plays track 1 as background music
    method = wavFile -> Music.track1(wavFile);
    soundButtons(track1B, method, "sounds/Space_Chill.wav");
    planetIcons(track1B, "images/Track1.png", 50, 350, 400, 75, 400, 75);
    //button plays track 2 as background music
    method = wavFile -> Music.track2(wavFile);
    soundButtons(track2B, method, "sounds/Space_Ambient.wav");
    planetIcons(track2B, "images/Track2.png", 550, 350, 400, 75, 400, 75);
    //button plays track 3 as background music
    method = wavFile -> Music.track3(wavFile);
    soundButtons(track3B, method, "sounds/Space_Cinematic.wav");
    planetIcons(track3B, "images/Track3.png", 50, 450, 400, 75, 400, 75);
    //button plays track 4 as background music
    method = wavFile -> Music.track4(wavFile);
    soundButtons(track4B, method, "sounds/Space_Cyber.wav");
    planetIcons(track4B, "images/Track4.png", 550, 450, 400, 75, 400, 75);
//exits back to main screen
    planetIcons(exitMenuBtn, "images/Return.png", 195, 600, 600, 150, 600, 150);
    exitMenuBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMain();
      }
    });
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
    sliderPanel.setBounds(550, 200, 400, 25);
    sliderPanel.setBackground(Color.black);
    slider.setBackground(PURPLE);
    slider.setPreferredSize(new Dimension(400, 25));
    playMusic();

    //adds buttons to music panel
    soundPanel.add(playBtn);
    soundPanel.add(pauseBtn);
    soundPanel.add(muteBtn);
    soundPanel.add(track1B);
    soundPanel.add(track2B);
    soundPanel.add(track3B);
    soundPanel.add(track4B);
    soundPanel.add(sliderPanel);
    soundPanel.add(exitMenuBtn);
    sliderPanel.add(slider);
    soundPanel.add(backgroundLabel);
  }

  private void createRightSideControlPanel(GridLayout gridLayout) {
    //CREATING PANEL ON THE RIGHT TO HOLD BUTTONS AND INVENTORY
    rightSidePanel = new JPanel(gridLayout); //holds controlPanel and inventoryPanel
    controlPanel = new JPanel(new GridLayout(3, 1, 0, 0)); //holds gui buttons
    inventoryPanel = new JPanel(new GridLayout(4, 1, 1, 9)); // will hold inventory images

    JLabel inventoryLabel = new JLabel("Inventory");
//    inventoryLabel.setText("Inventory");
    inventoryLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
    inventoryLabel.setForeground(Color.black);

    //setting color
    rightSidePanel.setBackground(Color.black);
    controlPanel.setBackground(Color.black);
    inventoryPanel.setBackground(Color.black);

    //Creating TitleBorder for Inventory
    controlPanel.setSize(100, 300);
    inventoryPanel.setBorder(
        new TitledBorder(BorderFactory.createLineBorder(PURPLE, 10), "Inventory",
            TitledBorder.CENTER, TitledBorder.TOP, new Font("Times New Roman", Font.BOLD, 22),
            Color.lightGray));

    //pass inventory panel to imageUI to be updated
    imageUI.setGuiInventoryPanel(inventoryPanel);

    //creating buttons right panel
    mapBtn = new JButton("Go Orbit");
    menuBtn = new JButton();
    repairBtn = new JButton();
    helpBtn = new JButton();
    loadBtn = new JButton("Load");
    unloadBtn = new JButton("Unload");
    refuelBtn = new JButton("Refuel");
    interactBtn = new JButton("Interact");
    godModeBtn = new JButton("GOD MODE");
    mainBtn = new JButton("Main Screen");

    // When a user presses a button, the respective word is given to the Controller to use for function
    chaChaRealSmooth(godModeBtn, "god", false);
    chaChaRealSmooth(loadBtn, "load", false);
    chaChaRealSmooth(unloadBtn, "unload", false);
    chaChaRealSmooth(refuelBtn, "refuel", false);
    chaChaRealSmooth(repairBtn, "repair", false);
//    chaChaRealSmooth(helpBtn, "help", false);
    helpBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showHelpScreenBtn();
      }
    });
    chaChaRealSmooth(interactBtn, "interact", false);
    //Event Listener for menu button to open new window w/menu options
    menuBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMenu();
      }
    });
    mapBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMap();
        chaChaRealSmooth(mapBtn, "go orbit", true);
        currentPlanetLabel.setText(
            "<html>&emsp Current Location: <font color=#990000>Orbit</font></html>");
        controllerField.updateFuel();
        warningLabel.setVisible(false);
      }
    });
    mainBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMain();
      }
    });

    //Adding inventory and control to rightSide panel
//    rightSidePanel.add(backgroundLabel);

    rightSidePanel.add(controlPanel);
    rightSidePanel.add(inventoryPanel);

    //adding button to right panel: Control Panel
    createBtnIcon(menuBtn, "images/Menu.png", 125, 70);
    controlPanel.add(menuBtn);
    createBtnIcon(helpBtn, "images/Help.png", 125, 70);
    controlPanel.add(helpBtn);
    createBtnIcon(repairBtn, "images/Repair.png", 125, 70);
    controlPanel.add(repairBtn);
//    controlPanel.add(inventoryLabel);
//    controlPanel.add(mapBtn);
//    controlPanel.add(mainBtn);
//    controlPanel.add(loadBtn);
//    controlPanel.add(unloadBtn);
//    controlPanel.add(refuelBtn);
//    controlPanel.add(godModeBtn);
//    controlPanel.add(interactBtn);
  }

  public void createGameOverWinScreen() {
    gameOverWinPanel.setVisible(false);

//    showGameOverWinScreen();

    gameOverWinPanel.setLayout(new BorderLayout());
    gameOverWinPanel.setBackground(Color.BLACK);

    JPanel topPanel = new JPanel(new BorderLayout());
    Font titleFont = new Font("Times New Roman", Font.PLAIN, 100);

    JLabel gameOverLabel = new JLabel(
        new ImageIcon(getClass().getClassLoader().getResource("Astronaut-BoomBox.png")));

    JLabel youWonLabel = new JLabel("You Won!!!", SwingConstants.CENTER);
    JLabel partnerLabel = new JLabel("Happy Travels Partner ;)", SwingConstants.CENTER);

    topPanel.setFont(titleFont);
    topPanel.setBackground(Color.black);
    topPanel.setForeground(Color.lightGray);

    partnerLabel.setFont(titleFont);
    partnerLabel.setBackground(Color.black);
    partnerLabel.setForeground(Color.lightGray);

    youWonLabel.setFont(titleFont);
    youWonLabel.setBackground(Color.BLACK);
    youWonLabel.setForeground(Color.lightGray);

    topPanel.add(youWonLabel, BorderLayout.PAGE_START);
    topPanel.add(gameOverLabel, BorderLayout.CENTER);
    topPanel.add(partnerLabel, BorderLayout.PAGE_END);

    startBtn = new JButton("Start New Game");
    JButton quitBtn = new JButton("Quit Game");

    normalFont = new Font("Times New Roman", Font.PLAIN, 40);
    partnerLabel.setFont(normalFont);
    quitBtn.setBackground(Color.black);
    quitBtn.setForeground(Color.lightGray);
    quitBtn.setFont(normalFont);
    startBtn.setBackground(Color.black);
    startBtn.setForeground(Color.lightGray);
    startBtn.setFont(normalFont);

    bottomPanel = new JPanel();
    bottomPanel.setBackground(Color.BLACK);
    bottomPanel.add(startBtn);
    bottomPanel.add(quitBtn);
    bottomPanel.add(quitBtn);

    //Add btn listeners
    chaChaRealSmooth(quitBtn, "quit", false);
    chaChaRealSmooth(startBtn, "new", false);

    gameOverWinPanel.add(topPanel, BorderLayout.CENTER);
    gameOverWinPanel.add(bottomPanel, BorderLayout.PAGE_END);


  }

  public void createGameOverLoseScreen() {
    gameOverLosePanel.setVisible(false);
//    removeOtherPanelsToShowGameOverLoseScreen();
//    showGameOverLoseScreen();
    gameOverLosePanel.setLayout(new BorderLayout());
    gameOverLosePanel.setBackground(Color.BLACK);

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setBackground(Color.BLACK);

    Font titleFont = new Font("Times New Roman", Font.PLAIN, 100);

    JLabel youLostLabel = new JLabel("You Lost :(", SwingConstants.CENTER);
    JLabel gameOverLabel = new JLabel(
        new ImageIcon(getClass().getClassLoader().getResource("game_over_PNG56.png")),
        SwingConstants.CENTER);
    JLabel youSuckLabel = new JLabel("YOU SUCK LOSER!", SwingConstants.CENTER);

    youLostLabel.setFont(titleFont);
    youLostLabel.setForeground(Color.lightGray);

    topPanel.add(youLostLabel, BorderLayout.PAGE_START);
    topPanel.add(gameOverLabel, BorderLayout.CENTER);
    topPanel.add(youSuckLabel, BorderLayout.PAGE_END);

    normalFont = new Font("Times New Roman", Font.PLAIN, 40);

    startBtn = new JButton();
    startBtn.setFont(normalFont);
    startBtn.setBackground(Color.black);
    startBtn.setForeground(Color.lightGray);

    JButton quitBtn = new JButton();
    quitBtn.setFont(normalFont);
    quitBtn.setBackground(Color.black);
    quitBtn.setForeground(Color.lightGray);

    planetIcons(quitBtn, "images/Quit.png", 0, 0, 200, 100, 300, 100);
    planetIcons(startBtn, "images/New.png", 0, 0, 200, 100, 300, 100);

    bottomPanel = new JPanel();
    bottomPanel.setBackground(Color.BLACK);
    bottomPanel.add(startBtn);
    bottomPanel.add(quitBtn);

    chaChaRealSmooth(quitBtn, "quit", false);
    chaChaRealSmooth(startBtn, "new", false);

    gameOverLosePanel.add(topPanel, BorderLayout.CENTER);
    gameOverLosePanel.add(bottomPanel, BorderLayout.PAGE_END);


  }


  public void createTitleScreen() {
    //Create components

    titleScreenPanel = new JPanel();
    titleLabel = new JLabel("Space Pilot", SwingConstants.CENTER); //centers label
    Font titleFont = new Font("Times New Roman", Font.PLAIN, 130);
    normalFont = new Font("Times New Roman", Font.PLAIN, 30);
    startBtn = new JButton();
    continueBtn = new JButton();
    titleBtnPanel = new JPanel();
    //Set up titlePanel
    titleScreenPanel.setLayout(new GridLayout(2, 1, 5, 4));
    titleScreenPanel.setBackground(Color.black);
    //Set up titleLabel
    titleLabel.setFont(titleFont);
    titleLabel.setForeground(Color.lightGray);
    //Set up titleButtons & Panel
    startBtn.setBackground(Color.black);
    continueBtn.setBackground(Color.black);
    planetIcons(continueBtn, "images/Load.png", 0, 0, 200, 100, 300, 100);
    planetIcons(startBtn, "images/New.png", 0, 0, 200, 100, 300, 100);
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
        showBackgroundScreen();
      }
    });
    continueBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controllerField.textParser("continue");
        showGameScreenPanels();
      }
    });
  }

  public void createBackgroundScreen() {
    backgroundScreenPanel = new JPanel();
    JLabel backgroundLabel = new JLabel(
        "<html><center><font color=#850DBF'><font size='+10'>November 9, 2079...</font><br><br><font color=#C0C0C0'>A week ago, an unusual solar activity destroyed space navigation systems and life-support devices on multiple spaceships, stranding astronauts on the Moon, Mars, Mercury, Venus, Jupiter, Saturn, uranus, and Neptune.<br><br>A former Space Force Astronaut Carl Walker Jr. receives a call from NASA. He is asked to do his one last dance: a Search and Rescue mission.<br><br>Given the urgency of the situation, the international community manages to cooperate and quickly construct one special spacecraft. But time is not on Walker’s side...<br><br>Colonel Walker must rescue every stranded astronaut and safely bring them home in just one trip. NASA expects Colonel Walker to bring back ALL of the stranded astronauts!<br><br></center></html>");
    Font titleFont = new Font("Times New Roman", Font.BOLD, 32);
    backgroundLabel.setFont(titleFont);
    backgroundLabel.setForeground(Color.lightGray);
    backgroundLabel.setBounds(70, -75, 1000, 900);

    continueBtn = new JButton("Continue");
    JPanel backgroundBtnPanel = new JPanel();
    //Set up titlePanel
    backgroundScreenPanel.setLayout(null);
    backgroundScreenPanel.setBackground(Color.black);
    //Set up titleLabel
    planetIcons(continueBtn, "images/Continue.png", 0, 0, 200, 100, 300, 100);
    //Set up titleButtons & Panel
    continueBtn.setBackground(Color.black);
    backgroundBtnPanel.setBackground(Color.black);
    backgroundBtnPanel.setBounds(400, 725, 300, 100);
    continueBtn.setForeground(Color.lightGray);
    //Add components together
    backgroundScreenPanel.add(backgroundLabel);
    backgroundBtnPanel.add(continueBtn);
    backgroundScreenPanel.add(backgroundBtnPanel);

    //Add btn listeners
    continueBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showHelpScreen();
      }
    });
  }

  public void createHelpScreen() {
    //Create components
    helpScreenPanel = new JPanel();
    helpLabel = new JLabel(
        "<html><center><font color=#850DBF><font size='+20'>COMMANDS:</font><br><br>"
            + "<font color=#850DBF><font size='+5'>Travel: </font>"
            + "<font color=#C0C0C0>To blast off to a different planet, click on your Spaceship!</font><br><br>"
            + "<font color=#850DBF><font size='+5'>Repair: </font>"
            + "<font color=#C0C0C0>When your spacecraft is damaged, you can use the click Repair to fully heal yourself.</font><br>"
            + "<font color=#C0C0C0>Beware, you have 2 repairs available</font><br><br>"
            + "<font color=#850DBF><font size='+5'>Refuel: </font>"
            + "<font color=#C0C0C0>If you are low on fuel, you can refuel at the Station by clicking the fuel pump!</font><br>"
            + "<font color=#C0C0C0>Beware, you have 3 refuels available</font><br><br>"
            + "<font color=#850DBF><font size='+5'>Interact: </font>"
            + "<font color=#C0C0C0>To interact with extraterrestrial hazards and life-forms, click the icons!</font><br>"
            + "<font color='red'>*hint*</font><font color=#C0C0C0> You need specific items to pass obstacles and load astronauts </font><font color='red'>*hint*</font><br><br>"
            + "<font color=#850DBF><font size='+5'>Load: </font>"
            + "<font color=#C0C0C0>Load astronauts and items by clicking on the group of astronauts!</font><br><br>"
            + "<font color=#850DBF><font size='+5'>Unload: </font>"
            + "<font color=#C0C0C0>Unload astronauts by clicking the unload platform on Earth!</font><br><br></center></html>"); //centers label
    helpLabel.setVerticalTextPosition(SwingConstants.TOP);
    helpLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    Font titleFont = new Font("Times New Roman", Font.BOLD, 28);

    continueBtn = new JButton("Continue");
    helpBtnPanel = new JPanel();
    //Set up titlePanel
    helpScreenPanel.setLayout(null);
    helpScreenPanel.setBackground(Color.black);
    //Set up titleLabel
    helpLabel.setFont(titleFont);
    helpLabel.setForeground(Color.lightGray);
    helpLabel.setBounds(35, -50, 1070, 900);

    planetIcons(continueBtn, "images/Continue.png", 0, 0, 200, 100, 300, 100);
    //Set up titleButtons & Panel
    continueBtn.setBackground(Color.black);
    helpBtnPanel.setBackground(Color.black);
    helpBtnPanel.setBounds(400, 725, 300, 100);
    continueBtn.setForeground(Color.lightGray);
    //Add components together
    helpScreenPanel.add(helpLabel);
    helpBtnPanel.add(continueBtn);
    helpScreenPanel.add(helpBtnPanel);
//    helpScreenPanel.add(backgroundLabel);

    //Add btn listeners
    continueBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showGameScreenPanels();
        ticktock.getTimer().start();
      }
    });
  }


  public void createMenuPanel() {

    menuPanel = new JPanel(); //Create Panel for content
    menuPanel.setLayout(null);
    //Creating a menu
    menuPanel.setBackground(Color.black);

    JLabel backgroundLabel = new JLabel("");
    backgroundLabel.setIcon(
        new ImageIcon(getClass().getClassLoader().getResource("images/Space.jpg")));
    backgroundLabel.setBounds(0, 0, 1140, 900);
    //Creating menu buttons
    JButton soundSettingsBtn = new JButton();
    soundSettingsBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showSoundSettings();
      }
    });
    planetIcons(soundSettingsBtn, "images/Sound.png", 195, 75, 600, 150, 600, 150);
    JButton saveGameBtn = new JButton();
    planetIcons(saveGameBtn, "images/Save.png", 195, 250, 600, 150, 600, 150);
    chaChaRealSmooth(saveGameBtn, "save", false);
    JButton quitGameBtn = new JButton();
    planetIcons(quitGameBtn, "images/Quit.png", 195, 425, 600, 150, 600, 150);
    chaChaRealSmooth(quitGameBtn, "quit", false);
    JButton exitMenuBtn = new JButton();
    planetIcons(exitMenuBtn, "images/Return.png", 195, 600, 600, 150, 600, 150);
    exitMenuBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMain();
      }
    });
    menuPanel.add(soundSettingsBtn); //Adding all buttons to menu frame
    menuPanel.add(saveGameBtn);
    menuPanel.add(quitGameBtn);
    menuPanel.add(exitMenuBtn);
    menuPanel.add(backgroundLabel);
  }

  public void createMapPanel() {

    mapPanel = new JPanel(); //Create Panel for content
    //Creating a menu
    mapPanel.setBackground(Color.black);
    mapPanel.setLayout(null);

    JLabel backgroundLabel = new JLabel("");
    backgroundLabel.setIcon(
        new ImageIcon(getClass().getClassLoader().getResource("images/Space.jpg")));
    backgroundLabel.setBounds(0, 0, 1140, 900);

    //Creating maps buttons to go to respective planets below

    //    creates sun btn, icon, and functionality
    sunBtn = new JButton();
    planetIcons(sunBtn, "images/Sun.png", 330, 200, 400, 380, 400, 400);
    sunBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Music.playAudioFX("sounds/Explosion.wav");
        showGameOverLoseScreen();
      }
    });
    //    creates earth btn, icon, and functionality
    JButton earthBtn = new JButton("Earth");
    planetIcons(earthBtn, "images/Earth.png", 440, 80, 100, 118, 100, 98);
    chaChaRealSmooth(earthBtn, "go earth", true);
//    creates moon btn, icon, and functionality
    JButton moonBtn = new JButton("Moon");
    planetIcons(moonBtn, "images/Moon.png", 380, 40, 47, 48, 27, 28);
    chaChaRealSmooth(moonBtn, "go moon", true);
    //    creates mars btn, icon, and functionality
    JButton marsBtn = new JButton("Mars");
    planetIcons(marsBtn, "images/Mars.png", 80, 280, 100, 122, 100, 102);
    chaChaRealSmooth(marsBtn, "go mars", true);

    //CONSUMER TIPS
//    marsBtn.addActionListener(e -> movePlanetsListenerConsumer.accept("mars"));
    //    creates mercury btn, icon, and functionality
    JButton mercuryBtn = new JButton("Mercury");
    planetIcons(mercuryBtn, "images/Mercury.png", 230, 400, 60, 84, 60, 64);
    chaChaRealSmooth(mercuryBtn, "go mercury", true);
    //    creates saturn btn, icon, and functionality
    JButton saturnBtn = new JButton("Saturn");
    planetIcons(saturnBtn, "images/Saturn.png", 115, 540, 116, 104, 116, 84);
    chaChaRealSmooth(saturnBtn, "go saturn", true);
    //    creates venus btn, icon, and functionality
    JButton venusBtn = new JButton("Venus");
    planetIcons(venusBtn, "images/Venus.png", 775, 375, 74, 96, 74, 76);
    chaChaRealSmooth(venusBtn, "go venus", true);
    //    creates neptune btn, icon, and functionality
    JButton neptuneBtn = new JButton("Neptune");
    planetIcons(neptuneBtn, "images/Neptune.png", 100, 100, 110, 100, 110, 80);
    chaChaRealSmooth(neptuneBtn, "go neptune", true);
    //    creates jupiter btn, icon, and functionality
    JButton jupiterBtn = new JButton("Jupiter");
    planetIcons(jupiterBtn, "images/Jupiter.png", 800, 550, 158, 182, 158, 162);
    chaChaRealSmooth(jupiterBtn, "go jupiter", true);
    //    creates uranus btn, icon, and functionality
    JButton uranusBtn = new JButton("Uranus");
    planetIcons(uranusBtn, "images/Uranus.png", 500, 590, 86, 110, 86, 90);
    chaChaRealSmooth(uranusBtn, "go uranus", true);
    //    creates station btn, icon, and functionality
    stationBtn = new JButton("Station");
    planetIcons(stationBtn, "images/Station.png", 700, -40, 300, 340, 300, 300);
    chaChaRealSmooth(stationBtn, "go station", true);
    //    creates godmode btn
    dotBtn = new JButton();
    planetIcons(dotBtn, "images/Dot.png", 5, 5, 3, 3, 3, 3);
    chaChaRealSmooth(dotBtn, "god", false);
    //Adding all buttons to menu frame
    mapPanel.add(earthBtn);
    mapPanel.add(moonBtn);
    mapPanel.add(marsBtn);
    mapPanel.add(mercuryBtn);
    mapPanel.add(jupiterBtn);
    mapPanel.add(neptuneBtn);
    mapPanel.add(venusBtn);
    mapPanel.add(uranusBtn);
    mapPanel.add(saturnBtn);
    mapPanel.add(stationBtn);
    mapPanel.add(dotBtn);
    mapPanel.add(sunBtn);
    mapPanel.add(backgroundLabel);
    sunBtn.setOpaque(false);
    sunBtn.setContentAreaFilled(false);
    sunBtn.setBorderPainted(false);
    stationBtn.setOpaque(false);
    stationBtn.setContentAreaFilled(false);
    stationBtn.setBorderPainted(false);
    dotBtn.setOpaque(false);
    dotBtn.setContentAreaFilled(false);
    dotBtn.setBorderPainted(false);
  }

  public void soundButtons(JButton btn, Consumer<String> musicMethod, String wavFile) {
    btn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (wavFile != null) {
          Music.setMusicMute(true);
        }
        musicMethod.accept(wavFile);

      }
    });
  }

  //THESE METHODS WILL SHOW RESPECTIVE SCREENS AND HIDE OTHERS
  public void showGuiStart() {
    createTitleScreen();
    frame.add(titleScreenPanel, BorderLayout.CENTER);
    frame.setVisible(true);
    createBackgroundScreen();
    createHelpScreen();

  }

  public void showSoundSettings() {
    menuPanel.setVisible(false);
    centralDisplayPanel.setVisible(false);
    mapPanel.setVisible(false);
    frame.add(soundPanel, BorderLayout.CENTER);
    soundPanel.setVisible(true);
  }

  public void showMenu() {
    soundPanel.setVisible(false);
    mapPanel.setVisible(false);
    centralDisplayPanel.setVisible(false);
    frame.add(menuPanel, BorderLayout.CENTER);
    menuPanel.setVisible(true);
  }

  public void showMap() {
    menuPanel.setVisible(false);
    soundPanel.setVisible(false);
    centralDisplayPanel.setVisible(false);
    frame.add(mapPanel, BorderLayout.CENTER);
    mapPanel.setVisible(true);
  }

  public void showMain() {
    menuPanel.setVisible(false);
    soundPanel.setVisible(false);
    mapPanel.setVisible(false);
    frame.add(centralDisplayPanel, BorderLayout.CENTER);
    centralDisplayPanel.setVisible(true);
  }

  public void showGameScreenPanels() {
    //Attach panels to the outermost Main Frame
//    if(titleScreenPanel.isVisible()) {
    titleScreenPanel.setVisible(false);
//    }
//    if (gameOverWinPanel.isVisible()){
    gameOverWinPanel.setVisible(false);
//    }
//    if(gameOverLosePanel.isVisible()){
    gameOverLosePanel.setVisible(false);

    helpScreenPanel.setVisible(false);

    frame.add(statusPanel, BorderLayout.PAGE_START);
    frame.add(centralDisplayPanel, BorderLayout.CENTER);
    frame.add(rightSidePanel, BorderLayout.LINE_END);
    statusPanel.setVisible(true);
    centralDisplayPanel.setVisible(true);
    rightSidePanel.setVisible(true);
    imageUI.showEarthScreen2(); //Gets earth background screen.
    frame.setVisible(true);
  }


  public void showBackgroundScreen() {
    //Attach panels to the outermost Main Frame
//    if(titleScreenPanel.isVisible()) {
    titleScreenPanel.setVisible(false);
//    }
//    if (gameOverWinPanel.isVisible()){
    gameOverWinPanel.setVisible(false);
//    }
//    if(gameOverLosePanel.isVisible()){
    gameOverLosePanel.setVisible(false);

    frame.add(backgroundScreenPanel, BorderLayout.CENTER);
    frame.setVisible(true);
    backgroundScreenPanel.setVisible(true);
  }

  public void showHelpScreen() {
    //Attach panels to the outermost Main Frame
//    if(titleScreenPanel.isVisible()) {
    titleScreenPanel.setVisible(false);
//    }
//    if (gameOverWinPanel.isVisible()){
    gameOverWinPanel.setVisible(false);
//    }
//    if(gameOverLosePanel.isVisible()){
    gameOverLosePanel.setVisible(false);

    backgroundScreenPanel.setVisible(false);

    frame.add(helpScreenPanel, BorderLayout.CENTER);
    frame.setVisible(true);
    helpScreenPanel.setVisible(true);
  }

  public void showHelpScreenBtn() {
    statusPanel.setVisible(false);
    centralDisplayPanel.setVisible(false);
    rightSidePanel.setVisible(false);
    menuPanel.setVisible(false);
    mapPanel.setVisible(false);
    soundPanel.setVisible(false);
    helpScreenPanel.setVisible(true);
  }

  public void showNewGameReplay() {
    if (gameOverWinPanel.isVisible()) {
      gameOverWinPanel.setVisible(false);
    }
    if (gameOverLosePanel.isVisible()) {
      gameOverLosePanel.setVisible(false);
    }

    centralDisplayPanel.setVisible(true);
    statusPanel.setVisible(true);
    rightSidePanel.setVisible(true);

    imageUI.showEarthScreen2(); //Gets earth background screen.
  }

  public void showGameOverLoseScreen() {
    statusPanel.setVisible(false);
    centralDisplayPanel.setVisible(false);
    frame.remove(centralDisplayPanel);
    rightSidePanel.setVisible(false);
    mapPanel.setVisible(false);
    statusPanel.setVisible(false);
    soundPanel.setVisible(false);
    menuPanel.setVisible(false);
    frame.add(gameOverLosePanel, BorderLayout.CENTER);
    gameOverLosePanel.setVisible(true);
    gameOverLosePanel.setVisible(false);
    gameOverLosePanel.setVisible(true);
  }

  public void showGameOverWinScreen() {
    statusPanel.setVisible(false);
    centralDisplayPanel.setVisible(false);
    rightSidePanel.setVisible(false);
    mapPanel.setVisible(false);
    frame.add(gameOverWinPanel, BorderLayout.CENTER);
    gameOverWinPanel.setVisible(true);

  }


  //Converts sout from terminal to displayTextArea instead
  public void appendText(String text) {
    displayArea.append(text);
    displayArea.setCaretPosition((displayArea.getDocument().getLength()));
  }

  //Takes button actions as input and sends to Controller textParser()
  public void chaChaRealSmooth(JButton btn, String command, Boolean planet) {

    btn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (btn.equals(startBtn)) {
          frame.remove(gameOverLosePanel);
          frame.remove(gameOverWinPanel);
          warningMessage();
        }
        controllerField.textParser(command);
        if (!planet) {
          return;
        } else if (command.equals("go station")) {
          mapPanel.setVisible(false); //hides map panel
          centralDisplayPanel.setVisible(true); //shows central panel
          imageUI.showStationScreen3(); //gets correct background panel
        } else if (command.equals("go earth")) {
          mapPanel.setVisible(false);
          centralDisplayPanel.setVisible(true);
          imageUI.showEarthScreen2();
        } else {
          mapPanel.setVisible(false);
          centralDisplayPanel.setVisible(true);
          controllerField.getStatusUpdateForBackgrounds(); //update planet again before switch
          imageUI.createPlanetScreen(); //create the planet screen then show
          imageUI.showPlanetScreen1();
        }


      }
    });
  }

  //  STATUS UPDATES
  public void displayPlanetStatus(String item, String damageCondition, int numberOfAstronauts) {
    itemsOnPlanetLabel.setText(
        String.format("<html>&emsp&emsp Items on Planet: <font color=#990000>%s</font></html>",
            (item == null ? "None" : item)));
    damageConditionLabel.setText(String.format(
        "<html>&emsp&emsp&emsp&emsp Threat: <font color=#990000>%s</font></html>",
        (damageCondition == null ? "None" : damageCondition)));
    numberOfAstronautsOnPlanetLabel.setText(
        String.format("<html>&emsp # Astronauts on Planet: <font color=#990000>%s</font></html>",
            numberOfAstronauts));
//    imageUI.createPlanetScreen();
  }

  public void planetBackgroundUpdate(String item, String dangerCondition,
      int numberOfAstronautsOnPlanet,
      String currentPlanet, List<String> inventory) {

    imageUI.planetBackgroundCustomization(item, dangerCondition, numberOfAstronautsOnPlanet,
        currentPlanet,
        inventory);
  }

  public void displayGameStatus(Collection<String> inventory, Planet planet, int repairsLeft,
      int strandedAstros, int refuelsLeft) {
//    inventoryLabel.setText("Inventory: " + inventory);
    currentPlanetLabel.setText(
        String.format("<html>&emsp Current Location: <font color=#990000>%s</font></html>",
            planet.getName()));
    repairsLeftLabel.setText(
        String.format("<html>&emsp Repairs Left: <font color=#990000>%s</font></html>",
            repairsLeft));
    strandedAstronautsLabel.setText(
        String.format("<html>Stranded Astronauts: <font color=#990000>%s</font></html>",
            strandedAstros));
    //Sets the refuelsLeft field in imageUI to update station planet
    imageUI.setRefuelsLeft(refuelsLeft);
  }

  public void planetIcons(JButton btn, String png, Integer x, Integer y, Integer width,
      Integer height, Integer scaleWidth, Integer scaleHeight) {

    planetIcon = new ImageIcon(getClass().getClassLoader().getResource(png));
    Image img = planetIcon.getImage();
    Image newImg = img.getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_DEFAULT);
    planetIcon = new ImageIcon(newImg);

    btn.setIcon(planetIcon);
    btn.setBackground(Color.black);
    btn.setForeground(Color.lightGray);
    btn.setBorder(BorderFactory.createLineBorder(Color.black));
    btn.setVerticalTextPosition(SwingConstants.BOTTOM);
    btn.setHorizontalTextPosition(SwingConstants.CENTER);
    btn.setBounds(x, y, width, height);
  }

  public void warningMessage() {
    if (warningBoolean) {
      warningLabel.setVisible(true);
      centralDisplayPanel.setVisible(false);
      centralDisplayPanel.setVisible(true);
      warningBoolean = false;
    } else {
      warningLabel.setVisible(false);
      centralDisplayPanel.setVisible(false);
      centralDisplayPanel.setVisible(true);
      warningBoolean = true;
    }
  }

  public void removeWarningMessage() {
    warningLabel.setVisible(true);
    warningLabel.setVisible(false);
  }

  public void createBtnIcon(JButton btn, String png, int scaleWidth, int scaleHeight) {
    btnIcon = new ImageIcon(getClass().getClassLoader().getResource(png));
    Image img = btnIcon.getImage();
    Image newImg = img.getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_DEFAULT);
    btnIcon = new ImageIcon(newImg);
    btn.setIcon(btnIcon);
    btn.setBackground(Color.black);
    btn.setBorder(BorderFactory.createLineBorder(Color.black));
  }

  public void imageUiReset() {
    imageUI.setRefuelsLeft(3);
    imageUI.updateRefuelsOnStation();
    imageUI.getGuiInventoryPanel().removeAll();
  }

  public void setStatusPanelFont(JLabel label) {
    label.setFont(new Font("Times New Roman", Font.BOLD, 20));
    label.setForeground(Color.lightGray);
  }

  //  GETTERS AND SETTERS

  public JProgressBar getFuelLevelBar() {
    return fuelLevelBar;
  }

  public void setControllerField(Controller controllerField) {
    this.controllerField = controllerField;
  }

  public Ticktock getTicktock() {
    return ticktock;
  }

  public void setTicktock(Ticktock ticktock) {
    this.ticktock = ticktock;
  }

  public JProgressBar getShipHealthBar() {
    return shipHealthBar;
  }

  public JLabel getWarningLabel() {
    return warningLabel;
  }

  public void setWarningLabel(JLabel warningLabel) {
    this.warningLabel = warningLabel;
  }

}


