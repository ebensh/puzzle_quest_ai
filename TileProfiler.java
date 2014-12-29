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
  private static class RGB {
    public int red = 0;
    public int green = 0;
    public int blue = 0;
    public RGB() { this(0, 0, 0); }
    public RGB(int red, int green, int blue) {
      this.red = red;
      this.green = green;
      this.blue = blue;
    }
  }

  private static void avgRGB(BufferedImage img) {
    RGB avg = new RGB();
    
    // In a circle of diameter 71 pixels (like our tiles) to omit the corners
    // and only take an inscribed square we start ~10 pixels in and end 10
    // pixels early. This examines only the 50x50 square inscribed in the tile.
    int OFFSET = 16;
    
    for (int y = OFFSET; y < img.getHeight() - OFFSET; y++) {
      for (int x = OFFSET; x < img.getWidth() - OFFSET; x++) {
        int pixel = img.getRGB(x, y);
        int red   = pixel >> 16 & 0xff;
        int green = pixel >>  8 & 0xff;
        int blue  = pixel       & 0xff;
        avg.red += red;
        avg.green += green;
        avg.blue += blue;
      }
    }
    
    int numPixels = (img.getHeight() - 2 * OFFSET) * (img.getWidth() - 2 * OFFSET);
    avg.red /= numPixels;
    avg.green /= numPixels;
    avg.blue /= numPixels;
    System.out.format("Avg: %d, %d, %d%n", avg.red, avg.green, avg.blue);
    //return avg;
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
        avgRGB(img);
      } catch (IOException e) {
        System.err.println("Could not read file " + file.getPath() + "; error: " + e.toString());
      }
    }
  }
}