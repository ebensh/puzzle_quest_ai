import java.lang.ArrayIndexOutOfBoundsException;
import java.lang.StringBuilder;

public class Board {
	private Tile[][] tiles;

	public Board(int rows, int columns) {
		this.tiles = new Tile[rows][columns];
	}

	// Copy constructor.
	public Board(Board otherBoard) {
		this(otherBoard.rows(), otherBoard.columns());
		for (int i = 0; i < otherBoard.tiles.length; i++) {
			System.arraycopy(otherBoard.tiles[i], 0, this.tiles[i], 0, otherBoard.tiles[i].length);
		}
	}

	public void swap(int row1, int col1, int row2, int col2) {
		Tile temp = this.tiles[row1][col1];
		this.tiles[row1][col1] = this.tiles[row2][col2];
		this.tiles[row2][col2] = temp;
	}
	public void swap(GameLogic.Swap s) {
		swap(s.src.getLeft(), s.src.getRight(), s.dest.getLeft(), s.dest.getRight());
	}
	
	public void drop() {
		for (int c = 0; c < columns(); c++) {
			int rowForTile = rows() - 1;
			for (int r = rows() - 1; r >= 0; r--) {
				Tile type = this.tiles[r][c]; 
				if (type == Tile.EMPTY) {
					continue;
				} else if (r == rowForTile) {
					rowForTile--;
				} else {
					swap(r, c, rowForTile, c);
					rowForTile--;
				}
			}
		}
	}
	
	public void set(int row, int column, Tile tile) {
		if (row < 0 || row >= rows()) {
			throw new ArrayIndexOutOfBoundsException(row);
		}
		if (column < 0 || column >= columns()) {
			throw new ArrayIndexOutOfBoundsException(column);
		}
		this.tiles[row][column] = tile;
	}

	public Tile get(int row, int column) {
		if (row < 0 || row >= rows()) {
			throw new ArrayIndexOutOfBoundsException(row);
		}
		if (column < 0 || column >= columns()) {
			throw new ArrayIndexOutOfBoundsException(column);
		}
		return this.tiles[row][column];
	}

	public int rows() { return this.tiles.length; }
	public int columns() { return this.tiles[0].length; }

	@Override
	public String toString() {
		// 8 chars per tile, new line => plenty
		StringBuilder str = new StringBuilder(rows() * columns() * 9);
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < columns(); j++) {
				str.append(String.format("%-8s", this.tiles[i][j].toString()));
			}
			str.append(String.format("%n"));
		}
		return str.toString();
	}
}