package org.unifi.turing.go.players;

public class RandomPlayer extends AbstractPlayer {
	private final int n = 19;
	
	public void black(boolean flag) {
	}

	public int[] nextStone() {
		int r = (int) (Math.random() * n);
		int c = (int) (Math.random() * n);
		return new int[] { r, c };
	}
}
