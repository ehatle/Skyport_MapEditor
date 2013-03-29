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
  void drawHexagon() {
    noFill();
    if (this.type != 'V') {
      color tileColor = color(72, 72, 72);
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

