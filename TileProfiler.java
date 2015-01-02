import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// Given a set of labels and files, create a profile for each label.

// Example:
// For OFFSET 16, we have profiles:
// File: C:\code\puzzle_quest_ai\learning\tile_dataset\Air\tile_0.png
// Avg: 134, 135, 4
// File: C:\code\puzzle_quest_ai\learning\tile_dataset\Coin\tile_2.png
// Avg: 207, 136, 72
// File: C:\code\puzzle_quest_ai\learning\tile_dataset\Earth\tile_1.png
// Avg: 2, 119, 24
// File: C:\code\puzzle_quest_ai\learning\tile_dataset\Fire\tile_4.png
// Avg: 133, 4, 5
// File: C:\code\puzzle_quest_ai\learning\tile_dataset\Skull\tile_7.png
// Avg: 140, 108, 85
// File: C:\code\puzzle_quest_ai\learning\tile_dataset\Star\tile_15.png
// Avg: 152, 0, 152
// File: C:\code\puzzle_quest_ai\learning\tile_dataset\Water\tile_5.png
// Avg: 16, 118, 135

public class TileProfiler {
  public static class RGB {
    public final int red;
    public final int green;
    public final int blue;
    public RGB() { this(0, 0, 0); }
    public RGB(int red, int green, int blue) {
      this.red = red;
      this.green = green;
      this.blue = blue;
    }
    
    public double distance(RGB otherRGB) {
      return Math.sqrt(Math.pow(otherRGB.red - this.red, 2) +
                        Math.pow(otherRGB.green - this.green, 2) +
                        Math.pow(otherRGB.blue - this.blue, 2));
    }
    
    @Override
    public String toString() {
      return String.format("(%d, %d, %d)", red, green, blue);
    }
    
    // TODO(ebensh): Optimize as a lookup keyed on Tile?
    public static RGB AIR = new RGB(134, 135, 4);
    public static RGB COIN = new RGB(207, 136, 72);
    public static RGB EARTH = new RGB(2, 119, 24);
    public static RGB FIRE = new RGB(133, 4, 5);
    public static RGB SKULL = new RGB(140, 108, 85);
    public static RGB STAR = new RGB(152, 0, 152);
    public static RGB WATER = new RGB(16, 118, 135);
  }

  private static RGB avgRGB(BufferedImage img) {
    RGB avg = new RGB();
    
    // In a circle of diameter 71 pixels (like our tiles) to omit the corners
    // and only take an inscribed square we start ~10 pixels in and end 10
    // pixels early. This examines only the 50x50 square inscribed in the tile.
    int OFFSET = 16;
    int red = 0;
    int green = 0;
    int blue = 0;
    
    for (int y = OFFSET; y < img.getHeight() - OFFSET; y++) {
      for (int x = OFFSET; x < img.getWidth() - OFFSET; x++) {
        int pixel = img.getRGB(x, y);
        red   += pixel >> 16 & 0xff;
        green += pixel >>  8 & 0xff;
        blue  += pixel       & 0xff;
      }
    }
    
    int numPixels = (img.getHeight() - 2 * OFFSET) * (img.getWidth() - 2 * OFFSET);
    red /= numPixels;
    green /= numPixels;
    blue /= numPixels;
    //System.out.format("Avg: %d, %d, %d%n", red, green, blue);
    return new RGB(red, green, blue);
  }
  
  private static Tile classifyRGBToTile(RGB rgb) {
    // Now find the closest profile point, and return that tile.
    // TODO(ebensh): Replace this with sane code. How does Java not have
    // tuples, lambdas, first class functions? Am I just missing it?
    RGB[] profiles = { RGB.AIR, RGB.COIN, RGB.EARTH, RGB.FIRE, RGB.SKULL, RGB.STAR, RGB.WATER };
    Tile[] tiles = { Tile.AIR, Tile.COIN, Tile.EARTH, Tile.FIRE, Tile.SKULL, Tile.STAR, Tile.WATER };
    Tile bestTile = Tile.AIR;
    double bestDist = Double.MAX_VALUE;
    for (int i = 0; i < profiles.length; i++) {
      double dist = rgb.distance(profiles[i]);
      if (dist < bestDist) {
        bestTile = tiles[i];
        bestDist = dist;
      }
    }
    
    return bestTile;
  }
  
  public static Tile classifyImageToTile(BufferedImage img) {
    return classifyRGBToTile(avgRGB(img));
  }
  
  public static void main(String args[]) {
    File[] files = {
      new File("C:/code/puzzle_quest_ai/learning/tile_dataset/Air/tile_0.png"),
      new File("C:/code/puzzle_quest_ai/learning/tile_dataset/Coin/tile_2.png"),
      new File("C:/code/puzzle_quest_ai/learning/tile_dataset/Earth/tile_1.png"),
      new File("C:/code/puzzle_quest_ai/learning/tile_dataset/Fire/tile_4.png"),
      new File("C:/code/puzzle_quest_ai/learning/tile_dataset/Skull/tile_7.png"),
      new File("C:/code/puzzle_quest_ai/learning/tile_dataset/Star/tile_15.png"),
      new File("C:/code/puzzle_quest_ai/learning/tile_dataset/Water/tile_5.png")
    };
    for (File file : files) {
      try {
        BufferedImage img = ImageIO.read(file);
        System.out.println("File: " + file.getPath());
        RGB avg = avgRGB(img);
        Tile tile = classifyRGBToTile(avg);
        System.out.println("RGB: " + avg.toString() + " => " + tile.toString());
      } catch (IOException e) {
        System.err.println("Could not read file " + file.getPath() + "; error: " + e.toString());
      }
    }
  }
}