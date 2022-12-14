package com.spacepilot.view;

import com.spacepilot.controller.Controller;
import com.spacepilot.model.Music;
import com.spacepilot.model.Planet;
import com.spacepilot.model.Ticktock;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.plaf.basic.BasicProgressBarUI;

public class Gui {
  //FIELDS
  public static final Color VERY_DARK_RED = new Color(153, 0, 0);
  public static final Color DARK_ORANGE = new Color(255, 102, 0);
  public static final Color PURPLE = new Color(133, 13, 191);
  private static JProgressBar shipHealthBar;
  public ImageIcon planetIcon, btnIcon;
  private JFrame frame;
  private JTextArea displayArea;
  private JScrollPane scrollPanel;
  private FloatControl gainControl;
  private JSlider slider;
  private Consumer<String> method;
  private Controller controllerField;
  private float currentVolume;
  private Font normalFont;
  private Ticktock ticktock;
  private Boolean isInitialGame;
  private ImageUI imageUI;
  private JProgressBar fuelLevelBar;
  private Boolean warningBoolean = true;
  private BorderLayout borderLayout = new BorderLayout();
  private GridLayout gridLayout = new GridLayout(0, 1, 5, 5); //0 rows, 1 col, 5 hor gap, 5 vert gap
  private JPanel gameOverLosePanel = new JPanel();
  private JPanel gameOverWinPanel = new JPanel();
  private JButton[] checkButtonArr = new JButton[8];
  private JPanel titleScreenPanel,statusPanel,centralDisplayPanel,rightSidePanel,planetStatusPanel,menuPanel,soundPanel,mapPanel,helpScreenPanel,backgroundScreenPanel,bottomPanel;
  private JButton moonBtn,marsBtn,mercuryBtn,saturnBtn,venusBtn,uranusBtn,jupiterBtn,neptuneBtn,continueBtn,startBtn,mapBtn;
  private JLabel currentPlanetLabel,damageConditionLabel,itemsOnPlanetLabel,numberOfAstronautsOnPlanetLabel,strandedAstronautsLabel,repairsLeftLabel,warningLabel,backgroundLabel,youSuckLabel;
  //CONSTRUCTOR

  public Gui() {

    //CREATES OUTERMOST MAIN FRAME
    frame = new JFrame("Main Panel"); //Create Frame for content //Default layout is BorderLayout
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Set closing event
    frame.setSize(new Dimension(1140, 900));
    frame.setResizable(false);

    //Centers a frame onscreen when it opens
    frame.setLocationRelativeTo(null);
  }

  //GUI  FUNCTIONALITY
  public void passCommandMethodsToImageGui() {
    //Creates an instance of imageUI while passing in runnables carrying the command methods from Gui to ImageUI
    //Effectively connects the class so this method can be passed and used in imageUI
    imageUI = new ImageUI(centralDisplayPanel,
        () -> {
          controllerField.textParser("load");
          //Updates background panel to remove astronuats
          imageUI.createPlanetScreen();
          //Refreshes panel to show updated images
          centralDisplayPanel.setVisible(false);
          centralDisplayPanel.setVisible(true);
        },
        () -> controllerField.textParser("unload"),
        () -> {
          controllerField.textParser("refuel");
          imageUI.updateRefuelsOnStation();
        },
        () -> controllerField.textParser("interact"),
        () -> {
          showMap();
          controllerField.textParser("go orbit");
          //updates fuel after moving
          controllerField.updateFuel();
          //update text for orbit
          currentPlanetLabel.setText(
              "<html>&emsp Current Location: <font color=#990000>Orbit</font></html>");
          controllerField.checkGameResult();
        });

  }
  public static void playMusic() {
    Music.playAudioMusic("sounds/Space_Chill.wav");
  }


  // THESE METHODS CREATE DIFFERENT SECTIONS OF THE GUI
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
  public void createBackgroundImagesForGui() {
    //Instantiates imageGUI and constructs background images for centralDisplayPanel Center
    //Adds the background panel to centerDisplayPanel
    imageUI.createTopLevelPanel();
    //Adds background images, scenes, and buttons to background panel
    imageUI.generateScene();
  }
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
    exitMenuBtn.addActionListener(e -> showMain());
    //slider is implemented to adjust volume up and down for current background music
    slider.addChangeListener(e -> {

      currentVolume = Music.currentVolume();
      currentVolume = slider.getValue();
      gainControl = Music.gainControl();
      gainControl.setValue(currentVolume);

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
    JPanel controlPanel = new JPanel(new GridLayout(3, 1, 0, 0)); //holds gui buttons
    JPanel inventoryPanel = new JPanel(new GridLayout(4, 1, 1, 9)); // will hold inventory images

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
    JButton menuBtn = new JButton();
    JButton repairBtn = new JButton();
    JButton helpBtn = new JButton();
    JButton loadBtn = new JButton("Load");
    JButton unloadBtn = new JButton("Unload");
    JButton refuelBtn = new JButton("Refuel");
    JButton interactBtn = new JButton("Interact");
    JButton godModeBtn = new JButton("GOD MODE");
    JButton mainBtn = new JButton("Main Screen");

    // When a user presses a button, the respective word is given to the Controller to use for function
    chaChaRealSmooth(godModeBtn, "god", false);
    chaChaRealSmooth(loadBtn, "load", false);
    chaChaRealSmooth(unloadBtn, "unload", false);
    chaChaRealSmooth(refuelBtn, "refuel", false);
    chaChaRealSmooth(repairBtn, "repair", false);
    helpBtn.addActionListener(e -> {
      showHelpScreenBtn();
      controllerField.stopTimer();
    });
    chaChaRealSmooth(interactBtn, "interact", false);
    //Event Listener for menu button to open new window w/menu options
    menuBtn.addActionListener(e -> {
      showMenu();
      controllerField.stopTimer();
    });
    mapBtn.addActionListener(e -> {
      showMap();
      chaChaRealSmooth(mapBtn, "go orbit", true);
      currentPlanetLabel.setText(
          "<html>&emsp Current Location: <font color=#990000>Orbit</font></html>");
      controllerField.updateFuel();
      warningLabel.setVisible(false);
    });
    mainBtn.addActionListener(e -> showMain());

    rightSidePanel.add(controlPanel);
    rightSidePanel.add(inventoryPanel);

    //adding button to right panel: Control Panel
    createBtnIcon(menuBtn, "images/Menu.png", 125, 70);
    controlPanel.add(menuBtn);
    createBtnIcon(helpBtn, "images/Help.png", 125, 70);
    controlPanel.add(helpBtn);
    createBtnIcon(repairBtn, "images/Repair.png", 125, 70);
    controlPanel.add(repairBtn);

  }
  public void createGameOverWinScreen() {
    gameOverWinPanel.setVisible(false);

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
deathStatement();
    gameOverLosePanel.setVisible(false);
    gameOverLosePanel.setLayout(new BorderLayout());
    gameOverLosePanel.setBackground(Color.BLACK);

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setBackground(Color.BLACK);

    Font titleFont = new Font("Times New Roman", Font.PLAIN, 100);

    JLabel youLostLabel = new JLabel("You Lost :(", SwingConstants.CENTER);
    JLabel gameOverLabel = new JLabel(
        new ImageIcon(getClass().getClassLoader().getResource("game_over_PNG56.png")),
        SwingConstants.CENTER);
    youSuckLabel = new JLabel("", SwingConstants.CENTER);
    Dimension loseStatement = new Dimension();
   loseStatement.setSize(450, 200);
    youSuckLabel.setPreferredSize(loseStatement);

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
    JLabel titleLabel = new JLabel(); //centers label
    Font titleFont = new Font("Times New Roman", Font.PLAIN, 130);
    normalFont = new Font("Times New Roman", Font.PLAIN, 30);
    startBtn = new JButton();
    continueBtn = new JButton();
    JPanel titleBtnPanel = new JPanel();

    titleLabel.setIcon(
        new ImageIcon(getClass().getClassLoader().getResource("images/Title.png")));
    titleLabel.setBounds(250,100, 600, 300);
    //Set up titlePanel
    titleScreenPanel.setLayout(null);
    titleScreenPanel.setBackground(Color.black);
    titleBtnPanel.setBounds(400, 550, 300, 300);
    //Set up titleLabel
    titleLabel.setFont(titleFont);
    titleLabel.setForeground(Color.lightGray);
    //Set up titleButtons & Panel
    startBtn.setBackground(Color.black);
    continueBtn.setBackground(Color.black);
    planetIcons(continueBtn, "images/Load.png", 870, 600, 200, 100, 300, 100);
    planetIcons(startBtn, "images/New.png", 570, 600, 200, 100, 300, 100);
    titleBtnPanel.setBackground(Color.black);
    //Add components together
    titleScreenPanel.add(titleLabel);
    titleBtnPanel.add(startBtn);
    titleBtnPanel.add(continueBtn);
    titleScreenPanel.add(titleBtnPanel);

    //Add btn listeners
    startBtn.addActionListener(e -> showBackgroundScreen());
    continueBtn.addActionListener(e -> controllerField.textParser("continue"));
  }
  public void createBackgroundScreen() {
    backgroundScreenPanel = new JPanel();
    JLabel backgroundLabel = new JLabel(
        "<html><center><font color=#850DBF'><font size='+10'>November 9, 2079...</font><br><br><font color=#C0C0C0'>A week ago, an unusual solar activity destroyed space navigation systems and life-support devices on multiple spaceships, stranding astronauts on the Moon, Mars, Mercury, Venus, Jupiter, Saturn, uranus, and Neptune.<br><br>A former Space Force Astronaut Carl Walker Jr. receives a call from NASA. He is asked to do his one last dance: a Search and Rescue mission.<br><br>Given the urgency of the situation, the international community manages to cooperate and quickly construct one special spacecraft. But time is not on Walker???s side...<br><br>Colonel Walker must rescue every stranded astronaut and safely bring them home in just one trip. NASA expects Colonel Walker to bring back ALL of the stranded astronauts!<br><br></center></html>");
    Font titleFont = new Font("Times New Roman", Font.BOLD, 32);
    backgroundLabel.setFont(titleFont);
    backgroundLabel.setForeground(Color.lightGray);
    backgroundLabel.setBounds(70, -75, 1000, 900);

    continueBtn = new JButton("Continue");
    JPanel backgroundBtnPanel = new JPanel();
    //Set up titlePanel
    backgroundScreenPanel.setLayout(null);
    backgroundScreenPanel.setBackground(Color.black);

    JLabel starsLabel = new JLabel("");
    starsLabel.setIcon(
        new ImageIcon(getClass().getClassLoader().getResource("images/Space.jpg")));
    starsLabel.setBounds(0, 0, 1140, 900);
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
    backgroundScreenPanel.add(starsLabel);


    //Add btn listeners
    continueBtn.addActionListener(e -> showHelpScreen());
  }
  public void createHelpScreen() {
    //Create components
    helpScreenPanel = new JPanel();
    JLabel helpLabel = new JLabel(
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
    Font titleFont = new Font("Times New Roman", Font.BOLD, 30);

    continueBtn = new JButton("Continue");
    JPanel helpBtnPanel = new JPanel();
    //Set up titlePanel
    helpScreenPanel.setLayout(null);
    helpScreenPanel.setBackground(Color.black);
    //Set up titleLabel
    helpLabel.setFont(titleFont);
    helpLabel.setForeground(Color.lightGray);
    helpLabel.setBounds(35, -65, 1070, 900);

    JLabel starsLabel = new JLabel("");
    starsLabel.setIcon(
        new ImageIcon(getClass().getClassLoader().getResource("images/Space.jpg")));
    starsLabel.setBounds(0, 0, 1140, 900);

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
    helpScreenPanel.add(starsLabel);

    //Add btn listeners
    continueBtn.addActionListener(e -> {
      //Conditional to show help at start at game vs help while playing game that returns to current screen.
      if(isInitialGame){
        showGameScreenPanels();
        isInitialGame = false;
      }
      else{
        showMain();
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
    soundSettingsBtn.addActionListener(e -> showSoundSettings());
    planetIcons(soundSettingsBtn, "images/Sound.png", 195, 75, 600, 150, 600, 150);
    JButton saveGameBtn = new JButton();
    planetIcons(saveGameBtn, "images/Save.png", 195, 250, 600, 150, 600, 150);
    chaChaRealSmooth(saveGameBtn, "save", false);
    JButton quitGameBtn = new JButton();
    planetIcons(quitGameBtn, "images/Quit.png", 195, 425, 600, 150, 600, 150);
    chaChaRealSmooth(quitGameBtn, "quit", false);
    JButton exitMenuBtn = new JButton();
    planetIcons(exitMenuBtn, "images/Return.png", 195, 600, 600, 150, 600, 150);
    exitMenuBtn.addActionListener(e -> showMain());
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

    backgroundLabel = new JLabel("");
    backgroundLabel.setIcon(
        new ImageIcon(getClass().getClassLoader().getResource("images/Space.jpg")));
    backgroundLabel.setBounds(0, 0, 1140, 900);
    //Creating maps buttons to go to respective planets below

    //    creates sun btn, icon, and functionality
    JButton sunBtn = new JButton();
    planetIcons(sunBtn, "images/Sun.png", 330, 200, 400, 380, 400, 400);
    sunBtn.addActionListener(e -> {
      Music.playAudioFX("sounds/Explosion.wav");
      youSuckLabel.setText(View.getPrintCrashIntoSunDeath());
      showGameOverLoseScreen();
    });
    //    creates earth btn, icon, and functionality
    JButton earthBtn = new JButton("Earth");
    planetIcons(earthBtn, "images/Earth.png", 440, 80, 100, 118, 100, 98);
    chaChaRealSmooth(earthBtn, "go earth", true);
//    creates moon btn, icon, and functionality
    moonBtn = new JButton("Moon");
    planetIcons(moonBtn, "images/Moon.png", 380, 40, 47, 48, 27, 28);
    chaChaRealSmooth(moonBtn, "go moon", true);
    //    creates mars btn, icon, and functionality
    marsBtn = new JButton("Mars");
    planetIcons(marsBtn, "images/Mars.png", 80, 280, 100, 122, 100, 102);
    chaChaRealSmooth(marsBtn, "go mars", true);

    //CONSUMER TIPS
    //    creates mercury btn, icon, and functionality
    mercuryBtn = new JButton("Mercury");
    planetIcons(mercuryBtn, "images/Mercury.png", 230, 400, 60, 84, 60, 64);
    chaChaRealSmooth(mercuryBtn, "go mercury", true);
    //    creates saturn btn, icon, and functionality
    saturnBtn = new JButton("Saturn");
    planetIcons(saturnBtn, "images/Saturn.png", 115, 540, 116, 104, 116, 84);
    chaChaRealSmooth(saturnBtn, "go saturn", true);
    //    creates venus btn, icon, and functionality
    venusBtn = new JButton("Venus");
    planetIcons(venusBtn, "images/Venus.png", 775, 375, 74, 96, 74, 76);
    chaChaRealSmooth(venusBtn, "go venus", true);
    //    creates neptune btn, icon, and functionality
    neptuneBtn = new JButton("Neptune");
    planetIcons(neptuneBtn, "images/Neptune.png", 100, 100, 110, 100, 110, 80);
    chaChaRealSmooth(neptuneBtn, "go neptune", true);
    //    creates jupiter btn, icon, and functionality
    jupiterBtn = new JButton("Jupiter");
    planetIcons(jupiterBtn, "images/Jupiter.png", 800, 550, 158, 182, 158, 162);
    chaChaRealSmooth(jupiterBtn, "go jupiter", true);
    //    creates uranus btn, icon, and functionality
    uranusBtn = new JButton("Uranus");
    planetIcons(uranusBtn, "images/Uranus.png", 500, 620, 86, 110, 86, 90);
    chaChaRealSmooth(uranusBtn, "go uranus", true);
    //    creates station btn, icon, and functionality
    JButton stationBtn = new JButton("Station");
    planetIcons(stationBtn, "images/Station.png", 700, -40, 300, 340, 300, 300);
    chaChaRealSmooth(stationBtn, "go station", true);
    //    creates godmode btn
    JButton godStarBtn = new JButton();
    planetIcons(godStarBtn, "images/Dot.png", 5, 5, 3, 3, 3, 3);
    chaChaRealSmooth(godStarBtn, "god", false);
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
    mapPanel.add(godStarBtn);
    mapPanel.add(sunBtn);
    mapPanel.add(backgroundLabel);
    sunBtn.setOpaque(false);
    sunBtn.setContentAreaFilled(false);
    sunBtn.setBorderPainted(false);
    stationBtn.setOpaque(false);
    stationBtn.setContentAreaFilled(false);
    stationBtn.setBorderPainted(false);
    godStarBtn.setOpaque(false);
    godStarBtn.setContentAreaFilled(false);
    godStarBtn.setBorderPainted(false);

  }


  //GUI FUNCTIONALITY
  public void removeCheckMarks(){
    //remove check marks from map btns
    for(JButton button : checkButtonArr){
      if(button != null){//check if button has assignment
        mapPanel.remove(button);
      }
    }
  }
  public void addCheckMarksFromLoadGame(List<Planet> allPlanets){
    for(Planet planet : allPlanets){
      addCheckMarkToCompletedPlanets(planet);
    }
  }
  public void addCheckMarkToCompletedPlanets(Planet currentPlanet){
    //get planet name and num astronauts on planet
    String planetName = currentPlanet.getName();
    int astronautsPresent = currentPlanet.getNumOfAstronautsOnPlanet();
    //add check to planets that cleared astronauts
    if(astronautsPresent <= 0){ //if current planet astronauts cleared, add checkmark
      switch (planetName){
        case "Neptune":
          checkButtonArr[0]  = new JButton();
          planetIcons(checkButtonArr[0], "images/checkMark.png", neptuneBtn.getLocation().x +43, neptuneBtn.getLocation().y-30, 25, 25, 25, 25);
          mapPanel.add(checkButtonArr[0]);
          break;
        case "Saturn":
          checkButtonArr[1] = new JButton();
          planetIcons(checkButtonArr[1], "images/checkMark.png", saturnBtn.getLocation().x +45, saturnBtn.getLocation().y -30, 25, 25, 25, 25);
          mapPanel.add(checkButtonArr[1]);
          break;
        case "Mercury":
          checkButtonArr[2] = new JButton();
          planetIcons(checkButtonArr[2], "images/checkMark.png", mercuryBtn.getLocation().x +20, mercuryBtn.getLocation().y -30, 25, 25, 25, 25);
          mapPanel.add(checkButtonArr[2]);
          break;
        case "Mars":
          checkButtonArr[3] = new JButton();
          planetIcons(checkButtonArr[3], "images/checkMark.png", marsBtn.getLocation().x +40, marsBtn.getLocation().y -30, 25, 25, 25, 25);
          mapPanel.add(checkButtonArr[3]);
          break;
        case "Moon":
          checkButtonArr[7] = new JButton();
          planetIcons(checkButtonArr[7], "images/checkMark.png", moonBtn.getLocation().x +15, moonBtn.getLocation().y -30, 25, 25, 25, 25);
          mapPanel.add(checkButtonArr[7]);
          break;
        case "Jupiter":
          checkButtonArr[5] = new JButton();
          planetIcons(checkButtonArr[5], "images/checkMark.png", jupiterBtn.getLocation().x +70, jupiterBtn.getLocation().y -30, 25, 25, 25, 25);
          mapPanel.add(checkButtonArr[5]);
          break;
        case "Venus":
          checkButtonArr[6] = new JButton();
          planetIcons(checkButtonArr[6], "images/checkMark.png", venusBtn.getLocation().x +35, venusBtn.getLocation().y -30, 25, 25, 25, 25);
          mapPanel.add(checkButtonArr[6]);
          break;
        case "Uranus":
          checkButtonArr[4] = new JButton();
          planetIcons(checkButtonArr[4], "images/checkMark.png", uranusBtn.getLocation().x  +33, uranusBtn.getLocation().y -30, 25, 25, 25, 25);
          mapPanel.add(checkButtonArr[4]);
          break;
      }


    }
    mapPanel.add(backgroundLabel);

  }
  public void soundButtons(JButton btn, Consumer<String> musicMethod, String wavFile) {
    btn.addActionListener(e -> {
      if (wavFile != null) {
        Music.setMusicMute(true);
      }
      musicMethod.accept(wavFile);
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
    helpScreenPanel.setVisible(false);

    frame.add(centralDisplayPanel, BorderLayout.CENTER);
    statusPanel.setVisible(true);
    rightSidePanel.setVisible(true);

    centralDisplayPanel.setVisible(true);
  }
  public void showGameScreenPanels() {
    //Set panels to false visibility
    titleScreenPanel.setVisible(false);
    gameOverWinPanel.setVisible(false);
    gameOverLosePanel.setVisible(false);
    helpScreenPanel.setVisible(false);

    //Attach panels to the outermost Main Frame
    frame.add(statusPanel, BorderLayout.PAGE_START);
    frame.add(centralDisplayPanel, BorderLayout.CENTER);
    frame.add(rightSidePanel, BorderLayout.LINE_END);

    //Set correct panels to visible
    statusPanel.setVisible(true);
    centralDisplayPanel.setVisible(true);
    rightSidePanel.setVisible(true);

    //Get earth background scene
    imageUI.showEarthScreen2();
    frame.setVisible(true);

    //starts the timer
    ticktock.getTimer().start();
  }
  public void showGameScreenPanelsForContinuedGame(String currentPlanet) {
    //Set panels to false visibility
    titleScreenPanel.setVisible(false);
    gameOverWinPanel.setVisible(false);
    gameOverLosePanel.setVisible(false);
    helpScreenPanel.setVisible(false);

    //Attach panels to the outermost Main Frame
    frame.add(statusPanel, BorderLayout.PAGE_START);
    frame.add(centralDisplayPanel, BorderLayout.CENTER);
    frame.add(rightSidePanel, BorderLayout.LINE_END);

    //Set correct panels to visible
    statusPanel.setVisible(true);
    centralDisplayPanel.setVisible(true);
    rightSidePanel.setVisible(true);

    //Get current planet background scene
    if(currentPlanet.equals("Earth")){
      imageUI.showEarthScreen2();
    }else if(currentPlanet.equals("Station")){
      imageUI.updateRefuelsOnStation();
      imageUI.showStationScreen3(); //gets correct background panel
    }else if(currentPlanet.equals("Orbit")){
      showMap();
    }else { //If it's any planet, create planet background
      controllerField.getStatusUpdateForBackgrounds(); //update planet again before switch
      imageUI.createPlanetScreen(); //create the planet screen then show
      imageUI.showPlanetScreen1();
    };

    //set visibility
    frame.setVisible(true);

    //starts the timer
    ticktock.getTimer().start();
  }
  public void showBackgroundScreen() {
    //Attach panels to the outermost Main Frame
    titleScreenPanel.setVisible(false);

    gameOverWinPanel.setVisible(false);

    gameOverLosePanel.setVisible(false);

    frame.add(backgroundScreenPanel, BorderLayout.CENTER);
    frame.setVisible(true);
    backgroundScreenPanel.setVisible(true);
  }
  public void showHelpScreen() {
    //Displays help screen at start of game as part of intro
    //Attach panels to the outermost Main Frame

    titleScreenPanel.setVisible(false);

    gameOverWinPanel.setVisible(false);

    gameOverLosePanel.setVisible(false);

    backgroundScreenPanel.setVisible(false);


    menuPanel.setVisible(false);
    soundPanel.setVisible(false);
    mapPanel.setVisible(false);
    centralDisplayPanel.setVisible(false);

    frame.add(helpScreenPanel, BorderLayout.CENTER);
    frame.setVisible(true);
    helpScreenPanel.setVisible(true);
  }
  public void showHelpScreenBtn() {
    //Displays help screen after game has started
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
    deathStatement();
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

  //GUI FUNCTIONALITY
  public void appendText(String text) {
    //Converts sout from terminal to displayTextArea instead
    displayArea.append(text);
    displayArea.setCaretPosition((displayArea.getDocument().getLength()));
  }
  public void chaChaRealSmooth(JButton btn, String command, Boolean planet) {
    //Takes button actions as input and sends to Controller textParser()
    btn.addActionListener(e -> {
      if (btn.equals(startBtn)) {
        frame.remove(gameOverLosePanel);
        frame.remove(gameOverWinPanel);
        warningMessage();
      }
      controllerField.textParser(command);
      if (!planet) {
        if(command.equals("god")){ //pop up for god movde
          CustomDialogBox godDialog = new CustomDialogBox(frame, "Message", "God Mode has Been Enabled.\n"
              + " \nAll items added to inventory."
              + " \nStatus bars replenished."
              + " \nAll astronauts on ship.");
        }else if(command.equals("save")){ //pop up for save
            CustomDialogBox saveDialog = new CustomDialogBox(frame, "Save Message", "Game Saved Successfully!");
        }
      } else if (command.equals("go station")) {
        mapPanel.setVisible(false); //hides map panel
        centralDisplayPanel.setVisible(true); //shows central panel
        imageUI.updateRefuelsOnStation();
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
      String currentPlanet, List<String> inventory, int startingRefuel, int refuelsLeft ) {
    //sends game status info to imageGUI to update planet backgrounds
    imageUI.planetBackgroundCustomization(item, dangerCondition, numberOfAstronautsOnPlanet,
        currentPlanet,
        inventory);
    imageUI.setStartingRefuels(startingRefuel);
    imageUI.setRefuelsLeft(refuelsLeft);
  }
  public void displayGameStatus(Collection<String> inventory, Planet planet, int repairsLeft,int startingRepairs,
     int startingStrandedAstronauts, int rescuedAstronauts) {
    currentPlanetLabel.setText(
        String.format("<html>&emsp Current Location: <font color=#990000>%s</font></html>",
            planet.getName()));
    repairsLeftLabel.setText(
        String.format("<html>&emsp Repairs Left: <font color=#990000>%1$s / %2$s</font></html>",
            repairsLeft, startingRepairs));
    strandedAstronautsLabel.setText(
        String.format("<html>Stranded Astronauts: <font color=#990000>%1$s / %2$s</font></html>",
            rescuedAstronauts, startingStrandedAstronauts));

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
  public void inventoryImagesReset() {
    //To update inventory with replay
    imageUI.loopThroughInventoryToCheckCurrentItems();
  }
  public void setStatusPanelFont(JLabel label) {
    label.setFont(new Font("Times New Roman", Font.BOLD, 20));
    label.setForeground(Color.lightGray);
  }
  public void deathStatement() {
    if (shipHealthBar.getValue() < 2) {
      youSuckLabel.setText(View.getPrintOutOfHealthDeath());
    } else if (fuelLevelBar.getValue() < 2) {
      youSuckLabel.setText(View.getPrintOutOfFuelDeath());
    } else if (ticktock.getOxygenTickerLose()) {
      youSuckLabel.setText(View.getPrintOutOfTimeDeath());
    }
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
  public void setInitialGame(Boolean initialGame) {
    isInitialGame = initialGame;
  }

}


