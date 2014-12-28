public class PQAI {
  public static void main(String[] args) throws Exception {
    if (args.length != 0) {
      System.out.println("Zero args please.");
      return;
    }

    // Get BufferedImage of PQ Window (or source from file - param? const?)
    // Return 2d enum of board
    Board board = VisualParser.getBoard("C:/code/puzzle_quest_ai/test_images/1.png");
    System.out.println(board.toString());
  }
}