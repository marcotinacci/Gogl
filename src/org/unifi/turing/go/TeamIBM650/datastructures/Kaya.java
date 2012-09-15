/*
 * Kaya.java
 *
 * Copyright (c) 2012, YOUR_NAME. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.unifi.turing.go.TeamIBM650.datastructures;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Marco Tinacci, Tommaso Turchi
 *
 */
public class Kaya {

	/**
	 * @return the board
	 */
	public int[][] getGoban() {
		return goban;
	}

	/**
	 * @param board
	 *            the board to set
	 */
	public void setGoban(int[][] goban) {
		this.goban = goban;
	}

	private int[][] goban;

	/**
	 *
	 */
	public Kaya(int size) {
		this.goban = new int[size][size];
		for (int r = 0; r < size; r++)
			for (int c = 0; c < size; c++)
				this.goban[r][c] = 0;
	}

	private void mark(int r, int c) {
		boolean[][] reached = new boolean[goban.length][goban.length];
		for (int i = 0; i < goban.length; i++) {
			for (int j = 0; j < goban.length; j++) {
				reached[i][j] = false;
			}
		}
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(r * goban.length + c);
		reached[r][c] = true;
		while (!q.isEmpty()) {
			int e = q.poll();
			int r1 = e / goban.length;
			int c1 = e % goban.length;
			if (goban[r1][c1] != 2 && goban[r1][c1] != 1) {
				LinkedList<Position> neighbors = this.getNeighbors(r1, c1,
						goban.length);
				for (Position p : neighbors) {
					if (!reached[p.getX()][p.getY()]) {
						reached[p.getX()][p.getY()] = true;
						q.add(p.getX() * goban.length + p.getY());
					}
				}
			}
		}
		boolean white = false;
		boolean black = false;
		for (int i = 0; i < goban.length; i++) {
			for (int j = 0; j < goban.length; j++) {
				if (reached[i][j] == true) {
					if (goban[i][j] == 2) {
						white = true;
					}
					if (goban[i][j] == 1) {
						black = true;
					}
				}
			}
		}
		if (white && !black) {
			for (int i = 0; i < goban.length; i++) {
				for (int j = 0; j < goban.length; j++) {
					if (reached[i][j] == true) {
						goban[i][j] = 2;
					}
				}
			}
		} else if (black && !white) {
			for (int i = 0; i < goban.length; i++) {
				for (int j = 0; j < goban.length; j++) {
					if (reached[i][j] == true) {
						goban[i][j] = 1;
					}
				}
			}
		} else if (black && white) {
			for (int i = 0; i < goban.length; i++) {
				for (int j = 0; j < goban.length; j++) {
					if (reached[i][j] == true && goban[i][j] != 1
							&& goban[i][j] != 2) {
						goban[i][j] = 0;
					}
				}
			}
		}
	}

	public double[] computeFinalScore() {
		// Mark territories
		for (int r = 0; r < goban.length; r++) {
			for (int c = 0; c < goban.length; c++) {
				if (goban[r][c] == 0) {
					mark(r, c);
				}
			}
		}

		/*
		 * For each player, computes the number of the stones of the player plus
		 * the number of crossing points dominated by the player
		 */
		double[] rst = new double[2];
		rst[0] = 0;
		rst[1] = 0;
		for (int r = 0; r < goban.length; r++) {
			for (int c = 0; c < goban.length; c++) {
				if (goban[r][c] == 1) {
					rst[0] = rst[0] + 1;
				} else if (goban[r][c] == 2) {
					rst[1] = rst[1] + 1;
				}
			}
		}
		// Add 7.5 to the score of the white
		rst[1] = rst[1] + 7.5;
		return rst;
	}

	protected LinkedList<Position> getNeighbors(int i, int j, int size) {
		LinkedList<Position> neighbors = new LinkedList<Position>();
		if (i > 0) {
			neighbors.add(new Position(i - 1, j));
		}
		if (j > 0) {
			neighbors.add(new Position(i, j - 1));
		}
		if (i < size - 1) {
			neighbors.add(new Position(i + 1, j));
		}
		if (j < size - 1) {
			neighbors.add(new Position(i, j + 1));
		}
		return neighbors;
	}

}
