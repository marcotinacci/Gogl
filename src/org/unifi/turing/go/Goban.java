/*
 * Goban.java
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
package org.unifi.turing.go;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Set;

public class Goban extends Observable {
	private class Chain {
		private Set<Position> chain;
		private Stone stone;

		private Chain() {
			chain = new HashSet<Position>();
			stone = Stone.NONE;
		}

		private Chain(Stone s, int i, int j) {
			chain = new HashSet<Position>();
			chain.add(new Position(i, j));
			stone = s;
		}

		private Set<Position> getChain() {
			return chain;
		}

		private Set<Position> getFreedoms() {
			Set<Position> freedoms = new HashSet<Position>();
			for (Position p : chain) {
				LinkedList<Position> neighbors = getNeighbors(p.getX(),
						p.getY());
				for (Position n : neighbors) {
					if (goban[n.getX()][n.getY()].isEmpty()) {
						freedoms.add(n);
					}
				}
			}
			return freedoms;
		}

		private Stone getStone() {
			return stone;
		}

		private boolean isEmpty() {
			return stone == Stone.NONE;
		}

		private boolean join(Chain c) {
			return chain.addAll(c.getChain());
		}

		private void refresh() {
			for (Position p : chain) {
				goban[p.getX()][p.getY()] = this;
			}
		}

		private void remove() {
			for (Position p : chain) {
				goban[p.getX()][p.getY()] = new Chain();
			}
		}
	}

	protected class Position {
		private int x;
		private int y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public boolean equals(Object arg0) {
			if (arg0 instanceof Position) {
				Position p = (Position) arg0;
				return (p.x == x) && (p.y == y);
			}
			return false;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int hashCode() {
			return x * size + y;
		}

		public String toString() {
			return "(" + x + "," + y + ")";
		}
	}

	private Chain[][] goban;

	private int size = DefaultValues.DEFAULT_SIZE;

	public Goban() {
		this(DefaultValues.DEFAULT_SIZE);
	}

	public Goban(int size) {
		this.size = size;
		this.goban = new Chain[size][size];
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				goban[r][c] = new Chain();
			}
		}
	}

	protected LinkedList<Position> getNeighbors(int i, int j) {
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

	public int getSize() {
		return size;
	}

	public Stone getStone(int i, int j) {
		return goban[i][j].getStone();
	}

	public boolean isCapture(Stone stone, int i, int j) {
		LinkedList<Position> neighbors = getNeighbors(i, j);
		for (Position p : neighbors) {
			Chain c = goban[p.getX()][p.getY()];
			if (!c.isEmpty() && c.getStone() != stone
					&& c.getFreedoms().size() == 1) {
				return true;
			}
		}
		return false;
	}

	public boolean isSuicide(Stone stone, int i, int j) {
		Stone currentStone = getStone(i, j);
		setStone(stone, i, j);
		Chain newChain = new Chain(stone, i, j);
		LinkedList<Position> neighbors = getNeighbors(i, j);
		for (Position p : neighbors) {
			Chain c = goban[p.getX()][p.getY()];
			if (c.getStone() == stone) {
				newChain.join(c);
			}
		}
		int freedomNumber = newChain.getFreedoms().size();
		setStone(currentStone, i, j);
		return freedomNumber == 0;
	}

	public boolean putStoneAt(Stone stone, int i, int j) {
		if ((i<0)||(i>=size)||(j<0)||(j>=size)) {
			return false;
		}
		if (!goban[i][j].isEmpty()) {
			return false;
		}
		if (!isCapture(stone, i, j) && isSuicide(stone, i, j)) {
			return false;
		}
		LinkedList<Position> neighbors = getNeighbors(i, j);
		for (Position p : neighbors) {
			Chain c = goban[p.getX()][p.getY()];
			if (!c.isEmpty()) {
				if ((c.getStone() != stone) && (c.getFreedoms().size() == 1)) {
					c.remove();
				}
			}
		}
		Chain newChain = new Chain(stone, i, j);
		for (Position p : neighbors) {
			Chain c = goban[p.getX()][p.getY()];
			if (c.getStone() == stone) {
				newChain.join(c);
			}
		}
		newChain.refresh();
		setChanged();
		notifyObservers();
		return true;
	}

	protected void setStone(Stone stone, int i, int j) {
		goban[i][j].stone = stone;
	}
}
