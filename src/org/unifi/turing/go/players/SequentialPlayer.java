package org.unifi.turing.go.players;

import org.unifi.turing.go.Goban;
import org.unifi.turing.go.Stone;

public class SequentialPlayer extends AbstractPlayer {
	private int[][] board;
	private int myID;
	private Goban goban;

	public SequentialPlayer() {
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
		Stone stone = myID == 1 ? Stone.BLACK : Stone.WHITE;
		for (int r = 0; r < n; r++) {
			for (int c = 0; c < n; c++) {
				if (goban.getStone(r, c) == stone) {
					board[r][c] = myID;
				} else if (goban.getStone(r, c) == Stone.NONE) {
					board[r][c] = 0;
				} else {
					board[r][c] = 3 - myID;
				}
			}
		}
		for (int r = 0; r < n; r++) {
			for (int c = 0; c < n; c++) {
				if (board[r][c] == 0) {
					board[r][c] = myID;
					return new int[] { r, c };
				}
			}
		}
		return null;
	}
}
