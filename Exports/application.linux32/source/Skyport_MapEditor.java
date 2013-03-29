import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Scanner; 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.io.FileWriter; 
import java.io.BufferedWriter; 
import javax.swing.JFileChooser; 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.util.Scanner; 
import java.io.FileWriter; 
import java.io.BufferedWriter; 
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

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Skyport_MapEditor extends PApplet {









/**
 *  Map-editor for the Skyport project, TG 2013.
 *  
 *  @author Emil Hatlelid
 **/

int bg;
Board current;
Hexagon[] palette;
boolean GUI_toggle, coo_toggle;
int selected;
JFileChooser fc;

public void setup() {
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
public void draw() {
  background(bg);
  current.drawBoard(coo_toggle);
  if (GUI_toggle) {
    drawGUI();
  }
}
public void setupGUI() {
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
public void optionsMenu(){
  
}
public void drawGUI() {
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
public void mouseReleased(){
}
public void mouseClicked() {
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
public void keyPressed() {
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








public class Board {
  Hexagon[][] board;
  int x, y, tileSize, players;
  float z;
  int dimensions;
  String description;
  public Board(int jk, int tileSize) {
    this.x = displayWidth/2;
    this.y = 100;
    this.dimensions = jk;
    this.tileSize = tileSize;
    board = new Hexagon[jk][jk];
    clearBoard();
    z = 1;
  }
  public void parseFile(File file) throws FileNotFoundException {
    Scanner sc = new Scanner(file);
    //HEADER
    String playersArray[] = sc.nextLine().split("\\s");
    String sizeArray[] = sc.nextLine().split("\\s");
    String descriptionArray[] = sc.nextLine().split("\\s");

    players = Integer.parseInt(playersArray[1]);
    dimensions = Integer.parseInt(sizeArray[1]);
    description = descriptionArray[1];
    for (String line : descriptionArray) {
      if (line.trim().length()>0) description += " " + line;
    }
    x = displayWidth/2;
    y = 100;
    board = new Hexagon[dimensions][dimensions];
    clearBoard();

    //BODY
    int i =0;
    int currentLength = 1;
    while (i < dimensions) {
      String lines[] = parseLine(sc);
      if (lines.length==0) {
        continue;
      }
      if (lines.length != currentLength) {
        println("Error: expected this line to have length "
          + currentLength + ", but got " + lines.length);
        continue;
      }
      if (currentLength == 1) {
        board[0][0].type = lines[0].charAt(0);
      } 
      else {
      }
    }
  }
  public void toFile(File file) throws IOException {
    BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
    //HEADER
    bw.write("players " + players + "\nsize " + dimensions + "\ndescription \"" + description + "\"");
    //BODY
    int jLevel = 0;
    int kLevel = dimensions;
    int j = 0;
    int k = 0;
    String[] completion = new String[dimensions];
    for (String s : completion) {
      s = "";
    }

    while (k < dimensions-1) {
      bw.write(board[j][k].type);
      completion[j]+=board[j][k].type;
      if (j!=0) {
        j--;
        k++;
      } 
      else {
        bw.write("\n");
        j = ++jLevel;
        k =0;
      }
    }    
    while (k > 1) {
      k = dimensions-1;
      j = dimensions-1;
      kLevel = dimensions-1;

      bw.write(board[j][k].type);
      completion[j]+=board[j][k].type;

      if (k!=dimensions) {
        j--;
        k++;
      }
      else {
        j = dimensions;
        k = --kLevel;
      }
    }    
    for (String s : completion) {
      bw.write(s);
    }
    println(completion);
    bw.close();
  }
  public String[] parseLine(Scanner sc) {
    String line = sc.nextLine();
    line = line.replace("/", "");
    line = line.replace("\\", "");
    line = line.replace("_", "");
    line = line.replace(" ", "");
    line = line.replace("\t", "");
    if (line.equals("")) {
      return new String[0];
    }
    String array[] = line.split("");
    String newArray[] = new String[array.length-1];
    System.arraycopy(array, 1, newArray, 0, array.length-1); // oh java, you so silly
    return newArray;
  }
  public void clearBoard() {
    for (int j = 0; j<dimensions; j++) {
      for (int k =0; k< dimensions;k++) {
        board[j][k] = new Hexagon(round(((k-j)*2)*((tileSize/2)*sqrt(3))), round((j+k)*((tileSize/2)*sqrt(3))), tileSize);
      }
    }
  }
  public void drawBoard(boolean toggle) {
    pushMatrix();
    translate(x, y);
    scale(z);
    for (int j = 0; j<dimensions; j++) {
      for (int k =0; k< dimensions;k++) {
        board[j][k].drawHexagon();
        if (toggle) {
          fill(0, 0, 0);
          text(Integer.toString(j) + "," + Integer.toString(k), board[j][k].x-(tileSize-7), board[j][k].y+(tileSize/5));
        }
      }
    }
    popMatrix();
  }
  public Hexagon getHex(int hexX, int hexY) {
    hexX -= x;
    hexY -= y;
    for (int j = 0; j<dimensions; j++) {
      for (int k =0; k< dimensions;k++) {
        if (hexX < board[j][k].x + tileSize && hexX > board[j][k].x-tileSize && hexY < board[j][k].y + tileSize && hexY > board[j][k].y-tileSize) return board[j][k];
      }
    }
    return null;
  }
}

public class Hexagon {
  float a, b, c;
  int x, y;
  char type;
  public Hexagon(int x, int y, float sideLength) {
    this.x = x;
    this.y = y;
    this.c = sideLength;
    this.b = this.c / 2;
    this.a = sqrt((this.c * this.c) - (this.b * this.b));
    this.type = 'V';
  }
  public void drawHexagon() {
    noFill();
    if (this.type != 'V') {
      int tileColor = color(72, 72, 72);
      if (this.type != 'O') {
        if (this.type == 'G') tileColor = color(0, 120, 0);
        else if (this.type=='S') tileColor = color(200, 0, 200);
        else if (this.type=='E') tileColor = color(250, 250, 51);
        else if (this.type=='R') tileColor = color(200, 0, 0);
        else if (this.type=='C') tileColor = color(154, 77, 0);
      }
      fill(tileColor);
    }
    smooth();
    beginShape();
    vertex(x-b, y-a);
    vertex(x+b, y-a);
    vertex(x+c, y);
    vertex(x+b, y+a);
    vertex(x-b, y+a);
    vertex(x-c, y);
    endShape(CLOSE);
  }
}














public class OptionsMenu extends JFrame {
  JPanel menuPanel;
  JLabel playersLabel;
  JTextField descriptionField;
  Skyport_MapEditor SME;
  
  public OptionsMenu(Skyport_MapEditor SME){
    this.SME = SME;
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Skyport_MapEditor" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
