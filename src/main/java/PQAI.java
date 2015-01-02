import java.io.File;
import java.util.Collection;
import java.util.Scanner;

public class PQAI {
	public static void main(String[] args) throws Exception {
		if (args.length != 0) {
			System.out.println("Zero args please.");
			return;
		}

		Scanner keyboard = new Scanner(System.in);
		while (true) {
			System.out.println("Press [Enter] to continue, or q[Enter] to quit.");
			if (keyboard.nextLine().equals("q")) { break; }

			//Eyes parser = Eyes.createFromScreen();
			Eyes parser = Eyes.createFromFile(new File("src/test/resources/test_images/1.png"));
			Board board = parser.getBoard();
			System.out.println(board.toString());
			Collection<GameLogic.Swap> swaps = GameLogic.getValidSwaps(board);
			for (GameLogic.Swap swap : swaps) {
				System.out.println(swap);
			}
			// TODO(ebensh): Make move
		}
		keyboard.close();
	}
}