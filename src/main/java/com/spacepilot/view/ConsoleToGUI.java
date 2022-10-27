package com.spacepilot.view;

import java.io.PrintStream;

public class ConsoleToGUI {

  public ConsoleToGUI() {
    Gui gui = new Gui();
    System.setOut(new PrintStream(new RedirectingOutputStream(gui), true));
//    gui.start();
  }

//  public void run(){
//    for(int i=0; i < 5; i++){
//      System.out.println("Test it out");
//    }
//  }
//
//  public static void main(String[] args) {
//    ConsoleToGUI ctg = new ConsoleToGUI();
//    ctg.run();
//  }

}
