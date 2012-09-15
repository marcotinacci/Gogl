package org.unifi.turing.go.players;

import java.util.LinkedList;

import org.unifi.turing.go.Goban;
import org.unifi.turing.go.Stone;

public class RandomValidPlayer extends AbstractPlayer {
	private int[][] board;
	private int myID;
	private Goban goban;

	private class Position {
		private int x;
		private int y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public RandomValidPlayer() {
		this.goban = new Goban();
		int n = goban.getSize();
		board = new int[n][n];
		for (int r = 0; r < n; r++) {
			for (int c = 0; c < n; c++) {
				board[r][c] = 0;
			}
		}
	}

	public void black(boolean flag) {
		myID = flag ? 1 : 2;
	}

	public int[] nextStone() {
		if (lastAdversaryMove != null) {
			goban.putStoneAt(myID == 1 ? Stone.WHITE : Stone.BLACK, lastAdversaryMove[0], lastAdversaryMove[1]);
		}
		int n = goban.getSize();
		Stone myStone = myID == 1 ? Stone.BLACK : Stone.WHITE;
		for (int r = 0; r < n; r++) {
			for (int c = 0; c < n; c++) {
				if (goban.getStone(r, c) == myStone) {
					board[r][c] = myID;
				} else if (goban.getStone(r, c) == Stone.NONE) {
					board[r][c] = 0;
				} else {
					board[r][c] = 3 - myID;
				}
			}
		}
		LinkedList<Position> candidates = new LinkedList<Position>();
		for (int r = 0; r < n; r++) {
			for (int c = 0; c < n; c++) {
				if (board[r][c] == 0
						&& (!goban.isSuicide(myStone, r, c) || goban.isCapture(
								myStone, r, c))) {
					candidates.add(new Position(r, c));
				}
			}
		}
		if (candidates.size() > 0) {
			int chosen = (int) (Math.random() * candidates.size());
			Position p = candidates.get(chosen);
			board[p.x][p.y] = myID;
			goban.putStoneAt(myStone, p.x, p.y);
			return new int[] { p.x, p.y };
		} else {
			return null;
		}
	}
}
