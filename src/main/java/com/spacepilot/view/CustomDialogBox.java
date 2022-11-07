package com.spacepilot.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;

public class CustomDialogBox implements ActionListener {

  //Create a dialog
  JDialog dialog;

  /**
   * Creates a custom dialog box for use in GUI using String []
   * @param dialogOwner Will open on given frame and center dialog box on it.
   * @param dialogTitle Title of dialog box.
   * @param stringArrayMessage Appended into textArea line by line to be displayed to user.
   */
  public CustomDialogBox(Frame dialogOwner, String dialogTitle, String[] stringArrayMessage) {

    //create Dialog
    dialog = new JDialog(dialogOwner, dialogTitle);
    //prevent clicking when dialog up
    dialog.setModal(true);
    //set dialog size
    dialog.setPreferredSize(new Dimension(500, 700));

    //Create panel to hold text and button
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS)); //layout that stacks

    //Create text area and format
    JTextArea textArea = new JTextArea();
    //wrap lines
    textArea.setLineWrap(true);
    //wrap around words, not characters
    textArea.setWrapStyleWord(true);
    //stop editing, center alignment
    textArea.setEditable(false);
    //clear color
    textArea.setBackground(null);

    //add strings to textarea
    for(String line : stringArrayMessage){
      textArea.append(line);
      textArea.append("\n");
    }

    //create exit button
    JButton button = new JButton("OK");
    //center button
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    //add action listener
    button.addActionListener(this);
    //set button size
//    button.setPreferredSize(new Dimension(30, 30));

    //Adds label and button to panel
    panel.add(textArea);
    //Adds invisible component go give spacing.
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    panel.add(button);

    //Add panel to dialog
    dialog.add(panel);

    //sets dialog ot preferred size
    dialog.pack();
    //Centers dialog to frame when it opens
    dialog.setLocationRelativeTo(dialogOwner);
    //set visibility
    dialog.setVisible(true);
  }


  /**
   * Creates a custom dialog box for use in GUI using string
   * @param dialogOwner Opens on given frame and center dialog box on it.
   * @param dialogTitle Title of dialog box.
   * @param stringMessage Appended into textArea to be displayed to user.
   */
  public CustomDialogBox(Frame dialogOwner, String dialogTitle, String stringMessage) {

    //create Dialog
    dialog = new JDialog(dialogOwner, dialogTitle);
    //prevent clicking when dialog up
    dialog.setModal(true);
    //set dialog size
    dialog.setPreferredSize(new Dimension(475, 550));

    //Create panel to hold text and button
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS)); //layout that stacks

    //Create text area and format
    JTextArea textArea = new JTextArea();
    //wrap lines
    textArea.setLineWrap(true);
    //wrap around words, not characters
    textArea.setWrapStyleWord(true);
    //stop editing, center alignment
    textArea.setEditable(false);
    //clear color
    textArea.setBackground(null);

    //Adds strings to textarea
    textArea.append(stringMessage);

    //create exit button
    JButton button = new JButton("OK");
    //center button
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    //add action listener
    button.addActionListener(this);
    //set button size
//    button.setPreferredSize(new Dimension(30, 30));

    //Adds label and button to panel
    panel.add(textArea);
    //Adds invisible component go give spacing.
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    panel.add(button);

    //Add panel to dialog
    dialog.add(panel);

    //sets dialog ot preferred size
    dialog.pack();
    //Centers dialog to frame when it opens
    dialog.setLocationRelativeTo(dialogOwner);
    //set visibility
    dialog.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    dialog.setVisible(false);
  }
}
