import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.AWTException;
import java.util.*;

public class VisualParser {
  static int SCREEN_WIDTH = 1024;
  static int SCREEN_HEIGHT = 768;
  static int BOARD_LEFT = 217;
  static int BOARD_TOP = 129;
  static int TILE_WIDTH = 74;
  static int TILE_HEIGHT = 74;
  
  private static BufferedImage readImageFromScreen(int x, int y, int width, int height) {
    try {
      Robot robot = new Robot();
      return robot.createScreenCapture(new Rectangle(x, y, width, height));
    } catch (Exception e) {
      System.out.println("Could not read image from screen; exception: " + e.toString());
      return null;
    }
  }
  
  private static BufferedImage readImageFromScreen() {
    return readImageFromScreen(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
  }

  private static BufferedImage readImageFromFile(String path) {
    BufferedImage img = null;
    try {
      img = ImageIO.read(new File(path));
    } catch (IOException e) {
      System.out.println("Couldn't read image from path " + path + ", " +
          "error: " + e.toString());
    }
    return img;
  }

  private static boolean writeImageToFile(BufferedImage img, String path) {
    try {
      File outputfile = new File(path);
      ImageIO.write(img, "png", outputfile);
    } catch (IOException e) {
      System.out.println("Couldn't write image to path " + path + ", " +
          "error: " + e.toString());
      return false;
    }
    return true;
  }

  private static Collection<Rectangle> getTileRectangles() {
    ArrayList<Rectangle> rects = new ArrayList<Rectangle>(64);
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        rects.add(new Rectangle(
            BOARD_LEFT + col * TILE_WIDTH,
            BOARD_TOP + row * TILE_HEIGHT,
            TILE_WIDTH, TILE_HEIGHT));
      }
    }
    return rects;
  }

  private static void drawRectanglesOnImage(BufferedImage img, Collection<Rectangle> rects) {
    Graphics2D g = img.createGraphics();
    Stroke oldStroke = g.getStroke();
    g.setStroke(new BasicStroke(2));
    for (Rectangle rect : rects) {
      g.drawRect(rect.x, rect.y, rect.width, rect.height);
    }
    g.setStroke(oldStroke);
  }
  
  private static void drawTilesOnImage(BufferedImage img) {
    drawRectanglesOnImage(img, getTileRectangles());
  }
  
  private static Board getBoardFromImage(BufferedImage img) { 
    // TODO(ebensh): This method.
    Board board = new Board(8, 8);
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        board.setTile(i, j, Tile.WATER);
      }
    }
    return board;
  }
  
  public static Board getBoard(String path) {
    BufferedImage img;
    if (path == null || path.equals("")) {
      img = readImageFromScreen();
    } else {
      img = readImageFromFile(path);
    }
    return getBoardFromImage(img);
  }
}
