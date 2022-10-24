package com.spacepilot.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import jm.JMC;

public final class Music implements JMC {

  public static void playMusic(String filePath)
      throws MidiUnavailableException, IOException, InvalidMidiDataException {

    Sequencer sequencer = MidiSystem.getSequencer();
    sequencer.open();
    InputStream inputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
    sequencer.setSequence(inputStream);
    sequencer.start();
  }

}
