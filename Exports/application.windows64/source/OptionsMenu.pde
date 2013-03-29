import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;

public class OptionsMenu extends JFrame {
  JPanel menuPanel;
  JLabel playersLabel;
  JTextField descriptionField;
  Skyport_MapEditor SME;
  
  public OptionsMenu(Skyport_MapEditor SME){
    this.SME = SME;
  }
}
