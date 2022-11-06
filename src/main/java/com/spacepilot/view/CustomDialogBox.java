package com.spacepilot.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class CustomDialogBox implements ActionListener {

  //Create a window
  JWindow window;

  public CustomDialogBox() {
    window = new JWindow();

    //sets window transparent
    window.setBackground(new Color(0,0,0,0));

    //create label
    JLabel label = new JLabel("This is a message dialog yo");

    //create button
    JButton button = new JButton("OKK");

    //add action listener
    button.addActionListener(this);

    JPanel panel  = new JPanel();

    panel.add(label);
    panel.add(button);
    window.add(panel);

    window.setSize(500, 500);
    window.setLocation(300, 300);

    window.setVisible(true);
    //Create a Label
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    window.setVisible(false);
  }
}
