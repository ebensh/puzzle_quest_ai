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
  
  public void setTile(int row, int column, Tile tile) {
    if (row < 0 || row >= rows()) {
      throw new ArrayIndexOutOfBoundsException(row);
    }
    if (column < 0 || column >= columns()) {
      throw new ArrayIndexOutOfBoundsException(column);
    }
    this.tiles[row][column] = tile;
  }
  
  public Tile getTile(int row, int column) {
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