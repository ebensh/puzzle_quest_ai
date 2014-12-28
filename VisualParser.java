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
  static int BOARD_LEFT = 219;
  static int BOARD_TOP = 131;
  static int TILE_WIDTH = 71;
  static int TILE_HEIGHT = 71;
  static int TILE_SPACER = 3;
  
  private BufferedImage img;
  
  public static VisualParser createFromScreen(int x, int y, int width, int height) {
    try {
      Robot robot = new Robot();
      return new VisualParser(robot.createScreenCapture(new Rectangle(x, y, width, height)));
    } catch (Exception e) {
      System.out.println("Could not read image from screen; exception: " + e.toString());
      return null;
    }
  }
  
  public static VisualParser createFromScreen() {
    return createFromScreen(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
  }

  public static VisualParser createFromFile(File path) {
    BufferedImage img = null;
    try {
      img = ImageIO.read(path);
    } catch (IOException e) {
      System.err.println("Couldn't read image from path " + path + ", " +
          "error: " + e.toString());
    }
    return new VisualParser(img);
  }
  
  private VisualParser(BufferedImage img) { this.img = img; }

  private static Collection<Rectangle> getTileRectangles() {
    ArrayList<Rectangle> rects = new ArrayList<Rectangle>(64);
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        rects.add(new Rectangle(
            BOARD_LEFT + col * (TILE_WIDTH + TILE_SPACER),
            BOARD_TOP + row * (TILE_HEIGHT + TILE_SPACER),
            TILE_WIDTH, TILE_HEIGHT));
      }
    }
    return rects;
  }
  
  private static boolean writeImageToFile(BufferedImage img, File path) {
    try {
      ImageIO.write(img, "png", path);
    } catch (IOException e) {
      System.err.println("Couldn't write image to path " + path + ", " +
          "error: " + e.toString());
      return false;
    }
    return true;
  }

  private static void drawRectangles(BufferedImage img, Collection<Rectangle> rects) {
    Graphics2D g = img.createGraphics();
    Stroke oldStroke = g.getStroke();
    g.setStroke(new BasicStroke(1));
    for (Rectangle rect : rects) {
      g.drawRect(rect.x, rect.y, rect.width, rect.height);
    }
    g.setStroke(oldStroke);
    g.dispose();
  }
  
  private static void drawRectanglesAroundTiles(BufferedImage img) {
    drawRectangles(img, getTileRectangles());
  }
  
  // Used only for initial training, saves each tile as tile_0.png, tile_1.png, ...
  public void saveTrainingTilesToDirectory(File directory) {
    Collection<Rectangle> rects = getTileRectangles();
    BufferedImage tile;
    
    int i = 0;
    for (Rectangle rect : rects) {
      // Create a copy that we can save.
      tile = img.getSubimage(rect.x, rect.y, rect.width, rect.height);
      File path = new File(directory, String.format("tile_%d.png", i++));
      writeImageToFile(tile, path);
    }
  }
  
  public void saveDebugImageToFile(File path) {
    // Create a copy that we can draw to.
    BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
    Graphics g = copy.getGraphics();
    g.drawImage(img, 0, 0, null);
    g.dispose();
    
    // Draw rectangles around the features we think we've identified.
    drawRectanglesAroundTiles(copy);
    // Save it to file.
    writeImageToFile(copy, path);
  }
  
  public Board getBoard() { 
    // TODO(ebensh): This method.
    Board board = new Board(8, 8);
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        board.setTile(i, j, Tile.WATER);
      }
    }
    return board;
  }
}
