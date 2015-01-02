import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

// What constitutes a "move"?
// Action, Gain, NewState

// We have: a State
// For each legal Action, such as swapping tiles, we have a transition to a new State

// We first need to identify all legal one-step Actions, their Gains, and their resulting NewStates.



public class GameLogic {
	public static class Swap {
		public final Pair<Integer, Integer> src;
		public final Pair<Integer, Integer> dest;
		
		public Swap(int x1, int y1, int x2, int y2) {
			this.src = new ImmutablePair<Integer, Integer>(x1, y1);
			this.dest = new ImmutablePair<Integer, Integer>(x2, y2);
		}
		public Swap(Pair<Integer, Integer> src, Pair<Integer, Integer> dest) {
			this.src = src;
			this.dest = dest;
		}
		
		@Override
		public String toString() {
			return "Swap [src=" + src + ", dest=" + dest + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dest == null) ? 0 : dest.hashCode());
			result = prime * result + ((src == null) ? 0 : src.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Swap other = (Swap) obj;
			if (dest == null) {
				if (other.dest != null)
					return false;
			} else if (!dest.equals(other.dest))
				return false;
			if (src == null) {
				if (other.src != null)
					return false;
			} else if (!src.equals(other.src))
				return false;
			return true;
		}

	}

	public static Collection<Swap> getValidSwaps(Board board) {
		// TODO(ebensh): Optimize.
		ArrayList<Swap> swaps = new ArrayList<Swap>();

		// Horizontal swaps.
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.columns() - 1; c++) {
				// If we were to swap horizontally:
				Tile right = board.getTile(r, c);
				Tile left = board.getTile(r, c + 1);
				if (left.matches(right)) { continue; }

				// Search for matches to the left of left.
				if (c >= 2 && left == board.getTile(r, c-1) && left == board.getTile(r, c-2)) {
					swaps.add(new Swap(r, c, r, c+1));
					continue;
				}
				
				// Search for matches to the right of right.
				if (c < board.columns() - 2 && right == board.getTile(r, c+1) && right == board.getTile(r, c+2)) {
					swaps.add(new Swap(r, c, r, c+1));
					continue;
				}

				// Now look for a vertical match by first crawling up, then down, from
				// the left point.
				int match_count = 1;
				for (int k = r - 1;
						k >= 0 && left.matches(board.getTile(k, c));
						k--, match_count++) { }
				for (int k = r + 1;
						k < board.rows() && left.matches(board.getTile(k, c));
						k++, match_count++) { }
				if (match_count >= 3) {
					swaps.add(new Swap(r, c, r, c+1));
					continue;
				}
				
				// Finally, look for a vertical match by first crawling up, then down, from
				// the right point.
				match_count = 1;
				for (int k = r - 1;
						k >= 0 && right.matches(board.getTile(k, c));
						k--, match_count++) { }
				for (int k = r + 1;
						k < board.rows() && right.matches(board.getTile(k, c));
						k++, match_count++) { }
				if (match_count >= 3) {
					swaps.add(new Swap(r, c, r, c+1));
					continue;
				}
			}
		}
		
		// Vertical swaps.
		for (int c = 0; c < board.columns() ; c++) {
			for (int r = 0; r < board.rows() - 1; r++) {
				// If we were to swap vertically:
				Tile top = board.getTile(r, c);
				Tile bottom = board.getTile(r + 1, c);
				if (top.matches(bottom)) { continue; }

				// Search for matches to the top of top.
				if (r >= 2 && top == board.getTile(r-1, c) && top == board.getTile(r-2, c)) {
					swaps.add(new Swap(r, c, r+1, c));
					continue;
				}
				
				// Search for matches to the bottom of bottom.
				if (r < board.rows() - 2 && bottom == board.getTile(r+1, c) && bottom == board.getTile(r+2, c)) {
					swaps.add(new Swap(r, c, r+1, c));
					continue;
				}

				// Now look for a horizontal match by first crawling left, then right, from
				// the top point.
				int match_count = 1;
				for (int k = c - 1;
						k >= 0 && top.matches(board.getTile(r, k));
						k--, match_count++) { }
				for (int k = c + 1;
						k < board.columns() && top.matches(board.getTile(r, k));
						k++, match_count++) { }
				if (match_count >= 3) {
					swaps.add(new Swap(r, c, r+1, c));
					continue;
				}
				
				// Finally, look for a horizontal match by first crawling left, then right, from
				// the bottom point.
				match_count = 1;
				for (int k = c - 1;
						k >= 0 && bottom.matches(board.getTile(r, k));
						k--, match_count++) { }
				for (int k = c + 1;
						k < board.columns() && bottom.matches(board.getTile(r, k));
						k++, match_count++) { }
				if (match_count >= 3) {
					swaps.add(new Swap(r, c, r+1, c));
					continue;
				}
			}
		}
		
		return swaps;
	}
}