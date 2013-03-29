import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

import javax.swing.JFileChooser;
/**
 *  Map-editor for the Skyport project, TG 2013.
 *  
 *  @author Emil Hatlelid
 **/

color bg;
Board current;
Hexagon[] palette;
boolean GUI_toggle, coo_toggle;
int selected;
JFileChooser fc;

void setup() {
  size(displayWidth-50, displayHeight);
  bg = color(250, 250, 250);
  GUI_toggle = true;
  coo_toggle = true;
  selected = 0;
  if (frame != null) {
    frame.setResizable(true);
  }

  current = new Board(4, 28);
  setupGUI();
}
void draw() {
  background(bg);
  current.drawBoard(coo_toggle);
  if (GUI_toggle) {
    drawGUI();
  }
}
void setupGUI() {
  String tileTypes = "VGOSERC";
  int y = 25;
  palette = new Hexagon [7];
  fc = new JFileChooser();
  fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

  for (int i =0; i < 7; i++) {
    palette[i] = new Hexagon(25, y, 22);
    y+=39;
    if (tileTypes.length()>0) {
      palette[i].type = tileTypes.charAt(0);
      tileTypes = tileTypes.substring(1);
    }
  }
}
void optionsMenu(){
  
}
void drawGUI() {
  fill(200, 200, 200);
  rect(-1, -1, 74, 290, 0, 0, 9, 0);
  rect(-1, 290, 74, 90, 0, 9, 9, 0);
  fill(230, 230, 230);
  noStroke();
  rect(0, palette[selected].y-14, 73, 28);
  stroke(1);
  for (Hexagon h : palette) {
    h.drawHexagon();
    fill(0, 0, 0);
    text(((h.y-25)/39)+1, h.x-3, h.y+4);
    text(h.type, (h.x+28), h.y+5);
  }
  fill(230, 230, 230);
  rect(-1, 292, 72, 20, 0, 9, 0, 0);
  rect(-1, 314, 72, 20, 0, 0, 0, 0);
  rect(-1, 336, 72, 20, 0, 0, 0, 0);
  rect(-1, 358, 72, 20, 0, 0, 9, 0);
  fill(0, 0, 0);
  text("Load", 20, 307);
  text("Save", 20, 329);
  text("Options", 13, 351);
  text("Reset", 17, 373);
}
void mouseReleased(){
}
void mouseClicked() {
  if (mouseX < 75 && mouseY < 360) {
    if (mouseY < 290) {
      selected = round((mouseY-25)/35);
    }
    else {
      if (mouseY >357) current.clearBoard();
      else if (mouseY > 335) {
        
      }
      else if (mouseY > 313) {
        int tempValue = fc.showSaveDialog(fc);
        try {
          if (tempValue == 0) current.toFile(fc.getSelectedFile());
        }
        catch (IOException e) {
          println(e);
        }
      }
      else {
        int tempValue = fc.showOpenDialog(fc);
        if (tempValue == JFileChooser.APPROVE_OPTION) {
          try {
            current.parseFile(fc.getSelectedFile());
          }
          catch(FileNotFoundException e) {
            println(e);
          }
        }
      }
    }
  }
  else {
    Hexagon h = current.getHex(mouseX, mouseY);
    if (h != null) {
      if (palette[selected].type != 'S' && h.type == 'S') current.players--;
      else if (palette[selected].type == 'S' && h.type != 'S') current.players++;
      h.type = palette[selected].type;
    }
  }
}
void keyPressed() {
  if (key == 'w' || key == 'W' || keyCode == UP) current.y+=18;
  else if (key == 'd' || key == 'D' || keyCode == RIGHT) current.x-=18;
  else if (key == 's' || key == 'S' || keyCode == DOWN) current.y-=18;
  else if (key == 'a' || key == 'A' || keyCode == LEFT) current.x+=18;
  //else if (key=='-') current.z-=0.1;
 // else if (key=='+') current.z+=0.1;
  else if ( key=='1'/*||key=='V'*/) selected=0;
  else if ( key=='2'/*||key=='G'*/) selected=1;
  else if ( key=='3'/*||key=='O'*/) selected=2;
  else if ( key=='4'/*||key=='S'*/) selected=3;
  else if ( key=='5'/*||key=='E'*/) selected=4;
  else if ( key=='6'/*||key=='R'*/) selected=5;
  else if ( key=='7'/*||key=='C'*/) selected=6;
  else if (key == 'z' || key == 'Z') GUI_toggle = (GUI_toggle)?false:true;
}

