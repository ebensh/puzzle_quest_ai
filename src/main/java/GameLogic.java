import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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

	public static class Match {
		public final Tile tile;
		public final int length;
		public final int value;
		public Match(Tile tile, int length, int value) {
			this.tile = tile;
			this.length = length;
			this.value = value;
		}
		@Override
		public String toString() {
			return "Match [tile=" + tile + ", length=" + length + ", value="
					+ value + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + length;
			result = prime * result + ((tile == null) ? 0 : tile.hashCode());
			result = prime * result + value;
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
			Match other = (Match) obj;
			if (length != other.length)
				return false;
			if (tile != other.tile)
				return false;
			if (value != other.value)
				return false;
			return true;
		}

		public static Comparator<Match> compareByLengthThenValue() {
	        return new Comparator<Match>() {
				public int compare(Match left, Match right) {
					if (left.length > right.length) { return 1; }
					if (left.length < right.length) { return -1; }
					if (left.value > right.value) { return 1; }
					if (left.value < right.value) { return -1; }
					return 0;
				}
	        };
	    }

	}
	
	public static Collection<Swap> getValidSwaps(Board board) {
		// TODO(ebensh): Clean up.
		ArrayList<Swap> swaps = new ArrayList<Swap>();

		// Horizontal swaps.
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.columns() - 1; c++) {
				// If we were to swap horizontally:
				Tile right = board.get(r, c);
				Tile left = board.get(r, c + 1);
				if (left.matches(right)) { continue; }

				// Search for matches to the left of left.
				if (c >= 2 && left == board.get(r, c-1) && left == board.get(r, c-2)) {
					swaps.add(new Swap(r, c, r, c+1));
					continue;
				}
				
				// Search for matches to the right of right.
				if (c < board.columns() - 2 && right == board.get(r, c+1) && right == board.get(r, c+2)) {
					swaps.add(new Swap(r, c, r, c+1));
					continue;
				}

				// Now look for a vertical match by first crawling up, then down, from
				// the left point.
				int match_count = 1;
				for (int k = r - 1;
						k >= 0 && left.matches(board.get(k, c));
						k--, match_count++) { }
				for (int k = r + 1;
						k < board.rows() && left.matches(board.get(k, c));
						k++, match_count++) { }
				if (match_count >= 3) {
					swaps.add(new Swap(r, c, r, c+1));
					continue;
				}
				
				// Finally, look for a vertical match by first crawling up, then down, from
				// the right point.
				match_count = 1;
				for (int k = r - 1;
						k >= 0 && right.matches(board.get(k, c));
						k--, match_count++) { }
				for (int k = r + 1;
						k < board.rows() && right.matches(board.get(k, c));
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
				Tile top = board.get(r, c);
				Tile bottom = board.get(r + 1, c);
				if (top.matches(bottom)) { continue; }

				// Search for matches to the top of top.
				if (r >= 2 && top == board.get(r-1, c) && top == board.get(r-2, c)) {
					swaps.add(new Swap(r, c, r+1, c));
					continue;
				}
				
				// Search for matches to the bottom of bottom.
				if (r < board.rows() - 2 && bottom == board.get(r+1, c) && bottom == board.get(r+2, c)) {
					swaps.add(new Swap(r, c, r+1, c));
					continue;
				}

				// Now look for a horizontal match by first crawling left, then right, from
				// the top point.
				int match_count = 1;
				for (int k = c - 1;
						k >= 0 && top.matches(board.get(r, k));
						k--, match_count++) { }
				for (int k = c + 1;
						k < board.columns() && top.matches(board.get(r, k));
						k++, match_count++) { }
				if (match_count >= 3) {
					swaps.add(new Swap(r, c, r+1, c));
					continue;
				}
				
				// Finally, look for a horizontal match by first crawling left, then right, from
				// the bottom point.
				match_count = 1;
				for (int k = c - 1;
						k >= 0 && bottom.matches(board.get(r, k));
						k--, match_count++) { }
				for (int k = c + 1;
						k < board.columns() && bottom.matches(board.get(r, k));
						k++, match_count++) { }
				if (match_count >= 3) {
					swaps.add(new Swap(r, c, r+1, c));
					continue;
				}
			}
		}
		
		return swaps;
	}
	
	public static Pair<Board, List<Match>> getMatchesAndRemoveTiles(Board board) {
		ArrayList<Match> matches = new ArrayList<Match>();
		
		boolean marked[][] = new boolean[board.rows()][board.columns()];  // default false
		// Note that we go through each tile, as there may be WILD WILD ELEMENTAL (edge).
		// Check for horizontal matches.
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.columns(); c++) {
				Tile type = board.get(r, c);
				if (type.isWild()) { continue; }  // Start on a non-wild (will include it by going left).
				int matchValue = 1;
				int multiplier = 1;

				// Find the left edge of the match.
				int left = c;
				while(left - 1 >= 0 && board.get(r, left - 1).matches(type)) {
					left--;
					multiplier *= board.get(r, left).multiplier();
					matchValue += board.get(r, left).matchValue();
				}
				int right = c;
				while(right + 1 < board.columns() && board.get(r, right + 1).matches(type)) {
					right++;
					multiplier *= board.get(r, right).multiplier();
					matchValue += board.get(r, left).matchValue();
				}
				
				int matchLength = right - left + 1;
				if (matchLength >= 3) {
					matches.add(new Match(type, matchLength, matchValue * multiplier));
					// Mark the matched tiles for removal.
					for (int k = left; k <= right; k++) {
						marked[r][k] = true; 
					}
					
					// Move to the next potential match.
					c = right + 1;
				}
			}
		}
		
		// Check for vertical matches.
		for (int c = 0; c < board.columns(); c++) {
			for (int r = 0; r < board.rows(); r++) {
				Tile type = board.get(r, c);
				if (type.isWild()) { continue; }  // Start on a non-wild (will include it by going up).
				int matchValue = 1;
				int multiplier = 1;

				// Find the top edge of the match.
				int top = r;
				while(top - 1 >= 0 && board.get(top - 1, c).matches(type)) {
					top--;
					multiplier *= board.get(top, c).multiplier();
					matchValue += board.get(top, c).matchValue();
				}
				int bottom = r;
				while(bottom + 1 < board.rows() && board.get(bottom + 1, c).matches(type)) {
					bottom++;
					multiplier *= board.get(bottom, c).multiplier();
					matchValue += board.get(bottom, c).matchValue();
				}

				int matchLength = bottom - top + 1;
				if (matchLength >= 3) {
					matches.add(new Match(type, matchLength, matchValue * multiplier));
					// Mark the matched tiles for removal.
					for (int k = top; k <= bottom; k++) {
						marked[k][c] = true; 
					}

					// Move to the next potential match.
					r = bottom + 1;
				}
			}
		}
		
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.columns(); c++) {
				if (marked[r][c]) { board.set(r, c, Tile.EMPTY); }
			}
		}
		
		return new ImmutablePair<Board, List<Match>>(board, matches);
	}
	
	public static Pair<Board, List<Match>> swapAndGetResults(Board board_before, Swap swap) {
		Board board = new Board(board_before);  // Make a copy to manipulate.
		board.swap(swap);
		ArrayList<Match> matches = new ArrayList<Match>();
		while(true) {
			Pair<Board, List<Match>> results = getMatchesAndRemoveTiles(board);
			List<Match> newMatches = results.getRight();
			if (newMatches.size() == 0) {
				break;
			}
			matches.addAll(newMatches);
			board = results.getLeft();
			board.drop();
			
		}
		
		matches.sort(Match.compareByLengthThenValue().reversed());
		
		return new ImmutablePair<Board, List<Match>>(board, matches);
	}
	
	public static Swap getBestSwap(Board board, Collection<Swap> swaps) {
		Match bestMatch = new Match(Tile.EMPTY, 0, 0);
		Swap bestSwap = new Swap(0, 0, 0, 0);
		
		for (Swap swap : swaps) {
			Pair<Board, List<Match>> result = swapAndGetResults(board, swap);
			if (result.getRight().size() > 0 &&
					Match.compareByLengthThenValue().compare(result.getRight().get(0), bestMatch) > 0) {
				bestMatch = result.getRight().get(0);
				bestSwap = swap;
				System.out.println("New best: " + bestSwap.toString() + ", " + bestMatch.toString());
			}
		}
		
		return bestSwap;
	}
}