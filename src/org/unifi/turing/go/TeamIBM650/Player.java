/*
 * Player.java
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
package org.unifi.turing.go.TeamIBM650;

import java.util.Collections;
import java.util.LinkedList;

import org.unifi.turing.go.Goban;
import org.unifi.turing.go.Stone;
import org.unifi.turing.go.TeamIBM650.datastructures.Position;
import org.unifi.turing.go.players.AbstractPlayer;

/**
 * @author Marco Tinacci, Tommaso Turchi
 *
 */
public class Player extends AbstractPlayer {

	private int myID;
	private Goban goban;
	private PatternSeeker inspector;

	/**
	 *
	 */
	public Player() {
		this.goban = new Goban();
		this.inspector = new PatternSeeker(goban.getSize());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.unifi.turing.go.players.Player#black(boolean)
	 */
	public void black(boolean flag) {
		myID = flag ? 1 : 2;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.unifi.turing.go.players.Player#nextStone()
	 */
	public int[] nextStone() {
		if (lastAdversaryMove != null) {
			goban.putStoneAt(myID == 1 ? Stone.WHITE : Stone.BLACK,
					lastAdversaryMove[0], lastAdversaryMove[1]);
			inspector.ban.getGoban()[lastAdversaryMove[0]][lastAdversaryMove[1]] = 3 - myID;
		} else {
			double[] score = inspector.ban.computeFinalScore();
			if (score[myID - 1] > score[myID % 2])
				return null;
		}
		int n = goban.getSize();
		Stone myStone = myID == 1 ? Stone.BLACK : Stone.WHITE;

		for (int r = 0; r < n; r++)
			for (int c = 0; c < n; c++)
				if (goban.getStone(r, c) == myStone)
					inspector.ban.getGoban()[r][c] = myID;
				else if (goban.getStone(r, c) == Stone.NONE)
					inspector.ban.getGoban()[r][c] = 0;
				else
					inspector.ban.getGoban()[r][c] = 3 - myID;

		LinkedList<Position> candidates = inspector.seek();
		if (candidates.size() > 0) {
			Collections.shuffle(candidates);
			for (Position p : candidates)
				if ((goban.getStone(p.getX(), p.getY()) == Stone.NONE)
						&& (!goban.isSuicide(myStone, p.getX(), p.getY()) || goban
								.isCapture(myStone, p.getX(), p.getY()))) {
					goban.putStoneAt(myStone, p.getX(), p.getY());
					return new int[] { p.getX(), p.getY() };
				}
		}
		candidates.clear();
		for (int r = 0; r < n; r++)
			for (int c = 0; c < n; c++)
				if ((goban.getStone(r, c) == Stone.NONE)
						&& (!goban.isSuicide(myStone, r, c) || goban.isCapture(
								myStone, r, c)))
					candidates.add(new Position(r, c));

		if (candidates.size() > 0) {
			Position p = candidates.get((int) (Math.random() * candidates
					.size()));
			goban.putStoneAt(myStone, p.getX(), p.getY());
			return new int[] { p.getX(), p.getY() };
		} else
			return null;
	}
}
