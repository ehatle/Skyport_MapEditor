import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;

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
  void parseFile(File file) throws FileNotFoundException {
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
  void toFile(File file) throws IOException {
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
  String[] parseLine(Scanner sc) {
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
  void clearBoard() {
    for (int j = 0; j<dimensions; j++) {
      for (int k =0; k< dimensions;k++) {
        board[j][k] = new Hexagon(round(((k-j)*2)*((tileSize/2)*sqrt(3))), round((j+k)*((tileSize/2)*sqrt(3))), tileSize);
      }
    }
  }
  void drawBoard(boolean toggle) {
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
  Hexagon getHex(int hexX, int hexY) {
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

