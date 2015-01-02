import java.awt.Point;

// What constitutes a "move"?
// Action, Gain, NewState

// We have: a State
// For each legal Action, such as swapping tiles, we have a transition to a new State

// We first need to identify all legal one-step Actions, their Gains, and their resulting NewStates.



public class GameLogic {
  public class Swap {
    public final Point src;
    public final Point dest;
    public Swap(Coord src, Coord dest) { this.src = src; this.dest = dest; }
  }
  
  public static getValidSwaps(Board board) {
    // TODO(ebensh): Optimize.
    ArrayList<Swap> swaps = new ArrayList();
    
    // Horizontal swaps.
    for (int r = 0; r < board.rows(); r++) {
      for (int c = 0; c < board.columns() - 1; c++) {
        // If we were to swap horizontally:
        Tile right = board.getTile(r, c);
        Tile left = board.getTile(r, c + 1);
        if (left.matches(right)) { continue; }
        
        // Search for matches to the left of left.
        if (c >= 2 && left == board.getTile(r, c-1) && left == board.getTile(r, c-2)) {
          swaps.add(new Swap(new Point(r, c), new Point(r, c+1)));
          continue;
        }
        
        // Now look for a vertical match by first crawling up, then down, from
        // the left point.
        int match_count = 1;
        for (int k = r - 1;
             k >= 0 && left.matches(board.getTile(k, c));
             k--, v_match++) { }
        for (int k = r + 1;
             k < board.rows() && left.matches(board.getTile(k, c));
             k++, v_match++) { }
        // TODO(ebensh): Working on matches here...
      }
    }
  }
}