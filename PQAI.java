import java.io.File;
import java.util.Scanner;

public class PQAI {
  public static void main(String[] args) throws Exception {
    if (args.length != 0) {
      System.out.println("Zero args please.");
      return;
    }
    
    Scanner keyboard = new Scanner(System.in);
    while (true) {
      System.out.println("Press [Enter] to continue.");
      keyboard.nextLine();
      Eyes parser = Eyes.createFromScreen();
      Board board = parser.getBoard();
      System.out.println(board.toString());
      // TODO(ebensh): Make move
    }
  }
}