import java.io.File;

public class PQAI {
  public static void main(String[] args) throws Exception {
    if (args.length != 0) {
      System.out.println("Zero args please.");
      return;
    }

    VisualParser parser = VisualParser.createFromFile(new File("C:/code/puzzle_quest_ai/test_images/1.png"));
    parser.saveDebugImageToFile(new File("C:/tmp/out.png"));
    parser.saveTrainingTilesToDirectory(new File("C:/tmp/"));
    Board board = parser.getBoard();
    
    System.out.println(board.toString());
  }
}