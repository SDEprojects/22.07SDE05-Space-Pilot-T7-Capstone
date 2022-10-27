package com.spacepilot.view;

import java.io.IOException;
import java.io.OutputStream;

public class RedirectingOutputStream extends OutputStream {

  private Gui gui;

  public RedirectingOutputStream(Gui gui) {
    this.gui = gui;
  }

  @Override
  public void write(int b) throws IOException {
    //redirects data to text area
    gui.appendText(String.valueOf((char) b));
    //scrolls the text area to end of data

  }
}
